package com.example.smartschedule.presentation.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.smartschedule.domain.common.fold
import com.example.smartschedule.domain.models.User
import com.example.smartschedule.domain.repository.UserRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class LoginViewModel @Inject constructor(
    private val userRepository: UserRepository
) : ViewModel() {

    //UI STATE
    private val _isLoading = MutableStateFlow(false)
    private val _errorMessage = MutableStateFlow<String?>(null)
    val isLoading : StateFlow<Boolean> = _isLoading.asStateFlow()
    val errorMessage : StateFlow<String?> = _errorMessage.asStateFlow()


    fun login(email: String, password: String, onLoginSuccess: (User) -> Unit) {
        _isLoading.value = true
        _errorMessage.value = null
        viewModelScope.launch {
            userRepository.loginWithResult(email, password).fold(
                onSuccess = { user ->
                    _isLoading.value = false
                    onLoginSuccess(user)
                },
                onError = { error ->
                    _isLoading.value = false
                    _errorMessage.value = error.message
                }
            )
        }
    }



    suspend fun getCurrentUser() : User? {
        return userRepository.getCurrentUser()
    }

    fun clearError() {
        _errorMessage.value = null
    }
}