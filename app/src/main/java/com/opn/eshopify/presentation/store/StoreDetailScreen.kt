package com.opn.eshopify.presentation.store

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.opn.eshopify.R
import com.opn.eshopify.domain.model.Product
import com.opn.eshopify.presentation.store.components.CheckoutBottomBar
import com.opn.eshopify.presentation.store.components.EmptyProductsList
import com.opn.eshopify.presentation.store.components.ErrorMessage
import com.opn.eshopify.presentation.store.components.ProductItem
import com.opn.eshopify.presentation.store.components.StoreHeader

@Composable
fun StoreDetailRoute(
    onCheckout: () -> Unit,
    viewModel: StoreDetailViewModel,
) {
    val uiState by viewModel.uiState.collectAsStateWithLifecycle()

    StoreDetailScreen(
        uiState = uiState,
        onProductIncrement = viewModel::incrementProductQuantity,
        onProductDecrement = viewModel::decrementProductQuantity,
        onCheckout = onCheckout,
        onRetry = viewModel::loadData
    )
}

@Composable
fun StoreDetailScreen(
    uiState: StoreDetailUiState,
    onProductIncrement: (Product) -> Unit,
    onProductDecrement: (Product) -> Unit,
    onCheckout: () -> Unit,
    onRetry: () -> Unit,
) {
    Scaffold(
        bottomBar = {
            CheckoutBottomBar(
                totalPrice = uiState.totalPrice,
                hasSelectedProducts = uiState.hasSelectedProducts,
                onCheckoutClick = onCheckout,
            )
        }
    ) { paddingValues ->
        Box(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues)
        ) {
            if (uiState.isLoading) {
                CircularProgressIndicator(modifier = Modifier.align(Alignment.Center))
            } else if (uiState.error != null) {
                ErrorMessage(
                    error = uiState.error.toUiString(),
                    onRetry = onRetry
                )
            } else {
                LazyColumn(modifier = Modifier.fillMaxSize()) {
                    uiState.store?.let { store ->
                        item {
                            StoreHeader(store = store)
                        }

                        item {
                            Spacer(modifier = Modifier.height(16.dp))
                            Text(
                                text = stringResource(R.string.products),
                                style = MaterialTheme.typography.titleLarge,
                                modifier = Modifier.padding(horizontal = 16.dp)
                            )
                            Spacer(modifier = Modifier.height(8.dp))
                        }
                    }

                    if (uiState.products.isEmpty() && !uiState.isLoading) {
                        item {
                            EmptyProductsList()
                        }
                    } else {
                        items(
                            items = uiState.products,
                            key = { product -> product.id }
                        ) { product ->
                            ProductItem(
                                product = product,
                                quantity = uiState.selectedProducts[product] ?: 0,
                                onIncrement = { onProductIncrement(product) },
                                onDecrement = { onProductDecrement(product) },
                            )
                        }
                    }
                }
            }
        }
    }
}
