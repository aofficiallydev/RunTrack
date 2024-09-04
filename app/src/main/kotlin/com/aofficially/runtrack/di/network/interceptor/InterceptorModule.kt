package com.aofficially.runtrack.di.network.interceptor

import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton
import okhttp3.Interceptor

@Module
@InstallIn(SingletonComponent::class)
class InterceptorModule {

    @Provides
    @Singleton
    fun provideInterceptors(baseAuthenticationInterceptor: BaseInterceptor): Interceptor {
        return baseAuthenticationInterceptor
    }
}