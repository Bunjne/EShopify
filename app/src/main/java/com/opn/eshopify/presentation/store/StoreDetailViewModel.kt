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
                error = null
            )
        }

        viewModelScope.launch {
            val storeDetailDeferred = async { useCases.getStoreDetail.invoke() }
            val productsDeferred = async { useCases.getProducts.invoke() }

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
                is Result.Success -> _uiState.update {
                    it.copy(products = productsResult.data)
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

    fun incrementProductQuantity(product: Product) {
        useCases.addProductToCart(product)
    }

    fun decrementProductQuantity(product: Product) {
        useCases.removeProductFromCart(product)
    }
}
