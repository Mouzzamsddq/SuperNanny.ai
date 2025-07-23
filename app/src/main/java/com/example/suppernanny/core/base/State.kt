package com.example.suppernanny.core.base

sealed class State<out T> {
    data object Default: State<Nothing>()
    data object Loading : State<Nothing>()
    data class Success<out T>(val data: T) : State<T>()
    data class Error(val message: String, val throwable: Throwable? = null) : State<Nothing>()
}