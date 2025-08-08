package com.opn.eshopify.data.remote.mapper

import com.opn.eshopify.data.remote.model.StoreDto
import com.opn.eshopify.domain.model.Store
import kotlinx.datetime.Clock
import kotlinx.datetime.Instant
import kotlinx.datetime.LocalTime
import kotlinx.datetime.TimeZone
import kotlinx.datetime.toLocalDateTime

fun StoreDto.toDomain(): Store = Store(
    name = name,
    rating = rating,
    openingTime = parseISOTime(openingTime),
    closingTime = parseISOTime(closingTime)
)

private fun parseISOTime(timeString: String): LocalTime {
    return try {
        Instant.parse(timeString).toLocalDateTime(TimeZone.UTC).time
    } catch (_: Exception) {
        val currentDate = Clock.System.now().toLocalDateTime(TimeZone.UTC).date
        val dateTime = "${currentDate}T$timeString"
        Instant.parse(dateTime).toLocalDateTime(TimeZone.currentSystemDefault()).time
    }
}
