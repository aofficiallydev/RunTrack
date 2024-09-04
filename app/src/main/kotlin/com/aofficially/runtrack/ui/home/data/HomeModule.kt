package com.aofficially.runtrack.ui.home.data

import com.aofficially.runtrack.di.network.NetworkModule
import com.aofficially.runtrack.ui.home.data.network.HomeService
import com.aofficially.runtrack.utils.preference.PreferenceDataSource
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import retrofit2.Retrofit

@Module(includes = [NetworkModule::class])
@InstallIn(SingletonComponent::class)
class HomeModule {

    @Provides
    fun provideHomeService(retrofit: Retrofit): HomeService {
        return retrofit.create(HomeService::class.java)
    }

    @Provides
    fun provideHomeRepository(
        dataSource: HomeDataSource,
        pref: PreferenceDataSource
    ) = HomeRepository(dataSource, pref)
}