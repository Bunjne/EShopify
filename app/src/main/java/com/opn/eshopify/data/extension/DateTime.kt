package com.opn.eshopify.data.extension


import kotlin.time.Clock
import kotlin.time.ExperimentalTime
import kotlin.time.Instant
import kotlinx.datetime.LocalTime
import kotlinx.datetime.TimeZone
import kotlinx.datetime.toLocalDateTime

@OptIn(ExperimentalTime::class)
fun parseISOTime(timeString: String, timeZone: TimeZone = TimeZone.currentSystemDefault()): LocalTime {
    return try {
        Instant.parse(timeString).toLocalDateTime(timeZone).time
    } catch (_: Exception) {
        val currentDate = Clock.System.now().toLocalDateTime(timeZone).date
        val dateTime = "${currentDate}T$timeString"
        Instant.parse(dateTime).toLocalDateTime(timeZone).time
    }
}
