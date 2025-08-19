package com.opn.eshopify.data.repository

import com.opn.eshopify.data.remote.api.ShoppingAPI
import com.opn.eshopify.data.remote.model.ProductDto
import com.opn.eshopify.data.remote.model.ProductResponse
import com.opn.eshopify.domain.DataError
import com.opn.eshopify.domain.Result
import com.opn.eshopify.util.TestAppDispatchers
import io.mockk.coEvery
import io.mockk.every
import io.mockk.mockk
import java.io.IOException
import java.net.SocketTimeoutException
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.runTest
import org.junit.Assert.assertEquals
import org.junit.Assert.assertFalse
import org.junit.Assert.assertTrue
import org.junit.Before
import org.junit.Test
import retrofit2.HttpException

@ExperimentalCoroutinesApi
class DefaultProductRepositoryTest {

    private lateinit var repository: DefaultProductRepository
    private val appDispatchers = TestAppDispatchers()

    private val mockApi = mockk<ShoppingAPI>()

    @Before
    fun setup() {
        repository = DefaultProductRepository(mockApi, appDispatchers)
    }

    @Test
    fun `getProducts should return success with paginated products when API call succeeds`() =
        runTest {
            // Given
            val productDtos = listOf(
                ProductDto(1, "Product 1", 10.0, "https://example.com/image1.jpg"),
                ProductDto(2, "Product 2", 20.0, "https://example.com/image2.jpg")
            )
            val paginationInfo = ProductResponse.PaginationInfo(
                currentPage = 1,
                totalPages = 3,
                totalCount = 50
            )
            val productResponse = ProductResponse(
                data = ProductResponse.ProductData(
                    productResult = ProductResponse.ProductResult(
                        products = productDtos,
                        pagination = paginationInfo
                    )
                )
            )
            coEvery { mockApi.getProducts(page = 1, limit = 10) } returns productResponse

            // When
            val result = repository.getProducts(page = 1, limit = 10)

            // Then
            assertTrue(result is Result.Success)
            val paginatedProducts = (result as Result.Success).data
            assertEquals(2, paginatedProducts.items.size)
            assertEquals(1, paginatedProducts.currentPage)
            assertEquals(3, paginatedProducts.totalPages)
            assertEquals(50, paginatedProducts.totalCount)
            assertTrue(paginatedProducts.hasNextPage)

            // Verify first product mapping
            assertEquals("1", paginatedProducts.items[0].id)
            assertEquals("Product 1", paginatedProducts.items[0].name)
            assertEquals(10.0, paginatedProducts.items[0].price, 0.001)
            assertEquals("https://example.com/image1.jpg", paginatedProducts.items[0].imageUrl)
        }

    @Test
    fun `getProducts should return empty list when API returns empty list`() = runTest {
        // Given
        val paginationInfo = ProductResponse.PaginationInfo(
            currentPage = 1,
            totalPages = 1,
            totalCount = 0
        )
        val productResponse = ProductResponse(
            data = ProductResponse.ProductData(
                productResult = ProductResponse.ProductResult(
                    products = emptyList(),
                    pagination = paginationInfo
                )
            )
        )
        coEvery { mockApi.getProducts(page = 1, limit = 10) } returns productResponse

        // When
        val result = repository.getProducts(page = 1, limit = 10)

        // Then
        assertTrue(result is Result.Success)
        val paginatedProducts = (result as Result.Success).data
        assertTrue(paginatedProducts.items.isEmpty())
        assertEquals(0, paginatedProducts.totalCount)
        assertFalse(paginatedProducts.hasNextPage)
    }


    @Test
    fun `getProducts should handle last page correctly`() = runTest {
        // Given
        val productDtos = listOf(
            ProductDto(1, "Product 1", 10.0, "https://example.com/image1.jpg")
        )
        val paginationInfo = ProductResponse.PaginationInfo(
            currentPage = 3,
            totalPages = 3,
            totalCount = 50
        )
        val productResponse = ProductResponse(
            data = ProductResponse.ProductData(
                productResult = ProductResponse.ProductResult(
                    products = productDtos,
                    pagination = paginationInfo
                )
            )
        )
        coEvery { mockApi.getProducts(page = 3, limit = 20) } returns productResponse

        // When
        val result = repository.getProducts(page = 3, limit = 20)

        // Then
        assertTrue(result is Result.Success)
        val paginatedProducts = (result as Result.Success).data
        assertEquals(3, paginatedProducts.currentPage)
        assertEquals(3, paginatedProducts.totalPages)
        assertFalse(paginatedProducts.hasNextPage)
    }

    @Test
    fun `getProducts should return HttpError when API throws HttpException`() = runTest {
        // Given
        val mockHttpException = mockk<HttpException> {
            every { code() } returns 404
        }
        coEvery { mockApi.getProducts(page = 1, limit = 10) } throws mockHttpException

        // When
        val result = repository.getProducts(page = 1, limit = 10)

        // Then
        assertTrue(result is Result.Error)
        assertEquals(DataError.Network.NotFound, (result as Result.Error).error)
    }


    @Test
    fun `getProducts should return Timeout error when API throws SocketTimeoutException`() =
        runTest {
            // Given
            coEvery { mockApi.getProducts(page = 1, limit = 10) } throws SocketTimeoutException("Timeout")

            // When
            val result = repository.getProducts(page = 1, limit = 10)

            // Then
            assertTrue(result is Result.Error)
            assertEquals(DataError.Network.Timeout, (result as Result.Error).error)
        }

    @Test
    fun `getProducts should return NoInternet error when API throws IOException`() = runTest {
        // Given
        coEvery { mockApi.getProducts(page = 1, limit = 10) } throws IOException("No connection")

        // When
        val result = repository.getProducts(page = 1, limit = 10)

        // Then
        assertTrue(result is Result.Error)
        assertEquals(DataError.Network.NoInternet, (result as Result.Error).error)
    }

    @Test
    fun `getProducts should return Unknown error when API throws unexpected exception`() = runTest {
        // Given
        coEvery { mockApi.getProducts(page = 1, limit = 10) } throws RuntimeException("Unexpected error")

        // When
        val result = repository.getProducts(page = 1, limit = 10)

        // Then
        assertTrue(result is Result.Error)
        assertEquals(DataError.Network.Unknown, (result as Result.Error).error)
    }
}
