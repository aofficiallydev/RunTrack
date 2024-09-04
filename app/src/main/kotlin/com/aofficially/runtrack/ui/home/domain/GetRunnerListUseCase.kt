package com.aofficially.runtrack.ui.home.domain

import com.aofficially.runtrack.database.RunnerEntity
import com.aofficially.runtrack.ui.home.data.HomeRepository
import com.aofficially.runtrack.utils.preference.PreferenceDataSource
import com.aofficially.runtrack.utils.preference.PreferenceDataSourceImp.Companion.KEY_RACE_ID
import javax.inject.Inject
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow

class GetRunnerListUseCase @Inject constructor(
    private val repo: HomeRepository,
    private val pref: PreferenceDataSource
) {

    fun execute(): Flow<List<RunnerEntity>> {
        return flow {
            val raceId = pref.getString(KEY_RACE_ID)
            emit(repo.getRunnerList(raceId))
        }
    }
}