package com.aofficially.runtrack.database

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity
data class RunnerEntity(
    @PrimaryKey(autoGenerate = true)
    var uid: Int = 0,
    @ColumnInfo(name = "rD_ID")
    var rDID: String,
    @ColumnInfo(name = "race_Id")
    var raceId: String,
    @ColumnInfo(name = "run_Age")
    var runAge: String,
    @ColumnInfo(name = "run_Agegroup")
    var runAgegroup: String,
    @ColumnInfo(name = "run_Bid")
    var runBid: String,
    @ColumnInfo(name = "run_Birthday")
    var runBirthday: String,
    @ColumnInfo(name = "run_Distance")
    var runDistance: String,
    @ColumnInfo(name = "run_Firstname")
    var runFirstname: String,
    @ColumnInfo(name = "run_Id")
    var runId: String,
    @ColumnInfo(name = "run_Image")
    var runImage: String,
    @ColumnInfo(name = "run_Lastname")
    var runLastname: String,
    @ColumnInfo(name = "run_National")
    var runNational: String,
    @ColumnInfo(name = "run_Register")
    var runRegister: String,
    @ColumnInfo(name = "run_Sex")
    var runSex: String,
    @ColumnInfo(name = "run_Special")
    var runSpecial: String,
    @ColumnInfo(name = "run_Status")
    var runStatus: String,
    @ColumnInfo(name = "isUploaded")
    var isUpLoaded: Boolean = false,
    @ColumnInfo(name = "hasUpdate")
    var hasUpdate: Boolean = false,
    @ColumnInfo(name = "dateIn")
    var dateIn: String = "",
    @ColumnInfo(name = "timeInt")
    var timeInt: String = "",
    @ColumnInfo(name = "dateOut")
    var dateOut: String = "",
    @ColumnInfo(name = "timeOut")
    var timeOut: String = "",
    @ColumnInfo(name = "timeStamp")
    var timeStamp: Long = 0,
)
