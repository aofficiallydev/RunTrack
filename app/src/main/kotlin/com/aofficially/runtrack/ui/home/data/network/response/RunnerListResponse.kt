package com.aofficially.runtrack.ui.home.data.network.response
import com.aofficially.runtrack.database.RunnerEntity
import com.google.gson.annotations.SerializedName


data class RunnerListResponse(
    @SerializedName("rD_ID")
    val rDID: String,
    @SerializedName("race_ID")
    val raceID: String,
    @SerializedName("run_Age")
    val runAge: String,
    @SerializedName("run_Agegroup")
    val runAgegroup: String,
    @SerializedName("run_Bid")
    val runBid: String,
    @SerializedName("run_Birthday")
    val runBirthday: String,
    @SerializedName("run_Distance")
    val runDistance: String,
    @SerializedName("run_Firstname")
    val runFirstname: String,
    @SerializedName("run_Id")
    val runId: String,
    @SerializedName("run_Image")
    val runImage: String,
    @SerializedName("run_Lastname")
    val runLastname: String,
    @SerializedName("run_National")
    val runNational: String,
    @SerializedName("run_Register")
    val runRegister: String,
    @SerializedName("run_Sex")
    val runSex: String,
    @SerializedName("run_Special")
    val runSpecial: String,
    @SerializedName("run_Status")
    val runStatus: String
) {
    fun mappingToModel() = RunnerEntity(
        rDID = rDID,
        raceId = raceID,
        runAge = runAge,
        runAgegroup = runAgegroup,
        runBid = runBid,
        runBirthday = runBirthday,
        runDistance = runDistance,
        runFirstname = runFirstname,
        runId = runId,
        runImage = runImage,
        runLastname = runLastname,
        runNational = runNational,
        runRegister = runRegister,
        runSex = runSex,
        runSpecial = runSpecial,
        runStatus = runStatus,
    )
}