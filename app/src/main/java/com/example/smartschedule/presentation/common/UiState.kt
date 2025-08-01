package com.example.smartschedule.presentation.common

sealed class UiState<out T> {
    data class Success<T>(val data: T) : UiState<T>()
    data class Error(val error: ErrorState) : UiState<Nothing>()
    object Loading : UiState<Nothing>()

}