package com.aofficially.runtrack.ui.login.data

import com.aofficially.runtrack.di.network.NetworkModule
import com.aofficially.runtrack.ui.login.data.network.LoginService
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import retrofit2.Retrofit

@Module(includes = [NetworkModule::class])
@InstallIn(SingletonComponent::class)
class LoginModule {

    @Provides
    fun provideLoginService(retrofit: Retrofit): LoginService {
        return retrofit.create(LoginService::class.java)
    }

    @Provides
    fun provideLoginRepository(
        dataSource: LoginDataSource
    ) = LoginRepository(dataSource)
}