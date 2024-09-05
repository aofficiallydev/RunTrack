package com.aofficially.runtrack.extensions

import java.text.SimpleDateFormat
import java.util.Calendar
import java.util.Date

 fun getCurrentDateTime(format: String = "HH:mm:ss"): String {
    val currentDateTime = Calendar.getInstance().time
    return currentDateTime.toString(format)
}
 private fun Date.toString(format: String): String {
    val formatter = SimpleDateFormat(format)
    return formatter.format(this)
}