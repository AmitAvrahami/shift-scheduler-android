package com.example.smartschedule.presentation.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
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



    fun login(email: String, password: String,onLoginSuccess: (String) -> Unit){
        viewModelScope.launch {
            _isLoading.value = true
            _errorMessage.value = null

            try {
                val user = userRepository.login(email, password)
                if (user != null){
                    _isLoading.value = false
                    onLoginSuccess(user.type.name.lowercase())
                }else {
                    _isLoading.value = false
                    _errorMessage.value = "שם משתמש או סיסמה שגויים"
                }
            }catch (e: Exception){
                _isLoading.value = false
                _errorMessage.value = "שגיאה בהתחברות: ${e.message}"
            }
        }
    }

    fun clearError() {
        _errorMessage.value = null
    }
}