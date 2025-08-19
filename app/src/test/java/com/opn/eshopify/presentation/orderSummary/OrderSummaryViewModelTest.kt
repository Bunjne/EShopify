package com.opn.eshopify.presentation.orderSummary

import com.opn.eshopify.domain.DataError
import com.opn.eshopify.domain.Result
import com.opn.eshopify.domain.model.Cart
import com.opn.eshopify.domain.model.Product
import com.opn.eshopify.util.MainCoroutineRule
import io.mockk.coEvery
import io.mockk.every
import io.mockk.mockk
import io.mockk.verify
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.test.StandardTestDispatcher
import kotlinx.coroutines.test.advanceUntilIdle
import kotlinx.coroutines.test.runTest
import org.junit.Assert.assertEquals
import org.junit.Assert.assertFalse
import org.junit.Assert.assertNull
import org.junit.Assert.assertTrue
import org.junit.Before
import org.junit.Rule
import org.junit.Test

@ExperimentalCoroutinesApi
class OrderSummaryViewModelTest {

    @get:Rule
    val mainCoroutineDispatcher = MainCoroutineRule(StandardTestDispatcher())

    private lateinit var viewModel: OrderSummaryViewModel
    private val useCases: OrderSummaryUseCases = mockk(relaxed = true)

    private val testProduct1 =
        Product(id = "1", name = "Test Product 1", price = 10.0, imageUrl = "url1")
    private val testProduct2 =
        Product(id = "2", name = "Test Product 2", price = 20.0, imageUrl = "url2")
    private val testAddress = "123 Test Street"
    private val testSelectedProducts = mapOf(testProduct1 to 1, testProduct2 to 2)

    private val testCart = Cart(
        products = testSelectedProducts,
        deliveryAddress = testAddress,
        totalPrice = testSelectedProducts.entries.sumOf { it.key.price * it.value },
        hasProducts = true
    )

    @Before
    fun setup() {
        viewModel = OrderSummaryViewModel(useCases)
        every { useCases.getCartDetail() } returns flow { emit(testCart) }
    }

    @Test
    fun `placeOrder should update state to loading and then success when order succeeds`() =
        runTest {
            // Given
            coEvery { useCases.placeOrder() } returns Result.Success(Unit)

            // When
            viewModel.placeOrder()
            advanceUntilIdle()

            // Then
            assertTrue(viewModel.uiState.value.isOrderPlaced)
            assertFalse(viewModel.uiState.value.isLoading)
            assertNull(viewModel.uiState.value.error)
        }

    @Test
    fun `placeOrder should update state to error when order fails`() = runTest {
        // Given
        coEvery { useCases.placeOrder() } returns Result.Error(DataError.Network.Unknown)

        // When
        viewModel.placeOrder()
        advanceUntilIdle()

        // Then
        assertFalse(viewModel.uiState.value.isOrderPlaced)
        assertFalse(viewModel.uiState.value.isLoading)
        assertTrue(viewModel.uiState.value.error != null)
    }

    @Test
    fun `placeOrder should set loading true immediately`() = runTest {
        // Given
        coEvery { useCases.placeOrder() } returns Result.Success(Unit)

        // When
        viewModel.placeOrder()

        // Then
        assertTrue(viewModel.uiState.value.isLoading)
    }

    @Test
    fun `updateDeliveryAddress should call the use case`() = runTest {
        // Given
        val newAddress = "456 New Street"

        // When
        viewModel.updateDeliveryAddress(newAddress)

        // Then
        verify(exactly = 1) { useCases.updateDeliveryAddress(newAddress) }
    }

    @Test
    fun `init should observe cart state and update UI state`() = runTest {
        // Then
        advanceUntilIdle()

        assertEquals(testSelectedProducts, viewModel.uiState.value.selectedProducts)
        assertEquals(testAddress, viewModel.uiState.value.deliveryAddress)
        assertEquals(50.0, viewModel.uiState.value.totalPrice, 0.01)
        assertTrue(viewModel.uiState.value.hasSelectedProducts)
    }

    @Test
    fun `observeCartState should apply subsequent emissions`() = runTest {
        // Given - create a flow that emits an updated cart
        val updatedCart = testCart.copy(
            deliveryAddress = "999 Updated Ave",
            totalPrice = 60.0
        )
        every { useCases.getCartDetail() } returns flow {
            emit(testCart)
            emit(updatedCart)
        }

        viewModel = OrderSummaryViewModel(useCases)

        // When
        advanceUntilIdle()

        // Then
        assertEquals(updatedCart.products, viewModel.uiState.value.selectedProducts)
        assertEquals(updatedCart.deliveryAddress, viewModel.uiState.value.deliveryAddress)
        assertEquals(updatedCart.totalPrice, viewModel.uiState.value.totalPrice, 0.01)
        assertTrue(viewModel.uiState.value.hasSelectedProducts)
    }
}
