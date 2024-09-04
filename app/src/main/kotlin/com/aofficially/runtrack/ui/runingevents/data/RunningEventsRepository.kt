package com.aofficially.runtrack.ui.runingevents.data

import com.aofficially.runtrack.ui.runingevents.domain.RunningEventsModel
import javax.inject.Inject

class RunningEventsRepository @Inject constructor(
    private val dataSource: RunningEventsDataSource
){

    suspend fun getRunningEvents(): List<RunningEventsModel> {
        return dataSource.getRunningEvents().map {
            it.mappingToModel()
        }
    }
}