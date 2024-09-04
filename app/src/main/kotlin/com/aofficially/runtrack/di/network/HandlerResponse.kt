package com.aofficially.runtrack.di.network

import retrofit2.Response

object HandlerResponse {

    fun <T> getResponse(response: Response<T>): T {
        return response.body() ?: throw RemoteException(
            code = response.code(),
            msg = response.message()
        )
    }
}