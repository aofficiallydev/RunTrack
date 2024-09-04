package com.aofficially.runtrack.ui.login.data.network

import com.google.gson.annotations.SerializedName

data class LoginResponse(
    @SerializedName("stationid")
    val stationId: String,
    @SerializedName("stationname")
    val stationName: String
)