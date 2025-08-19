package com.opn.eshopify.presentation.store

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.opn.eshopify.domain.Result
import com.opn.eshopify.domain.model.Product
import com.opn.eshopify.presentation.util.asTextValue
import kotlinx.coroutines.async
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

private const val PAGE_SIZE = 10
private const val FIRST_PAGE = 1

class StoreDetailViewModel(
    private val useCases: StoreDetailUseCases,
) : ViewModel() {

    private val _uiState = MutableStateFlow(StoreDetailUiState())
    val uiState: StateFlow<StoreDetailUiState> get() = _uiState.asStateFlow()

    init {
        loadData()
        observeCartDetails()
    }

    private fun observeCartDetails() {
        viewModelScope.launch {
            useCases.getCartDetail().collect { cartState ->
                _uiState.update { currentState ->
                    currentState.copy(
                        selectedProducts = cartState.products,
                        deliveryAddress = cartState.deliveryAddress,
                        totalPrice = cartState.totalPrice,
                        hasSelectedProducts = cartState.hasProducts
                    )
                }
            }
        }
    }

    fun loadData() {
        _uiState.update {
            it.copy(
                isLoading = true,
                error = null,
                currentPage = 1,
                products = emptyList()
            )
        }

        viewModelScope.launch {
            val storeDetailDeferred = async { useCases.getStoreDetail() }
            val productsDeferred = async { useCases.getProducts(page = FIRST_PAGE, limit = PAGE_SIZE) }

            // Await all to show result at the same time
            val storeResult = storeDetailDeferred.await()
            val productsResult = productsDeferred.await()

            when (storeResult) {
                is Result.Success -> _uiState.update {
                    it.copy(store = storeResult.data)
                }

                is Result.Error -> _uiState.update {
                    it.copy(error = storeResult.error.asTextValue())
                }
            }

            when (productsResult) {
                is Result.Success -> {
                    val paginatedProducts = productsResult.data
                    _uiState.update {
                        it.copy(
                            products = paginatedProducts.items,
                            hasMorePages = paginatedProducts.hasNextPage,
                            currentPage = paginatedProducts.currentPage
                        )
                    }
                }

                is Result.Error -> _uiState.update {
                    it.copy(error = productsResult.error.asTextValue())
                }
            }

            _uiState.update {
                it.copy(isLoading = false)
            }
        }
    }

    fun loadMoreProducts() {
        val currentState = _uiState.value
        if (!currentState.canLoadMore) return

        _uiState.update {
            it.copy(
                isLoadingMore = true,
                paginationError = null
            )
        }

        viewModelScope.launch {
            val nextPage = currentState.currentPage + 1
            when (val result = useCases.getProducts(page = nextPage, limit = PAGE_SIZE)) {
                is Result.Success -> {
                    val paginatedProducts = result.data
                    _uiState.update { currentState ->
                        currentState.copy(
                            products = currentState.products + paginatedProducts.items,
                            hasMorePages = paginatedProducts.hasNextPage,
                            currentPage = paginatedProducts.currentPage,
                            isLoadingMore = false
                        )
                    }
                }

                is Result.Error -> _uiState.update {
                    it.copy(
                        isLoadingMore = false,
                        paginationError = result.error.asTextValue()
                    )
                }
            }
        }
    }

    fun refreshProducts() {
        _uiState.update {
            it.copy(
                isRefreshing = true,
                paginationError = null
            )
        }

        viewModelScope.launch {
            when (val result = useCases.getProducts(page = FIRST_PAGE, limit = PAGE_SIZE)) {
                is Result.Success -> {
                    val paginatedProducts = result.data
                    _uiState.update {
                        it.copy(
                            products = paginatedProducts.items,
                            hasMorePages = paginatedProducts.hasNextPage,
                            currentPage = paginatedProducts.currentPage,
                            isRefreshing = false,
                            error = null
                        )
                    }
                }

                is Result.Error -> _uiState.update {
                    it.copy(
                        isRefreshing = false,
                        paginationError = result.error.asTextValue()
                    )
                }
            }
        }
    }

    fun incrementProductQuantity(product: Product) {
        useCases.addProductToCart(product)
    }

    fun decrementProductQuantity(product: Product) {
        useCases.removeProductFromCart(product)
    }
}
