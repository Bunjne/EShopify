package com.opn.eshopify.presentation.orderSummary

import com.opn.eshopify.domain.DataError
import com.opn.eshopify.domain.Result
import com.opn.eshopify.domain.model.Cart
import com.opn.eshopify.domain.model.Product
import com.opn.eshopify.domain.usecase.cart.GetCartDetailUseCase
import com.opn.eshopify.domain.usecase.cart.PlaceOrderUseCase
import com.opn.eshopify.domain.usecase.cart.UpdateDeliveryAddressUseCase
import com.opn.eshopify.presentation.util.TextValue
import com.opn.eshopify.util.MainCoroutineRule
import io.mockk.coEvery
import io.mockk.every
import io.mockk.mockk
import io.mockk.verify
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.test.StandardTestDispatcher
import kotlinx.coroutines.test.advanceUntilIdle
import kotlinx.coroutines.test.resetMain
import kotlinx.coroutines.test.runTest
import kotlinx.coroutines.test.setMain
import org.junit.After
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
    private lateinit var placeOrderUseCase: PlaceOrderUseCase
    private lateinit var getCartDetailUseCase: GetCartDetailUseCase
    private lateinit var updateDeliveryAddressUseCase: UpdateDeliveryAddressUseCase
    private lateinit var useCases: OrderSummaryUseCases

    private val testProduct1 =
        Product(id = "1", name = "Test Product 1", price = 10.0, imageUrl = "url1")
    private val testProduct2 =
        Product(id = "2", name = "Test Product 2", price = 20.0, imageUrl = "url2")
    private val testAddress = "123 Test Street"
    private val testSelectedProducts = mapOf(testProduct1 to 1, testProduct2 to 2)

    private val testCart = Cart(
        products = testSelectedProducts,
        deliveryAddress = testAddress,
        totalPrice = 50.0, // 10.0 + (20.0 * 2)
        hasProducts = true
    )

    @Before
    fun setup() {
        placeOrderUseCase = mockk()
        updateDeliveryAddressUseCase = mockk(relaxed = true)
        getCartDetailUseCase = mockk {
            every { this@mockk.invoke() } returns flow { emit(testCart) }
        }

        useCases = OrderSummaryUseCases(
            placeOrder = placeOrderUseCase,
            updateDeliveryAddress = updateDeliveryAddressUseCase,
            getCartDetail = getCartDetailUseCase
        )

        viewModel = OrderSummaryViewModel(useCases)
    }

    @Test
    fun `placeOrder should update state to loading and then success when order succeeds`() =
        runTest {
            // Given
            coEvery { placeOrderUseCase() } returns Result.Success(Unit)

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
        coEvery { placeOrderUseCase() } returns Result.Error(DataError.Network.Unknown)

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
        coEvery { placeOrderUseCase() } returns Result.Success(Unit)

        // When
        viewModel.placeOrder()

        // Then (before advancing coroutines)
        assertTrue(viewModel.uiState.value.isLoading)
    }

    @Test
    fun `updateDeliveryAddress should call the use case`() = runTest {
        // Given
        val newAddress = "456 New Street"

        // When
        viewModel.updateDeliveryAddress(newAddress)

        // Then
        verify(exactly = 1) { updateDeliveryAddressUseCase(newAddress) }
    }

    @Test
    fun `init should observe cart state and update UI state`() = runTest {
        // Then - cart state has been observing in ViewModel's init
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
        getCartDetailUseCase = mockk {
            every { this@mockk.invoke() } returns flow {
                emit(testCart)
                emit(updatedCart)
            }
        }
        useCases = OrderSummaryUseCases(
            placeOrder = placeOrderUseCase,
            updateDeliveryAddress = updateDeliveryAddressUseCase,
            getCartDetail = getCartDetailUseCase
        )
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
