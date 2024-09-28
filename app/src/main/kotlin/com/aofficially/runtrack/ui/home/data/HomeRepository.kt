package com.aofficially.runtrack.ui.home.data

import com.aofficially.runtrack.database.RunnerEntity
import com.aofficially.runtrack.ui.home.data.network.request.UploadRunnerListRequest
import com.aofficially.runtrack.ui.home.data.network.request.UploadRunnerRequest
import com.aofficially.runtrack.ui.home.data.network.response.UploadRunnerListResponse
import com.aofficially.runtrack.utils.preference.PreferenceDataSource
import com.aofficially.runtrack.utils.preference.PreferenceDataSourceImp.Companion.KEY_STATION_ID
import javax.inject.Inject

class HomeRepository @Inject constructor(
    private val dataSource: HomeDataSource,
    private val pref: PreferenceDataSource
) {

    suspend fun getRunnerList(raceId: String): List<RunnerEntity> {
        return dataSource.getRunnerList(raceId).map {
            it.mappingToModel()
        }
    }

    suspend fun uploadRunnerList(runner: List<RunnerEntity>): UploadRunnerListResponse {
        val runnerList = runner.map { it.mappingToUploadRunnerListRequest() }
        val request = UploadRunnerListRequest(runnerList)
        return dataSource.uploadRunnerList(request)
    }

    private fun RunnerEntity.mappingToUploadRunnerListRequest() = UploadRunnerRequest(
        rDID = this.rDID,
        raceId = this.raceId,
        runBid = this.runBid,
        runId = this.runId,
        staId = pref.getString(KEY_STATION_ID),
        traDatein = this.dateIn,
        traDateout = this.dateOut,
        traDistance = this.runDistance,
        traStatus = this.runStatus,
        traTimein = this.timeIn,
        traTimeout = this.timeOut
    )
}