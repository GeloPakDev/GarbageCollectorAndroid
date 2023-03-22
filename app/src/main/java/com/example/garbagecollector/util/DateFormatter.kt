package com.example.garbagecollector.util

import android.os.Build
import androidx.annotation.RequiresApi
import java.time.LocalDate
import java.time.format.DateTimeFormatter

class DateFormatter {
    companion object {
        @RequiresApi(Build.VERSION_CODES.O)
        fun formatDate(date: LocalDate?): String {
            if (date == null) {
                throw NullPointerException()
            } else {
                val formatter = DateTimeFormatter.ofPattern("dd.MM.yyyy")
                return date.format(formatter)
            }
        }
    }
}