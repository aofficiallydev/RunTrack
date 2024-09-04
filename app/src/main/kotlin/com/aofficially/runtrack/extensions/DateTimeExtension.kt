package com.aofficially.runtrack.extensions

import java.text.SimpleDateFormat
import java.util.Calendar
import java.util.Date

 fun getCurrentDate(): String {
    val currentDateTime = Calendar.getInstance().time
    val yearInString = (currentDateTime.toString("yyyy").toInt() + 543)
    val monthInString = currentDateTime.toString("dd/MM")
    return "${monthInString}/${yearInString}"
}
 fun getCurrentTime(format: String = "HH:mm:ss"): String {
    val currentDateTime = Calendar.getInstance().time
    return currentDateTime.toString(format)
}
 private fun Date.toString(format: String): String {
    val formatter = SimpleDateFormat(format)
    return formatter.format(this)
}