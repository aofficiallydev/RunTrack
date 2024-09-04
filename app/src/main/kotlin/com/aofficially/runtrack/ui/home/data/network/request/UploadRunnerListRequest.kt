package com.aofficially.runtrack.ui.home.data.network.request

import com.google.gson.annotations.SerializedName


data class UploadRunnerListRequest(
    @SerializedName("Gcntranstion")
    val gcntranstion: List<UploadRunnerRequest>
)

data class UploadRunnerRequest(
    @SerializedName("rD_ID")
    val rDID: String,
    @SerializedName("race_Id")
    val raceId: String,
    @SerializedName("run_Bid")
    val runBid: String,
    @SerializedName("run_Id")
    val runId: String,
    @SerializedName("sta_Id")
    val staId: String,
    @SerializedName("tra_Datein")
    val traDatein: String,
    @SerializedName("tra_Dateout")
    val traDateout: String,
    @SerializedName("tra_Distance")
    val traDistance: String,
    @SerializedName("tra_Status")
    val traStatus: String,
    @SerializedName("tra_Timein")
    val traTimein: String,
    @SerializedName("tra_Timeout")
    val traTimeout: String
)