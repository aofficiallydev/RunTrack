package com.aofficially.runtrack.ui.home.data.network

import com.aofficially.runtrack.ui.home.data.network.request.UploadRunnerListRequest
import com.aofficially.runtrack.ui.home.data.network.response.RunnerListResponse
import com.aofficially.runtrack.ui.home.data.network.response.UploadRunnerListResponse
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.POST
import retrofit2.http.Path

interface HomeService {

    @GET("runner/{raceId}")
    suspend fun getRunnerList(@Path("raceId") username: String): Response<List<RunnerListResponse>>

    @POST("Upload")
    suspend fun uploadRunnerList(@Body request: UploadRunnerListRequest): Response<UploadRunnerListResponse>
}