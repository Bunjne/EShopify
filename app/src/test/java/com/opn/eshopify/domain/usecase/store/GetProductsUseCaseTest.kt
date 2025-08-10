package com.opn.eshopify.domain.usecase

import com.opn.eshopify.domain.DataError
import com.opn.eshopify.domain.Result
import com.opn.eshopify.domain.model.Product
import com.opn.eshopify.domain.repository.ProductRepository
import com.opn.eshopify.domain.usecase.store.GetProductsUseCase
import io.mockk.coEvery
import io.mockk.mockk
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.runTest
import org.junit.Assert.assertEquals
import org.junit.Assert.assertTrue
import org.junit.Before
import org.junit.Test

@ExperimentalCoroutinesApi
class GetProductsUseCaseTest {

    private lateinit var useCase: GetProductsUseCase

    private val mockRepository = mockk<ProductRepository>()

    private val testProducts = listOf(
        Product(id = "1", name = "Test Product 1", price = 100.0, imageUrl = "url1"),
        Product(id = "2", name = "Test Product 2", price = 200.0, imageUrl = "url2")
    )

    @Before
    fun setup() {
        useCase = GetProductsUseCase(mockRepository)
    }

    @Test
    fun `invoke should return success with products when repository succeeds`() = runTest {
        // Given
        coEvery { mockRepository.getProducts() } returns Result.Success(testProducts)

        // When
        val result = useCase()

        // Then
        assertTrue(result is Result.Success)
        assertEquals(testProducts, (result as Result.Success).data)
    }

    @Test
    fun `invoke should return error when repository fails`() = runTest {
        // Given
        val expectedError = DataError.Network.NoInternet
        coEvery { mockRepository.getProducts() } returns Result.Error(expectedError)

        // When
        val result = useCase()

        // Then
        assertTrue(result is Result.Error)
        assertEquals(expectedError, (result as Result.Error).error)
    }
}
