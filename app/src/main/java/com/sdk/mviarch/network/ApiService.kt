package com.sdk.mviarch.network

import com.sdk.mviarch.model.User
import retrofit2.http.GET
import retrofit2.http.Query

interface ApiService {
    @GET("users")
    suspend fun getUsers(
        @Query("q") query: String = "neywork"
    ): List<User>
}