package com.example.myapplication.utils

import java.time.LocalDate
import java.time.format.DateTimeFormatter

class DateTimeUtils {

    companion object {
        private val dateTimeFormatter1 = DateTimeFormatter.ofPattern("dd-MM-yyyy")
        private val dateTimeFormatter2 = DateTimeFormatter.ofPattern("MMM-dd")
        fun getToday(): LocalDate = LocalDate.now()

        fun convertLocalDateToString(localDate: LocalDate): String {
            return localDate.format(dateTimeFormatter1)
        }

        fun convertLocalDateToSeatDateString(localDate: LocalDate): String {
            return localDate.format(dateTimeFormatter2)
        }
    }
}

