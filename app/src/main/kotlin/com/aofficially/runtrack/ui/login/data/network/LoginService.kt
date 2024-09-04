package com.aofficially.runtrack.ui.login.data.network

import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Path

interface LoginService {

    @GET("user/{username}:{password}")
    suspend fun login(@Path("username") username: String, @Path("password") password: String): Response<LoginResponse>
}