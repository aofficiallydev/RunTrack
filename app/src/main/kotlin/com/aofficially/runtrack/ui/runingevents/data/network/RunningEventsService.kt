package com.aofficially.runtrack.ui.runingevents.data.network

import retrofit2.Response
import retrofit2.http.GET

interface RunningEventsService {

    @GET("race")
    suspend fun getRunningEvents(): Response<List<RunningEventsResponse>>
}