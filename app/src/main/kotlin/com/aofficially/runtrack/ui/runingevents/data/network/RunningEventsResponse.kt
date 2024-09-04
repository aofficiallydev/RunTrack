package com.aofficially.runtrack.ui.runingevents.data.network
import com.aofficially.runtrack.ui.runingevents.domain.RunningEventsModel
import com.google.gson.annotations.SerializedName

data class RunningEventsResponse(
    @SerializedName("race_Detail")
    val raceDetail: String,
    @SerializedName("race_Id")
    val raceId: String,
    @SerializedName("race_Image")
    val raceImage: String,
    @SerializedName("race_Name")
    val raceName: String
) {
    fun mappingToModel() = RunningEventsModel(
        raceDetail = raceDetail,
        raceId = raceId,
        raceImage = raceImage.replace("\\", "/"),
        raceName = raceName
    )
}