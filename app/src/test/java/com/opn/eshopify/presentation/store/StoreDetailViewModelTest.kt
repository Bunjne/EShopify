package com.opn.eshopify.presentation.store

import com.opn.eshopify.domain.DataError
import com.opn.eshopify.domain.Result
import com.opn.eshopify.domain.model.Cart
import com.opn.eshopify.domain.model.Product
import com.opn.eshopify.domain.model.Store
import com.opn.eshopify.domain.usecase.cart.AddProductToCartUseCase
import com.opn.eshopify.domain.usecase.cart.GetCartDetailUseCase
import com.opn.eshopify.domain.usecase.cart.RemoveProductFromCartUseCase
import com.opn.eshopify.domain.usecase.store.GetProductsUseCase
import com.opn.eshopify.domain.usecase.store.GetStoreDetailUseCase
import com.opn.eshopify.util.MainCoroutineRule
import io.mockk.coEvery
import io.mockk.every
import io.mockk.justRun
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
import kotlinx.datetime.LocalTime
import org.junit.After
import org.junit.Assert.assertEquals
import org.junit.Assert.assertNull
import org.junit.Before
import org.junit.Rule
import org.junit.Test

@ExperimentalCoroutinesApi
class StoreDetailViewModelTest {

    @get:Rule
    val mainCoroutineDispatcher = MainCoroutineRule(StandardTestDispatcher())

    private val useCases: StoreDetailUseCases = mockk()
    private lateinit var viewModel: StoreDetailViewModel

    private val testStore = Store(
        name = "Test Store",
        rating = 4.5f,
        openingTime = LocalTime(9, 0),
        closingTime = LocalTime(21, 0)
    )

    private val testProducts = listOf(
        Product(id = "1", name = "Product 1", price = 10.0, imageUrl = "url1"),
        Product(id = "2", name = "Product 2", price = 20.0, imageUrl = "url2")
    )

    private val testCart = Cart(
        products = mapOf(testProducts[0] to 1),
        deliveryAddress = "Test Address",
        totalPrice = 10.0,
        hasProducts = true
    )

    @Before
    fun setup() {
        // Provide defaults before ViewModel init
        every { useCases.getCartDetail() } returns flow { emit(testCart) }
        coEvery { useCases.getStoreDetail() } returns Result.Success(testStore)
        coEvery { useCases.getProducts() } returns Result.Success(testProducts)
        viewModel = StoreDetailViewModel(useCases)
    }

    @Test
    fun `loadData should update state with error when products fetch fails`() = runTest {
        // Given
        coEvery { useCases.getStoreDetail() } returns Result.Success(testStore)
        coEvery { useCases.getProducts() } returns Result.Error(DataError.Network.Unknown)

        // When
        viewModel.loadData()
        advanceUntilIdle()

        // Then
        assertEquals(testStore, viewModel.uiState.value.store)
        assertEquals(emptyList<Product>(), viewModel.uiState.value.products)
        assertEquals(true, viewModel.uiState.value.error != null)
        assertEquals(false, viewModel.uiState.value.isLoading)
    }

    @Test
    fun `loadData should end loading with error when both store and products fail`() = runTest {
        // Given
        coEvery { useCases.getStoreDetail() } returns Result.Error(DataError.Network.Timeout)
        coEvery { useCases.getProducts() } returns Result.Error(DataError.Network.Timeout)

        // When
        viewModel.loadData()
        advanceUntilIdle()

        // Then
        assertNull(viewModel.uiState.value.store)
        assertEquals(emptyList<Product>(), viewModel.uiState.value.products)
        assertEquals(true, viewModel.uiState.value.error != null)
        assertEquals(false, viewModel.uiState.value.isLoading)
    }

    @Test
    fun `loadData should set loading true immediately`() = runTest {
        // Given
        coEvery { useCases.getStoreDetail() } returns Result.Success(testStore)
        coEvery { useCases.getProducts() } returns Result.Success(testProducts)

        // When
        viewModel.loadData()

        // Then (before advancing coroutines)
        assertEquals(true, viewModel.uiState.value.isLoading)
    }

    @Test
    fun `observeCartDetails should apply subsequent emissions`() = runTest {
        // Given a second cart emission
        val updatedCart = testCart.copy(
            products = mapOf(testProducts[0] to 2),
            totalPrice = 20.0
        )
        every { useCases.getCartDetail() } returns flow {
            emit(testCart)
            emit(updatedCart)
        }
        // Recreate ViewModel to use the new stubbed flow
        viewModel = StoreDetailViewModel(useCases)

        // When
        advanceUntilIdle()

        // Then
        assertEquals(updatedCart.products, viewModel.uiState.value.selectedProducts)
        assertEquals(updatedCart.deliveryAddress, viewModel.uiState.value.deliveryAddress)
        assertEquals(updatedCart.totalPrice, viewModel.uiState.value.totalPrice, 0.001)
        assertEquals(updatedCart.hasProducts, viewModel.uiState.value.hasSelectedProducts)
    }

    @Test
    fun `loadData should update state with store and products when successful`() = runTest {
        // Given
        coEvery { useCases.getStoreDetail() } returns Result.Success(testStore)
        coEvery { useCases.getProducts() } returns Result.Success(testProducts)

        // When
        viewModel.loadData()
        advanceUntilIdle()

        // Then
        assertEquals(testStore, viewModel.uiState.value.store)
        assertEquals(testProducts, viewModel.uiState.value.products)
        assertNull(viewModel.uiState.value.error)
        assertEquals(false, viewModel.uiState.value.isLoading)
    }

    @Test
    fun `loadData should update state with error when store fetch fails`() = runTest {
        // Given
        coEvery { useCases.getStoreDetail() } returns Result.Error(DataError.Network.Unknown)
        coEvery { useCases.getProducts() } returns Result.Success(testProducts)

        // When
        viewModel.loadData()
        advanceUntilIdle()

        // Then
        assertNull(viewModel.uiState.value.store)
        assertEquals(testProducts, viewModel.uiState.value.products)
        assertEquals(true, viewModel.uiState.value.error != null)
        assertEquals(false, viewModel.uiState.value.isLoading)
    }

    @Test
    fun `incrementProductQuantity should call addProductToCart use case`() = runTest {
        // Given
        val product = testProducts[0]
        justRun { useCases.addProductToCart(product) }

        // When
        viewModel.incrementProductQuantity(product)

        // Then
        verify(exactly = 1) { useCases.addProductToCart(product) }
    }

    @Test
    fun `decrementProductQuantity should call removeProductFromCart use case`() = runTest {
        // Given
        val product = testProducts[0]
        justRun { useCases.removeProductFromCart(product) }

        // When
        viewModel.decrementProductQuantity(product)

        // Then
        verify(exactly = 1) { useCases.removeProductFromCart(product) }
    }
}
