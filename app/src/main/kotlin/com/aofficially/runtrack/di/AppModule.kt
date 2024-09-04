package com.aofficially.runtrack.di

import com.aofficially.runtrack.utils.dialog.DialogUtility
import com.aofficially.runtrack.utils.dialog.DialogView
import com.aofficially.runtrack.utils.preference.PreferenceDataSource
import com.aofficially.runtrack.utils.preference.PreferenceDataSourceImp
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.Dispatchers

@Module
@InstallIn(SingletonComponent::class)
class AppModule {

    @Provides
    fun provideDefaultDispatcher(): CoroutineDispatcher = Dispatchers.Default

    @Provides
    fun provideDialogUtility(): DialogUtility = DialogView

    @Provides
    @Singleton
    fun provideSharedPreference(
        sharePreferenceUtilityImp: PreferenceDataSourceImp
    ): PreferenceDataSource =
        sharePreferenceUtilityImp
}