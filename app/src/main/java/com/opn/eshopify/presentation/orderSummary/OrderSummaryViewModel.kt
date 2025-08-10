package com.opn.eshopify.presentation.orderSummary

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.opn.eshopify.domain.Result
import com.opn.eshopify.presentation.util.asTextValue
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

class OrderSummaryViewModel(
    private val useCases: OrderSummaryUseCases,
) : ViewModel() {

    private val _uiState = MutableStateFlow(OrderSummaryUiState())
    val uiState: StateFlow<OrderSummaryUiState> = _uiState.asStateFlow()

    init {
        observeCartState()
    }

    private fun observeCartState() {
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

    fun placeOrder() {
        _uiState.update {
            it.copy(
                isLoading = true,
                error = null
            )
        }

        viewModelScope.launch {
            when (val result = useCases.placeOrder()) {
                is Result.Success -> _uiState.update {
                    it.copy(isOrderPlaced = true)
                }
                is Result.Error -> _uiState.update {
                    it.copy(error = result.error.asTextValue())
                }
            }

            _uiState.update {
                it.copy(isLoading = false)
            }
        }
    }

    fun updateDeliveryAddress(address: String) {
        useCases.updateDeliveryAddress(address)
    }
}
