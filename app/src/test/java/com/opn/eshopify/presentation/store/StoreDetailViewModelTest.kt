package com.opn.eshopify.presentation.store

import com.opn.eshopify.R
import com.opn.eshopify.domain.DataError
import com.opn.eshopify.domain.Result
import com.opn.eshopify.domain.model.Cart
import com.opn.eshopify.domain.model.PaginatedResult
import com.opn.eshopify.domain.model.Product
import com.opn.eshopify.domain.model.Store
import com.opn.eshopify.presentation.util.TextValue
import com.opn.eshopify.util.MainCoroutineRule
import io.mockk.coEvery
import io.mockk.every
import io.mockk.justRun
import io.mockk.mockk
import io.mockk.verify
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.test.StandardTestDispatcher
import kotlinx.coroutines.test.advanceUntilIdle
import kotlinx.coroutines.test.runTest
import kotlinx.datetime.LocalTime
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

    private val testPaginatedProducts = PaginatedResult<Product>(
        items = testProducts,
        currentPage = 1,
        totalPages = 2,
        totalCount = 20,
        hasNextPage = true
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
        coEvery { useCases.getProducts(any(), any()) } returns Result.Success(testPaginatedProducts)
        viewModel = StoreDetailViewModel(useCases)
    }

    @Test
    fun `loadData should update state with error when products fetch fails`() = runTest {
        // Given
        coEvery { useCases.getStoreDetail() } returns Result.Success(testStore)
        coEvery { useCases.getProducts(page = 1, limit = 10) } returns Result.Error(DataError.Network.Unknown)

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
        coEvery { useCases.getProducts(page = 1, limit = 10) } returns Result.Error(DataError.Network.Timeout)

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
        coEvery { useCases.getProducts(page = 1, limit = 10) } returns Result.Success(testPaginatedProducts)

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
        coEvery { useCases.getProducts(page = 1, limit = 10) } returns Result.Success(testPaginatedProducts)

        // When
        viewModel.loadData()
        advanceUntilIdle()

        // Then
        assertEquals(testStore, viewModel.uiState.value.store)
        assertEquals(testProducts, viewModel.uiState.value.products)
        assertEquals(true, viewModel.uiState.value.hasMorePages)
        assertEquals(1, viewModel.uiState.value.currentPage)
        assertNull(viewModel.uiState.value.error)
        assertEquals(false, viewModel.uiState.value.isLoading)
    }

    @Test
    fun `loadData should update state with error when store fetch fails`() = runTest {
        // Given
        coEvery { useCases.getStoreDetail() } returns Result.Error(DataError.Network.Unknown)
        coEvery { useCases.getProducts(page = 1, limit = 10) } returns Result.Success(testPaginatedProducts)

        // When
        viewModel.loadData()
        advanceUntilIdle()

        // Then
        assertNull(viewModel.uiState.value.store)
        assertEquals(testProducts, viewModel.uiState.value.products)
        assertEquals(true, viewModel.uiState.value.hasMorePages)
        assertEquals(1, viewModel.uiState.value.currentPage)
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

    @Test
    fun `loadMoreProducts should append new products when successful`() = runTest {
        // Given
        val additionalProducts = listOf(
            Product(id = "3", name = "Product 3", price = 30.0, imageUrl = "url3"),
            Product(id = "4", name = "Product 4", price = 40.0, imageUrl = "url4")
        )
        val secondPagePaginatedProducts = PaginatedResult<Product>(
            items = additionalProducts,
            currentPage = 2,
            totalPages = 2,
            totalCount = 20,
            hasNextPage = false
        )

        viewModel.loadData()
        advanceUntilIdle()
        
        coEvery { useCases.getProducts(page = 2, limit = 10) } returns Result.Success(secondPagePaginatedProducts)

        // When
        viewModel.loadMoreProducts()
        advanceUntilIdle()

        // Then
        val expectedProducts = testProducts + additionalProducts
        assertEquals(expectedProducts, viewModel.uiState.value.products)
        assertEquals(2, viewModel.uiState.value.currentPage)
        assertEquals(false, viewModel.uiState.value.hasMorePages)
        assertEquals(false, viewModel.uiState.value.isLoadingMore)
        assertNull(viewModel.uiState.value.paginationError)
    }

    @Test
    fun `loadMoreProducts should set pagination error when API fails`() = runTest {
        // Given
        viewModel.loadData()
        advanceUntilIdle()
        
        coEvery { useCases.getProducts(page = 2, limit = 10) } returns Result.Error(DataError.Network.Timeout)

        // When
        viewModel.loadMoreProducts()
        advanceUntilIdle()

        // Then
        assertEquals(testProducts, viewModel.uiState.value.products) // Original products unchanged
        assertEquals(1, viewModel.uiState.value.currentPage) // Page unchanged
        assertEquals(true, viewModel.uiState.value.hasMorePages) // Still has more pages
        assertEquals(false, viewModel.uiState.value.isLoadingMore)
        assertEquals(true, viewModel.uiState.value.paginationError != null)
    }

    @Test
    fun `loadMoreProducts should not trigger when no more pages available`() = runTest {
        // Given
        val noMorePagesPaginatedProducts = testPaginatedProducts.copy(hasNextPage = false)
        coEvery { useCases.getProducts(page = 1, limit = 10) } returns Result.Success(noMorePagesPaginatedProducts)
        
        viewModel.loadData()
        advanceUntilIdle()

        // When
        viewModel.loadMoreProducts()
        advanceUntilIdle()

        // Then
        assertEquals(false, viewModel.uiState.value.isLoadingMore)
        assertEquals(false, viewModel.uiState.value.hasMorePages)
    }

    @Test
    fun `refreshProducts should replace products with fresh data`() = runTest {
        // Given
        viewModel.loadData()
        advanceUntilIdle()
        
        val refreshedProducts = listOf(
            Product(id = "5", name = "Refreshed Product 1", price = 50.0, imageUrl = "url5"),
            Product(id = "6", name = "Refreshed Product 2", price = 60.0, imageUrl = "url6")
        )
        val refreshedPaginatedProducts = PaginatedResult<Product>(
            items = refreshedProducts,
            currentPage = 1,
            totalPages = 3,
            totalCount = 30,
            hasNextPage = true
        )
        
        coEvery { useCases.getProducts(page = 1, limit = 10) } returns Result.Success(refreshedPaginatedProducts)

        // When
        viewModel.refreshProducts()
        advanceUntilIdle()

        // Then
        assertEquals(refreshedProducts, viewModel.uiState.value.products)
        assertEquals(1, viewModel.uiState.value.currentPage)
        assertEquals(true, viewModel.uiState.value.hasMorePages)
        assertEquals(false, viewModel.uiState.value.isRefreshing)
        assertNull(viewModel.uiState.value.error)
        assertNull(viewModel.uiState.value.paginationError)
    }

    @Test
    fun `refreshProducts should set pagination error when API fails`() = runTest {
        // Given
        viewModel.loadData()
        advanceUntilIdle()
        
        coEvery { useCases.getProducts(page = 1, limit = 10) } returns Result.Error(DataError.Network.NoInternet)

        // When
        viewModel.refreshProducts()
        advanceUntilIdle()

        // Then
        assertEquals(false, viewModel.uiState.value.isRefreshing)
        assertEquals(true, viewModel.uiState.value.paginationError != null)
        assertEquals(testProducts, viewModel.uiState.value.products)
    }

    @Test
    fun `canLoadMore should return a correct value depending on the state`() {
        // Test case 1: Can load more when all conditions are met
        val canLoadState = StoreDetailUiState(
            hasMorePages = true,
            isLoadingMore = false,
            isRefreshing = false,
            paginationError = null
        )
        assert(canLoadState.canLoadMore)

        // Test case 2: Cannot load more when no more pages
        val noMorePagesState = StoreDetailUiState(
            hasMorePages = false,
            isLoadingMore = false,
            isRefreshing = false,
            paginationError = null
        )
        assert(!noMorePagesState.canLoadMore)

        // Test case 3: Cannot load more when already loading
        val loadingState = StoreDetailUiState(
            hasMorePages = true,
            isLoadingMore = true,
            isRefreshing = false,
            paginationError = null
        )
        assert(!loadingState.canLoadMore)

        // Test case 4: Cannot load more when refreshing
        val refreshingState = StoreDetailUiState(
            hasMorePages = true,
            isLoadingMore = false,
            isRefreshing = true,
            paginationError = null
        )
        assert(!refreshingState.canLoadMore)

        // Test case 5: Cannot load more when there's a pagination error
        val errorState = StoreDetailUiState(
            hasMorePages = true,
            isLoadingMore = false,
            isRefreshing = false,
            paginationError = TextValue.Resource(R.string.error_no_internet)
        )
        assert(!errorState.canLoadMore)
    }
}
