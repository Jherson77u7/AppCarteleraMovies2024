package com.example.myapplication.data

sealed class NetworkResult<out T> {
    data class Success<out T>(val data: T) : NetworkResult<T>()
    data class Error(val error: String): NetworkResult<Nothing>()
}
