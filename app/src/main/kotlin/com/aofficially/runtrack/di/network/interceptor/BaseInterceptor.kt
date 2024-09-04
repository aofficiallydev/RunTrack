package com.aofficially.runtrack.di.network.interceptor

import javax.inject.Inject
import okhttp3.Interceptor
import okhttp3.Response

class BaseInterceptor @Inject constructor() : Interceptor {

    override fun intercept(chain: Interceptor.Chain): Response {
        val request = chain.request().newBuilder().build()

        return chain.proceed(request)
    }
}