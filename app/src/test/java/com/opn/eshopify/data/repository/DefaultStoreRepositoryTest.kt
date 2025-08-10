package com.opn.eshopify.data.repository

import com.opn.eshopify.data.extension.parseISOTime
import com.opn.eshopify.data.remote.api.ShoppingAPI
import com.opn.eshopify.data.remote.model.StoreDto
import com.opn.eshopify.domain.DataError
import com.opn.eshopify.domain.Result
import com.opn.eshopify.util.TestAppDispatchers
import io.mockk.coEvery
import io.mockk.every
import io.mockk.mockk
import io.mockk.mockkStatic
import io.mockk.unmockkAll
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.runTest
import kotlinx.datetime.LocalTime
import org.junit.After
import org.junit.Assert.assertEquals
import org.junit.Assert.assertTrue
import org.junit.Before
import org.junit.Test
import retrofit2.HttpException
import java.io.IOException
import java.net.SocketTimeoutException

@ExperimentalCoroutinesApi
class DefaultStoreRepositoryTest {

    private lateinit var repository: DefaultStoreRepository
    private val appDispatchers = TestAppDispatchers()

    private val mockApi = mockk<ShoppingAPI>()

    @Before
    fun setup() {
        mockkStatic(::parseISOTime)
        repository = DefaultStoreRepository(mockApi, appDispatchers)
    }

    @After
    fun tearDown() {
        unmockkAll()
    }

    @Test
    fun `getStoreDetail should return success with mapped store when API call succeeds`() =
        runTest {
            // Given
            val storeDto = StoreDto(
                name = "Test Store",
                rating = 4.5f,
                openingTime = "09:00:00.000Z",
                closingTime = "21:00:00.000Z"
            )

            // Mock the extension function
            every { parseISOTime(storeDto.openingTime) } returns LocalTime(9, 0)
            every { parseISOTime(storeDto.closingTime) } returns LocalTime(21, 0)

            coEvery { mockApi.getStoreDetail() } returns storeDto

            // When
            val result = repository.getStoreDetail()

            // Then
            assertTrue(result is Result.Success)
            val store = (result as Result.Success).data
            assertEquals("Test Store", store.name)
            assertEquals(4.5f, store.rating)
            assertEquals(LocalTime(9, 0), store.openingTime)
            assertEquals(LocalTime(21, 0), store.closingTime)
        }

    @Test
    fun `getStoreDetail should return HttpError when API throws HttpException`() = runTest {
        // Given
        val mockHttpException = mockk<HttpException> {
            every { code() } returns 500
        }
        coEvery { mockApi.getStoreDetail() } throws mockHttpException

        // When
        val result = repository.getStoreDetail()

        // Then
        assertTrue(result is Result.Error)
        assertEquals(DataError.Network.InternalServerError, (result as Result.Error).error)
    }

    @Test
    fun `getStoreDetail should return Timeout error when API throws SocketTimeoutException`() =
        runTest {
            // Given
            coEvery { mockApi.getStoreDetail() } throws SocketTimeoutException("Timeout")

            // When
            val result = repository.getStoreDetail()

            // Then
            assertTrue(result is Result.Error)
            assertEquals(DataError.Network.Timeout, (result as Result.Error).error)
        }

    @Test
    fun `getStoreDetail should return NoInternet error when API throws IOException`() = runTest {
        // Given
        coEvery { mockApi.getStoreDetail() } throws IOException("No connection")

        // When
        val result = repository.getStoreDetail()

        // Then
        assertTrue(result is Result.Error)
        assertEquals(DataError.Network.NoInternet, (result as Result.Error).error)
    }

    @Test
    fun `getStoreDetail should return Unknown error when API throws unexpected exception`() =
        runTest {
            // Given
            coEvery { mockApi.getStoreDetail() } throws RuntimeException("Unexpected error")

            // When
            val result = repository.getStoreDetail()

            // Then
            assertTrue(result is Result.Error)
            assertEquals(DataError.Network.Unknown, (result as Result.Error).error)
        }
}
