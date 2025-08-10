package com.opn.eshopify.data.extension

import kotlinx.datetime.LocalTime
import kotlinx.datetime.TimeZone
import org.junit.Assert.assertEquals
import org.junit.Test

class DateTimeTest {

    private val fixedTimeZone = TimeZone.UTC

    @Test
    fun `parseISOTime should parse full ISO datetime string correctly`() {
        // Given
        val isoTimeString = "2023-01-01T14:30:45Z"
        
        // When
        val result = parseISOTime(isoTimeString, fixedTimeZone)
        
        // Then
        assertEquals(14, result.hour)
        assertEquals(30, result.minute)
        assertEquals(45, result.second)
    }

    @Test
    fun `parseISOTime should handle time-only format by appending current date`() {
        // Given
        val timeOnlyString = "14:30:45Z"
        
        // When
        val result = parseISOTime(timeOnlyString, fixedTimeZone)
        
        // Then
        assertEquals(14, result.hour)
        assertEquals(30, result.minute)
        assertEquals(45, result.second)
    }

    @Test
    fun `parseISOTime should handle edge case times`() {
        // Given
        val midnightString = "00:00:00Z"
        val endOfDayString = "23:59:59Z"
        
        // When & Then
        val midnightResult = parseISOTime(midnightString, fixedTimeZone)
        assertEquals(0, midnightResult.hour)
        assertEquals(0, midnightResult.minute)
        assertEquals(0, midnightResult.second)
        
        val endOfDayResult = parseISOTime(endOfDayString, fixedTimeZone)
        assertEquals(23, endOfDayResult.hour)
        assertEquals(59, endOfDayResult.minute)
        assertEquals(59, endOfDayResult.second)
    }
}
