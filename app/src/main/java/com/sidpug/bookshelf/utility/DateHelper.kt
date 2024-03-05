package com.sidpug.bookshelf.utility

import java.sql.Timestamp
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

val DateHelperInstance = DateHelper()

class DateHelper {

    fun getDateTime(s: Long): String? {
        return try {
            val timeStamp = Timestamp(s * 1000) // Convert seconds to milliseconds
            val sdf = SimpleDateFormat("yyyy", Locale.US)
            val netDate = Date(timeStamp.time)
            sdf.format(netDate)
        } catch (e: Exception) {
            e.toString()
        }
    }
}