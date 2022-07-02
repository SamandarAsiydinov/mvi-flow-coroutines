package com.sdk.mviarch.repository

import com.sdk.mviarch.model.User

sealed class MainState {
    object Init: MainState()
    object Loading: MainState()
    data class Users(val users: List<User>): MainState()
    data class Error(val error: String?): MainState()
}
