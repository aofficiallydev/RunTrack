package com.aofficially.runtrack.ui.runingevents.domain

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

@Parcelize
data class RunningEventsModel(
    val raceDetail: String = "",
    val raceId: String = "",
    val raceImage: String = "",
    val raceName: String = ""
) : Parcelable