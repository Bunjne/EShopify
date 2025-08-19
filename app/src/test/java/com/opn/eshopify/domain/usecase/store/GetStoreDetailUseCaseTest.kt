package com.opn.eshopify.domain.usecase.store

import com.opn.eshopify.domain.DataError
import com.opn.eshopify.domain.Result
import com.opn.eshopify.domain.model.Store
import com.opn.eshopify.domain.repository.StoreRepository
import io.mockk.coEvery
import io.mockk.mockk
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.runTest
import kotlinx.datetime.LocalTime
import org.junit.Assert.assertEquals
import org.junit.Assert.assertTrue
import org.junit.Before
import org.junit.Test

@ExperimentalCoroutinesApi
class GetStoreDetailUseCaseTest {

    private lateinit var useCase: GetStoreDetailUseCase

    private val mockRepository = mockk<StoreRepository>()

    private val testStore = Store(
        name = "Test Store",
        rating = 4.5f,
        openingTime = LocalTime(9, 0),
        closingTime = LocalTime(21, 0)
    )

    @Before
    fun setup() {
        useCase = GetStoreDetailUseCase(mockRepository)
    }

    @Test
    fun `invoke should return success with store when repository succeeds`() = runTest {
        // Given
        coEvery { mockRepository.getStoreDetail() } returns Result.Success(testStore)

        // When
        val result = useCase()

        // Then
        assertTrue(result is Result.Success)
        assertEquals(testStore, (result as Result.Success).data)
    }

    @Test
    fun `invoke should return error when repository fails`() = runTest {
        // Given
        val expectedError = DataError.Network.ServiceUnavailable
        coEvery { mockRepository.getStoreDetail() } returns Result.Error(expectedError)

        // When
        val result = useCase()

        // Then
        assertTrue(result is Result.Error)
        assertEquals(expectedError, (result as Result.Error).error)
    }
}
