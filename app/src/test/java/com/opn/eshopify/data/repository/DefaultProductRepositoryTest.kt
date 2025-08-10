package com.opn.eshopify.data.repository

import com.opn.eshopify.data.remote.api.ShoppingAPI
import com.opn.eshopify.data.remote.model.ProductDto
import com.opn.eshopify.domain.DataError
import com.opn.eshopify.domain.Result
import com.opn.eshopify.util.TestAppDispatchers
import io.mockk.coEvery
import io.mockk.every
import io.mockk.mockk
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.runTest
import org.junit.Assert.assertEquals
import org.junit.Assert.assertTrue
import org.junit.Before
import org.junit.Test
import retrofit2.HttpException
import java.io.IOException
import java.net.SocketTimeoutException

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
    fun `getProducts should return success with mapped products when API call succeeds`() =
        runTest {
            // Given
            val productDtos = listOf(
                ProductDto("Product 1", 10.0, "https://example.com/image1.jpg"),
                ProductDto("Product 2", 20.0, "https://example.com/image2.jpg")
            )
            coEvery { mockApi.getProducts() } returns productDtos

            // When
            val result = repository.getProducts()

            // Then
            assertTrue(result is Result.Success)
            val products = (result as Result.Success).data
            assertEquals(2, products.size)

            // Verify first product mapping
            assertEquals("0", products[0].id)
            assertEquals("Product 1", products[0].name)
            assertEquals(10.0, products[0].price, 0.001)
            assertEquals("https://example.com/image1.jpg", products[0].imageUrl)

            // Verify second product mapping
            assertEquals("1", products[1].id)
            assertEquals("Product 2", products[1].name)
            assertEquals(20.0, products[1].price, 0.001)
            assertEquals("https://example.com/image2.jpg", products[1].imageUrl)
        }

    @Test
    fun `getProducts should return empty list when API returns empty list`() = runTest {
        // Given
        coEvery { mockApi.getProducts() } returns emptyList()

        // When
        val result = repository.getProducts()

        // Then
        assertTrue(result is Result.Success)
        val products = (result as Result.Success).data
        assertTrue(products.isEmpty())
    }

    @Test
    fun `getProducts should return HttpError when API throws HttpException`() = runTest {
        // Given
        val mockHttpException = mockk<HttpException> {
            every { code() } returns 404
        }
        coEvery { mockApi.getProducts() } throws mockHttpException

        // When
        val result = repository.getProducts()

        // Then
        assertTrue(result is Result.Error)
        assertEquals(DataError.Network.NotFound, (result as Result.Error).error)
    }

    @Test
    fun `getProducts should return Timeout error when API throws SocketTimeoutException`() =
        runTest {
            // Given
            coEvery { mockApi.getProducts() } throws SocketTimeoutException("Timeout")

            // When
            val result = repository.getProducts()

            // Then
            assertTrue(result is Result.Error)
            assertEquals(DataError.Network.Timeout, (result as Result.Error).error)
        }

    @Test
    fun `getProducts should return NoInternet error when API throws IOException`() = runTest {
        // Given
        coEvery { mockApi.getProducts() } throws IOException("No connection")

        // When
        val result = repository.getProducts()

        // Then
        assertTrue(result is Result.Error)
        assertEquals(DataError.Network.NoInternet, (result as Result.Error).error)
    }

    @Test
    fun `getProducts should return Unknown error when API throws unexpected exception`() = runTest {
        // Given
        coEvery { mockApi.getProducts() } throws RuntimeException("Unexpected error")

        // When
        val result = repository.getProducts()

        // Then
        assertTrue(result is Result.Error)
        assertEquals(DataError.Network.Unknown, (result as Result.Error).error)
    }
}
