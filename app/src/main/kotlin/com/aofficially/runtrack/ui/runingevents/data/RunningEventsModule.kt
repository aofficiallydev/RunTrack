package com.aofficially.runtrack.ui.runingevents.data

import com.aofficially.runtrack.di.network.NetworkModule
import com.aofficially.runtrack.ui.runingevents.data.network.RunningEventsService
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import retrofit2.Retrofit

@Module(includes = [NetworkModule::class])
@InstallIn(SingletonComponent::class)
class RunningEventsModule {

    @Provides
    fun provideRunningEventsService(retrofit: Retrofit): RunningEventsService {
        return retrofit.create(RunningEventsService::class.java)
    }

    @Provides
    fun provideRunningEventsRepository(
        dataSource: RunningEventsDataSource
    ) = RunningEventsRepository(dataSource)
}