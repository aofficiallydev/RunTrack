package com.aofficially.runtrack.ui.login.data

import com.aofficially.runtrack.ui.login.data.network.LoginResponse
import javax.inject.Inject

class LoginRepository @Inject constructor(
    private val dataSource: LoginDataSource
) {

    suspend fun login(username: String, password: String, raceId: String): LoginResponse {
        return dataSource.login(username, password, raceId)
    }
}