package com.opn.eshopify.presentation.orderSummary

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.navigationBarsPadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import com.opn.eshopify.R
import com.opn.eshopify.presentation.orderSummary.components.OrderProductItem
import com.opn.eshopify.presentation.orderSummary.components.PlaceOrderBottomBar
import com.opn.eshopify.presentation.orderSummary.components.TotalPrice
import com.opn.eshopify.presentation.store.components.ErrorMessage
import org.koin.androidx.compose.koinViewModel

@Composable
fun SummaryRoute(
    onNavigateBack: () -> Unit,
    onOrderSuccess: () -> Unit,
    viewModel: OrderSummaryViewModel = koinViewModel(),
) {
    val uiState by viewModel.uiState.collectAsState()

    LaunchedEffect(uiState.isOrderPlaced) {
        if (uiState.isOrderPlaced) {
            onOrderSuccess()
        }
    }

    SummaryScreen(
        uiState = uiState,
        onNavigateBack = onNavigateBack,
        onDeliveryAddressChange = viewModel::updateDeliveryAddress,
        onPlaceOrder = viewModel::placeOrder
    )
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SummaryScreen(
    uiState: OrderSummaryUiState,
    onNavigateBack: () -> Unit,
    onDeliveryAddressChange: (String) -> Unit,
    onPlaceOrder: () -> Unit
) {
    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text(text = stringResource(R.string.order_summary)) },
                navigationIcon = {
                    IconButton(onClick = onNavigateBack) {
                        Icon(
                            imageVector = Icons.AutoMirrored.Filled.ArrowBack,
                            contentDescription = stringResource(R.string.navigate_back)
                        )
                    }
                }
            )
        },
        bottomBar = {
            PlaceOrderBottomBar(
                modifier = Modifier.navigationBarsPadding(),
                isEnabled = !uiState.isLoading && uiState.deliveryAddress.isNotBlank() && uiState.selectedProducts.isNotEmpty(),
                isLoading = uiState.isLoading,
                onPlaceOrderClicked = onPlaceOrder
            )
        }
    ) { paddingValues ->
        Box(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues)
        ) {
            if (uiState.error != null) {
                ErrorMessage(
                    error = uiState.error.toUiString(),
                    onRetry = onPlaceOrder
                )
            } else {
                LazyColumn(
                    modifier = Modifier.fillMaxSize(),
                    contentPadding = PaddingValues(16.dp),
                    verticalArrangement = Arrangement.spacedBy(16.dp)
                ) {
                    item {
                        Text(
                            text = stringResource(R.string.your_order),
                            style = MaterialTheme.typography.titleLarge
                        )
                    }

                    if (uiState.selectedProducts.isEmpty()) {
                        item {
                            Text(
                                text = stringResource(R.string.no_products_selected),
                                style = MaterialTheme.typography.bodyMedium,
                                color = MaterialTheme.colorScheme.onSurfaceVariant
                            )
                        }
                    } else {
                        items(
                            items = uiState.selectedProducts.keys.toList(),
                            key = { it.id },
                        ) { product ->
                            OrderProductItem(
                                product = product,
                                quantity = uiState.selectedProducts[product] ?: 0,
                            )
                        }
                    }

                    item {
                        TotalPrice(
                            modifier = Modifier.fillMaxSize(),
                            totalPrice = uiState.totalPrice,
                        )
                    }

                    item {
                        Column {
                            Text(
                                text = stringResource(R.string.delivery_address),
                                style = MaterialTheme.typography.titleMedium,
                                modifier = Modifier.padding(vertical = 8.dp)
                            )

                            OutlinedTextField(
                                value = uiState.deliveryAddress,
                                onValueChange = onDeliveryAddressChange,
                                modifier = Modifier.fillMaxWidth(),
                                label = { Text(stringResource(R.string.enter_delivery_address)) },
                                keyboardOptions = KeyboardOptions(
                                    keyboardType = KeyboardType.Text,
                                    imeAction = ImeAction.Done
                                ),
                                maxLines = 3
                            )
                        }
                    }
                }
            }
        }
    }
}
