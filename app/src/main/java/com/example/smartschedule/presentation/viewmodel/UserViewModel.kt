package com.example.smartschedule.presentation.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.smartschedule.domain.common.fold
import com.example.smartschedule.domain.models.User
import com.example.smartschedule.domain.repository.UserRepository
import com.example.smartschedule.presentation.common.ErrorState
import com.example.smartschedule.presentation.user.UserUiState
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class UserViewModel @Inject constructor(
    private val userRepository: UserRepository
) : ViewModel() {

    private val _uiState = MutableStateFlow<UserUiState>(UserUiState.Loading)
    val uiState: StateFlow<UserUiState> = _uiState.asStateFlow()

    //UI States
    @Deprecated("Use uiState instead", ReplaceWith("uiState"))
    val users: StateFlow<List<User>?> = uiState
        .map { state -> state.users }
        .stateIn(viewModelScope, SharingStarted.Lazily, emptyList())

    @Deprecated("Use uiState instead", ReplaceWith("uiState"))
    val isLoading: StateFlow<Boolean> = uiState
        .map { state -> state.isLoading }
        .stateIn(viewModelScope, SharingStarted.Lazily, false)

    @Deprecated("Use uiState instead", ReplaceWith("uiState"))
    val errorMessage: StateFlow<String?> = uiState
        .map { state -> state.errorMessage }
        .stateIn(viewModelScope, SharingStarted.Lazily, null)

    init {
        loadUsers()
    }

    fun loadUsers() {
        viewModelScope.launch {
            _uiState.value = UserUiState.Loading

            try {
                userRepository.getAllUsersWithResult().collect { userListRes ->
                    userListRes.fold(
                        onSuccess = { userList ->
                            _uiState.value = UserUiState.Success(userList)
                        },
                        onError = { error ->
                            _uiState.value = UserUiState.Error(ErrorState.fromThrowable(error))
                        }
                    )
                }
            } catch (e: Exception) {
                try {
                    //TODO : This is Fallback to old method if new one doesn't exist yet
                    userRepository.getAllUsers().collect { userList ->
                        _uiState.value = UserUiState.Success(userList)
                    }
                } catch (fallbackError: Exception) {
                    _uiState.value = UserUiState.Error(ErrorState.fromThrowable(fallbackError))
                }
            }
        }
    }

    fun addUser(user: User, password: String) {
        viewModelScope.launch {
            try {
                userRepository.registerUserWithResult(user, password)
                    .fold(
                        onSuccess = {
                            loadUsers()
                        },
                        onError = { error ->
                            _uiState.value = UserUiState.Error(ErrorState.fromThrowable(error))
                        }
                    )
            } catch (e: Exception) {
                //TODO : This is Fallback to old method if new one doesn't exist yet
                try {
                    val success = userRepository.registerUser(user, password)
                    if (success) {
                        loadUsers()
                    } else {
                        _uiState.value =
                            UserUiState.Error(ErrorState.UnknownError("Failed to create user"))
                    }
                } catch (fallbackError: Exception) {
                    _uiState.value = UserUiState.Error(ErrorState.fromThrowable(fallbackError))
                }
            }
        }
    }

    fun deleteUser(user: User) = viewModelScope.launch {
        try {
            userRepository.deleteUserWithResult(user).fold(
                onSuccess = {
                    loadUsers() // Refresh list
                },
                onError = { error ->
                    _uiState.value = UserUiState.Error(ErrorState.fromThrowable(error))
                }
            )
        } catch (e: Exception) {
            //TODO : This is Fallback to old method if new one doesn't exist yet
            try {
                userRepository.deleteUser(user)
                loadUsers()
            } catch (fallbackError: Exception) {
                _uiState.value = UserUiState.Error(ErrorState.fromThrowable(fallbackError))
            }
        }
    }

    fun updateUser(user: User) {
        viewModelScope.launch {
            try {
                userRepository.insertUserWithResult(user).fold(
                    onSuccess = {
                        loadUsers() // Refresh list
                    },
                    onError = { error ->
                        _uiState.value = UserUiState.Error(ErrorState.fromThrowable(error))
                    }
                )
            } catch (e: Exception) {
                //TODO : This is Fallback to old method if new one doesn't exist yet
                try {
                    userRepository.insertUser(user)
                    loadUsers()
                } catch (fallbackError: Exception) {
                    _uiState.value = UserUiState.Error(ErrorState.fromThrowable(fallbackError))
                }
            }
        }
    }

    fun clearError() {
        if (_uiState.value is UserUiState.Error) {
            loadUsers() // Reload data
        }
    }
}