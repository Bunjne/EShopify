package com.opn.eshopify.domain.usecase.store

import com.opn.eshopify.domain.DataError
import com.opn.eshopify.domain.Result
import com.opn.eshopify.domain.model.PaginatedResult
import com.opn.eshopify.domain.model.Product
import com.opn.eshopify.domain.repository.ProductRepository
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

    private val paginatedProducts = PaginatedResult<Product>(
        items = testProducts,
        currentPage = 1,
        totalPages = 3,
        totalCount = 50,
        hasNextPage = true
    )

    private val page = 1
    private val limit = 10

    @Before
    fun setup() {
        useCase = GetProductsUseCase(mockRepository)
    }

    @Test
    fun `invoke should return success with products when repository succeeds`() = runTest {
        // Given
        coEvery { mockRepository.getProducts(page, limit) } returns Result.Success(paginatedProducts)

        // When
        val result = useCase(page, limit)

        // Then
        assertTrue(result is Result.Success)
        assertEquals(testProducts, (result as Result.Success).data.items)
    }

    @Test
    fun `invoke should return error when repository fails`() = runTest {
        // Given
        val expectedError = DataError.Network.NoInternet
        coEvery { mockRepository.getProducts(page, limit) } returns Result.Error(expectedError)

        // When
        val result = useCase(page, limit)

        // Then
        assertTrue(result is Result.Error)
        assertEquals(expectedError, (result as Result.Error).error)
    }
}
