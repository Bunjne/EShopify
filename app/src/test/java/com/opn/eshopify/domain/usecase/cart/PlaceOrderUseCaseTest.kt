package com.opn.eshopify.domain.usecase.cart

import com.opn.eshopify.domain.DataError
import com.opn.eshopify.domain.Result
import com.opn.eshopify.domain.model.Order
import com.opn.eshopify.domain.model.Product
import com.opn.eshopify.domain.repository.CartRepository
import com.opn.eshopify.domain.repository.OrderRepository
import com.opn.eshopify.domain.usecase.cart.PlaceOrderUseCase
import io.mockk.coEvery
import io.mockk.coVerify
import io.mockk.every
import io.mockk.mockk
import io.mockk.verify
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.test.runTest
import org.junit.Assert.assertEquals
import org.junit.Assert.assertTrue
import org.junit.Before
import org.junit.Test

@ExperimentalCoroutinesApi
class PlaceOrderUseCaseTest {

    private lateinit var useCase: PlaceOrderUseCase
    private lateinit var orderRepository: OrderRepository
    private lateinit var cartRepository: CartRepository

    private val testProducts = listOf(
        Product(id = "1", name = "Test Product 1", price = 100.0, imageUrl = "url1"),
        Product(id = "2", name = "Test Product 2", price = 200.0, imageUrl = "url2")
    )

    private val testAddress = "123 Test Street"
    private val selectedProductsFlow = MutableStateFlow(testProducts.associateWith { 1 })
    private val deliveryAddressFlow = MutableStateFlow(testAddress)

    @Before
    fun setup() {
        orderRepository = mockk()
        cartRepository = mockk {
            every { selectedProducts } returns selectedProductsFlow
            every { deliveryAddress } returns deliveryAddressFlow
            every { reset() } returns Unit
        }

        useCase = PlaceOrderUseCase(orderRepository, cartRepository)
    }

    @Test
    fun `invoke should return success when repository succeeds`() = runTest {
        // Given
        coEvery { orderRepository.placeOrder(any()) } returns Result.Success(Unit)

        // When
        val result = useCase()

        // Then
        assertTrue(result is Result.Success)
        coVerify(exactly = 1) {
            orderRepository.placeOrder(
                Order(
                    products = testProducts,
                    deliveryAddress = testAddress
                )
            )
        }
        verify(exactly = 1) { cartRepository.reset() }
    }

    @Test
    fun `invoke should return error when repository fails`() = runTest {
        // Given
        val expectedError = DataError.Network.BadRequest
        coEvery { orderRepository.placeOrder(any()) } returns Result.Error(expectedError)

        // When
        val result = useCase()

        // Then
        assertTrue(result is Result.Error)
        assertEquals(expectedError, (result as Result.Error).error)
        coVerify(exactly = 1) {
            orderRepository.placeOrder(
                Order(
                    products = testProducts,
                    deliveryAddress = testAddress
                )
            )
        }
        verify(exactly = 0) { cartRepository.reset() }
    }

    @Test
    fun `invoke with empty cart and address should still call repository and reset on success`() =
        runTest {
            // Given
            selectedProductsFlow.value = emptyMap()
            deliveryAddressFlow.value = ""
            coEvery { orderRepository.placeOrder(any()) } returns Result.Success(Unit)

            // When
            val result = useCase()

            // Then
            assertTrue(result is Result.Success)
            coVerify(exactly = 1) {
                orderRepository.placeOrder(
                    Order(
                        products = emptyList(),
                        deliveryAddress = ""
                    )
                )
            }
            verify(exactly = 1) { cartRepository.reset() }
        }

    @Test
    fun `invoke with empty cart and address should not reset on repository error`() = runTest {
        // Given
        selectedProductsFlow.value = emptyMap()
        deliveryAddressFlow.value = ""
        val expectedError = DataError.Network.BadRequest
        coEvery { orderRepository.placeOrder(any()) } returns Result.Error(expectedError)

        // When
        val result = useCase()

        // Then
        assertTrue(result is Result.Error)
        assertEquals(expectedError, (result as Result.Error).error)
        coVerify(exactly = 1) {
            orderRepository.placeOrder(
                Order(
                    products = emptyList(),
                    deliveryAddress = ""
                )
            )
        }
        verify(exactly = 0) { cartRepository.reset() }
    }
}
