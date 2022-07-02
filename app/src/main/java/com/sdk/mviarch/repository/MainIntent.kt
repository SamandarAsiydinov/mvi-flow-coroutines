package com.sdk.mviarch.repository

sealed class MainIntent {
    object FetchUser: MainIntent()
}