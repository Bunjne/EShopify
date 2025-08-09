package com.opn.eshopify.data.remote.mapper

import com.opn.eshopify.data.remote.model.StoreDto
import com.opn.eshopify.domain.model.Store
import kotlin.time.Clock
import kotlin.time.ExperimentalTime
import kotlin.time.Instant
import kotlinx.datetime.LocalTime
import kotlinx.datetime.TimeZone
import kotlinx.datetime.toLocalDateTime

fun StoreDto.asDomain(): Store = Store(
    name = name,
    rating = rating,
    openingTime = parseISOTime(openingTime),
    closingTime = parseISOTime(closingTime)
)

@OptIn(ExperimentalTime::class)
private fun parseISOTime(timeString: String): LocalTime {
    return try {
        Instant.parse(timeString).toLocalDateTime(TimeZone.UTC).time
    } catch (_: Exception) {
        val currentDate = Clock.System.now().toLocalDateTime(TimeZone.UTC).date
        val dateTime = "${currentDate}T$timeString"
        Instant.parse(dateTime).toLocalDateTime(TimeZone.currentSystemDefault()).time
    }
}
