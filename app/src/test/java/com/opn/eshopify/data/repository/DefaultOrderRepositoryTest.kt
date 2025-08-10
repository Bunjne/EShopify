package com.opn.eshopify.data.repository

import com.opn.eshopify.data.remote.api.ShoppingAPI
import com.opn.eshopify.data.remote.mapper.asDto
import com.opn.eshopify.data.remote.model.OrderDto
import com.opn.eshopify.domain.DataError
import com.opn.eshopify.domain.Result
import com.opn.eshopify.domain.model.Order
import com.opn.eshopify.domain.model.Product
import com.opn.eshopify.util.TestAppDispatchers
import io.mockk.coEvery
import io.mockk.every
import io.mockk.mockk
import io.mockk.mockkStatic
import io.mockk.slot
import io.mockk.unmockkAll
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.runTest
import org.junit.After
import org.junit.Assert.assertEquals
import org.junit.Assert.assertTrue
import org.junit.Before
import org.junit.Test
import retrofit2.HttpException
import java.io.IOException
import java.net.SocketTimeoutException

@ExperimentalCoroutinesApi
class DefaultOrderRepositoryTest {

    private lateinit var repository: DefaultOrderRepository

    private val appDispatchers = TestAppDispatchers()
    private val mockApi = mockk<ShoppingAPI>()

    private val testProducts = listOf(
        Product("1", "Product 1", 10.0, "https://example.com/image1.jpg"),
        Product("2", "Product 2", 20.0, "https://example.com/image2.jpg")
    )
    private val testOrder = Order(testProducts, "123 Test Street")

    @Before
    fun setup() {
        mockkStatic(Order::asDto)
        repository = DefaultOrderRepository(mockApi, appDispatchers)
    }

    @After
    fun tearDown() {
        unmockkAll()
    }

    @Test
    fun `placeOrder should return success when API call succeeds`() = runTest {
        // Given
        val orderDtoSlot = slot<OrderDto>()
        val mockOrderDto = mockk<OrderDto>()

        every { testOrder.asDto() } returns mockOrderDto
        coEvery { mockApi.makeOrder(capture(orderDtoSlot)) } returns Unit

        // When
        val result = repository.placeOrder(testOrder)

        // Then
        assertTrue(result is Result.Success)
        assertEquals(mockOrderDto, orderDtoSlot.captured)
    }

    @Test
    fun `placeOrder should return HttpError when API throws HttpException`() = runTest {
        // Given
        val mockOrderDto = mockk<OrderDto>()
        every { testOrder.asDto() } returns mockOrderDto

        val mockHttpException = mockk<HttpException> {
            every { code() } returns 400
        }
        coEvery { mockApi.makeOrder(any()) } throws mockHttpException

        // When
        val result = repository.placeOrder(testOrder)

        // Then
        assertTrue(result is Result.Error)
        assertEquals(DataError.Network.BadRequest, (result as Result.Error).error)
    }

    @Test
    fun `placeOrder should return Timeout error when API throws SocketTimeoutException`() =
        runTest {
            // Given
            val mockOrderDto = mockk<OrderDto>()
            every { testOrder.asDto() } returns mockOrderDto
            coEvery { mockApi.makeOrder(any()) } throws SocketTimeoutException("Timeout")

            // When
            val result = repository.placeOrder(testOrder)

            // Then
            assertTrue(result is Result.Error)
            assertEquals(DataError.Network.Timeout, (result as Result.Error).error)
        }

    @Test
    fun `placeOrder should return NoInternet error when API throws IOException`() = runTest {
        // Given
        val mockOrderDto = mockk<OrderDto>()
        every { testOrder.asDto() } returns mockOrderDto
        coEvery { mockApi.makeOrder(any()) } throws IOException("No connection")

        // When
        val result = repository.placeOrder(testOrder)

        // Then
        assertTrue(result is Result.Error)
        assertEquals(DataError.Network.NoInternet, (result as Result.Error).error)
    }

    @Test
    fun `placeOrder should return Unknown error when API throws unexpected exception`() = runTest {
        // Given
        val mockOrderDto = mockk<OrderDto>()
        every { testOrder.asDto() } returns mockOrderDto
        coEvery { mockApi.makeOrder(any()) } throws RuntimeException("Unexpected error")

        // When
        val result = repository.placeOrder(testOrder)

        // Then
        assertTrue(result is Result.Error)
        assertEquals(DataError.Network.Unknown, (result as Result.Error).error)
    }
}
