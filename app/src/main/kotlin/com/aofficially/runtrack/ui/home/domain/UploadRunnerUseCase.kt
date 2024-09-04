package com.aofficially.runtrack.ui.home.domain

import com.aofficially.runtrack.database.RunnerEntity
import com.aofficially.runtrack.ui.home.data.HomeRepository
import com.aofficially.runtrack.ui.home.data.network.response.UploadRunnerListResponse
import javax.inject.Inject
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow

class UploadRunnerUseCase @Inject constructor(
    private val repo: HomeRepository
) {

    fun execute(runner: List<RunnerEntity>): Flow<UploadRunnerListResponse> {
        return flow {
            emit(repo.uploadRunnerList(runner))
        }
    }
}