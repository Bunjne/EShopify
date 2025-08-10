package com.opn.eshopify.data.remote.mapper

import com.opn.eshopify.data.extension.parseISOTime
import com.opn.eshopify.data.remote.model.StoreDto
import io.mockk.every
import io.mockk.mockkStatic
import io.mockk.unmockkAll
import junit.framework.TestCase.assertEquals
import kotlinx.datetime.LocalTime
import org.junit.After
import org.junit.Before
import org.junit.Test

class StoreMapperTest {

    @Before
    fun setup() {
        mockkStatic(::parseISOTime)
    }

    @After
    fun tearDown() {
        unmockkAll()
    }

    @Test
    fun `asDomain should map StoreDto to Store correctly with ISO format time`() {
        // Given
        val storeDto = StoreDto(
            name = "Test Store",
            rating = 4.5f,
            openingTime = "2023-01-01T09:00:00Z",
            closingTime = "2023-01-01T21:00:00Z"
        )

        val openingTime = LocalTime(9, 0)
        val closingTime = LocalTime(21, 0)

        every { parseISOTime(storeDto.openingTime) } returns openingTime
        every { parseISOTime(storeDto.closingTime) } returns closingTime

        // When
        val store = storeDto.asDomain()

        // Then
        assertEquals(storeDto.name, store.name)
        assertEquals(storeDto.rating, store.rating)
        assertEquals(openingTime, store.openingTime)
        assertEquals(closingTime, store.closingTime)
    }

    @Test
    fun `asDomain should map StoreDto to Store correctly with time-only format`() {
        // Given
        val storeDto = StoreDto(
            name = "Test Store",
            rating = 4.5f,
            openingTime = "09:00:00.000Z",
            closingTime = "21:00:00.000Z"
        )

        val openingTime = LocalTime(9, 0)
        val closingTime = LocalTime(21, 0)

        every { parseISOTime(storeDto.openingTime) } returns openingTime
        every { parseISOTime(storeDto.closingTime) } returns closingTime

        // When
        val store = storeDto.asDomain()

        // Then
        assertEquals(storeDto.name, store.name)
        assertEquals(storeDto.rating, store.rating)
        assertEquals(openingTime, store.openingTime)
        assertEquals(closingTime, store.closingTime)
    }

    @Test
    fun `asDomain should handle edge case times`() {
        // Given
        val storeDto = StoreDto(
            name = "24/7 Store",
            rating = 5.0f,
            openingTime = "00:00:00.000Z",
            closingTime = "23:59:59.000Z"
        )

        val openingTime = LocalTime(0, 0, 0)
        val closingTime = LocalTime(23, 59, 59)

        every { parseISOTime(storeDto.openingTime) } returns openingTime
        every { parseISOTime(storeDto.closingTime) } returns closingTime

        // When
        val store = storeDto.asDomain()

        // Then
        assertEquals(storeDto.name, store.name)
        assertEquals(storeDto.rating, store.rating)
        assertEquals(openingTime, store.openingTime)
        assertEquals(closingTime, store.closingTime)
    }
}
