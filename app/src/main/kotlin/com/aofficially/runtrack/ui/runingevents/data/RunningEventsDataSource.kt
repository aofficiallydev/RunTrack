package com.aofficially.runtrack.ui.runingevents.data

import com.aofficially.runtrack.di.network.HandlerResponse
import com.aofficially.runtrack.di.network.RemoteException
import com.aofficially.runtrack.ui.runingevents.data.network.RunningEventsResponse
import com.aofficially.runtrack.ui.runingevents.data.network.RunningEventsService
import java.net.HttpURLConnection
import javax.inject.Inject

class RunningEventsDataSource @Inject constructor(
    private val api: RunningEventsService
) {

    suspend fun getRunningEvents(): List<RunningEventsResponse> {
        api.getRunningEvents().let { response ->
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