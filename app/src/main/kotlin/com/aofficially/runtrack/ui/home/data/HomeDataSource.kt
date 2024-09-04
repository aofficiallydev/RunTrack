package com.aofficially.runtrack.ui.home.data

import com.aofficially.runtrack.di.network.HandlerResponse
import com.aofficially.runtrack.di.network.RemoteException
import com.aofficially.runtrack.ui.home.data.network.HomeService
import com.aofficially.runtrack.ui.home.data.network.request.UploadRunnerListRequest
import com.aofficially.runtrack.ui.home.data.network.response.RunnerListResponse
import com.aofficially.runtrack.ui.home.data.network.response.UploadRunnerListResponse
import java.net.HttpURLConnection
import javax.inject.Inject

class HomeDataSource @Inject constructor(
    private val api: HomeService
) {

    suspend fun getRunnerList(raceId: String): List<RunnerListResponse> {
        api.getRunnerList(raceId).let { response ->
            return when (val code = response.code()) {
                HttpURLConnection.HTTP_OK -> HandlerResponse.getResponse(response)
                else -> throw RemoteException(
                    code = code,
                    msg = response.message().takeIf { it.isEmpty() }.let {
                        null
                    }
                )
            }
        }
    }

    suspend fun uploadRunnerList(request: UploadRunnerListRequest): UploadRunnerListResponse {
        api.uploadRunnerList(request).let { response ->
            return when (val code = response.code()) {
                HttpURLConnection.HTTP_OK -> HandlerResponse.getResponse(response)
                else -> throw RemoteException(
                    code = code,
                    msg = response.message().takeIf { it.isEmpty() }.let {
                        null
                    }
                )
            }
        }
    }
}