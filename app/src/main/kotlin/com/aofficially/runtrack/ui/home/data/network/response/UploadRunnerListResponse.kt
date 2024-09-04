package com.aofficially.runtrack.ui.home.data.network.response
import com.google.gson.annotations.SerializedName


data class UploadRunnerListResponse(
    @SerializedName("runnerbib")
    val runnerBib: String,
    @SerializedName("runnerstatus")
    val runnerStatus: String
)