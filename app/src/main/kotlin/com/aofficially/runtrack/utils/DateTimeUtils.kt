package com.aofficially.runtrack.utils

import java.time.LocalDate
import java.time.format.DateTimeFormatter

object DateTimeUtils {

    fun convertDateFormat(dateString: String): String {
        val inputFormatter = DateTimeFormatter.ofPattern("dd/MM/yyyy")
        val date = LocalDate.parse(dateString, inputFormatter)
        val outputFormatter = DateTimeFormatter.ofPattern("yyyy/MM/dd")
        return date.format(outputFormatter)
    }

    fun sortFromDateTime(date: String, time: String): String {
        val date = convertDateFormat(date)
        return date + "T" + time
    }
}