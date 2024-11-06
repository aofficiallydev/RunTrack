package com.aofficially.runtrack.ui.login.data

import com.aofficially.runtrack.di.network.HandlerResponse
import com.aofficially.runtrack.di.network.RemoteException
import com.aofficially.runtrack.ui.login.data.network.LoginResponse
import com.aofficially.runtrack.ui.login.data.network.LoginService
import java.net.HttpURLConnection
import javax.inject.Inject

class LoginDataSource @Inject constructor(
    private val api: LoginService
) {

    suspend fun login(username: String, password: String, raceId: String): LoginResponse {
        api.login(username, password, raceId).let { response ->
            return when (val code = response.code()) {
                HttpURLConnection.HTTP_OK -> HandlerResponse.getResponse(response)
                else -> throw RemoteException(
                    code = code,
                    msg = response.message().takeIf { it.isEmpty() }.let {
                        null
                    }
                )
            }
        }
    }
}