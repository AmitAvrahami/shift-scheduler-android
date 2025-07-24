package com.example.smartschedule.presentation.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.smartschedule.domain.models.User
import com.example.smartschedule.domain.repository.UserRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class UserViewModel @Inject constructor(
    private val userRepository: UserRepository
) : ViewModel() {

    private val _users = MutableStateFlow<List<User>>(emptyList())
    val users : StateFlow<List<User>> = _users.asStateFlow()
    //UI States
    private val _isLoading = MutableStateFlow(false)
    val isLoading : StateFlow<Boolean> = _isLoading.asStateFlow()
    private val _errorMessage = MutableStateFlow<String?>(null)
    val errorMessage: StateFlow<String?> = _errorMessage.asStateFlow()

    init {
        loadUsers()
    }

    fun loadUsers() {
        viewModelScope.launch {
            _isLoading.value = true
            _errorMessage.value = null

            try {
                userRepository.getAllUsers().collect { userList ->
                    _users.value = userList
                    _isLoading.value = false
                }
            }catch (e: Exception){
                _errorMessage.value = "שגיאה בטעינת משתמשים: ${e.message}"
                _isLoading.value = false
            }
        }
    }

    fun addUser(user: User, password : String){
        viewModelScope.launch {
            _isLoading.value = true
            _errorMessage.value = null

            try {
                val success = userRepository.registerUser(user,password)
                if (!success) {
                    _errorMessage.value = "שגיאה ביצירת משתמש"
                }
            } catch (e: Exception) {
                _errorMessage.value = "שגיאה ביצירת משתמש: ${e.message}"
            } finally {
                _isLoading.value = false
            }
        }
    }

    fun deleteUser(user: User) {
        viewModelScope.launch {
            _isLoading.value = true
            _errorMessage.value = null

            try {
                userRepository.deleteUser(user)
            } catch (e: Exception) {
                _errorMessage.value = "שגיאה במחיקת משתמש: ${e.message}"
            } finally {
                _isLoading.value = false
            }
        }
    }

    fun updateUser(user: User) {
        viewModelScope.launch {
            _isLoading.value = true
            _errorMessage.value = null

            try {
                userRepository.insertUser(user)
            } catch (e: Exception) {
                _errorMessage.value = "שגיאה בעדכון משתמש: ${e.message}"
            } finally {
                _isLoading.value = false
            }
        }
    }

    fun clearError() {
        _errorMessage.value = null
    }
}