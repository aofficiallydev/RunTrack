package com.aofficially.runtrack.ui.runingevents.domain

import com.aofficially.runtrack.ui.runingevents.data.RunningEventsRepository
import javax.inject.Inject
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow

class GetRunningEventsUseCase @Inject constructor(
    private val repo: RunningEventsRepository
) {

    fun execute(): Flow<List<RunningEventsModel>> {
        return flow {
            emit(repo.getRunningEvents())
        }
    }
}