package com.deffa.musicplayerapp.utils

sealed class Resource<out T> {
    data class Success<out T>(
        val data: T,
        val code: Int
    ) : Resource<T>()

    data class Error(
        val code: Int,
        val message: String
    ) : Resource<Nothing>()

    data object Empty : Resource<Nothing>()
}