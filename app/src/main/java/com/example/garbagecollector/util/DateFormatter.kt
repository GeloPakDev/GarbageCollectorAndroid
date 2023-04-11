package com.example.garbagecollector.util

import android.os.Build
import androidx.annotation.RequiresApi
import java.time.LocalDate
import java.time.format.DateTimeFormatter

class DateFormatter {
    companion object {
        @RequiresApi(Build.VERSION_CODES.O)
        fun convertDateFormat(inputDate: String): String {
            val formatterInput = DateTimeFormatter.ofPattern("yyyy-MM-dd")
            val formatterOutput = DateTimeFormatter.ofPattern("dd.MM.yyyy")
            val date = LocalDate.parse(inputDate, formatterInput)
            return date.format(formatterOutput)
        }
    }
}