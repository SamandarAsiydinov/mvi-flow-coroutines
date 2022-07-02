package com.sdk.mviarch.repository

import com.sdk.mviarch.network.ApiService

class UserRepository(private val apiService: ApiService) {
    suspend fun getUsers() = apiService.getUsers()
}