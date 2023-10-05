package com.rohan.findingfalcone.network

import com.rohan.findingfalcone.models.ApiException
import retrofit2.Response

fun <T> handleApiResponse(response: Response<T>): Result<T> {
    return try {
        if (response.isSuccessful) {
            val body = response.body()
            if (body != null) {
                Result.Success(body)
            } else {
                Result.Error(ApiException("Empty response body", response.code()))
            }
        } else {
            val errorBody = response.errorBody()?.string()
            val message = errorBody ?: "Unknown error"
            Result.Error(ApiException(message, response.code()))
        }
    } catch (e: Exception) {
        Result.Error(ApiException(e.message ?: "Unknown error", -1))
    }
}

sealed class Result<out T> {
    data class Success<out T>(val data: T) : Result<T>()
    data class Error(val apiException: ApiException) : Result<Nothing>()
}