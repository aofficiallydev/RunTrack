package com.aofficially.runtrack.ui.login.domain

import com.aofficially.runtrack.ui.login.data.LoginRepository
import com.aofficially.runtrack.ui.login.data.network.LoginResponse
import com.aofficially.runtrack.utils.preference.PreferenceDataSource
import com.aofficially.runtrack.utils.preference.PreferenceDataSourceImp.Companion.KEY_RACE_ID
import com.aofficially.runtrack.utils.preference.PreferenceDataSourceImp.Companion.KEY_STATION_ID
import com.aofficially.runtrack.utils.preference.PreferenceDataSourceImp.Companion.KEY_STATION_NAME
import javax.inject.Inject
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow

class LoginUseCase @Inject constructor(
    private val repo: LoginRepository,
    private val pref: PreferenceDataSource
) {

    fun execute(raceId: String, username: String, password: String): Flow<LoginResponse> {
        return flow {
            repo.login(username, password).also {
                pref.putString(KEY_RACE_ID, raceId)
                pref.putString(KEY_STATION_ID, it.stationId)
                pref.putString(KEY_STATION_NAME, it.stationName)
                emit(it)
            }
        }
    }
}