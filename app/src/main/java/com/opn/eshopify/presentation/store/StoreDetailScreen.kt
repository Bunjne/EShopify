package com.opn.eshopify.presentation.store

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.navigationBarsPadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyListState
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.pulltorefresh.PullToRefreshBox
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.snapshotFlow
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.opn.eshopify.R
import com.opn.eshopify.domain.model.Product
import com.opn.eshopify.presentation.designsystem.error.ErrorMessage
import com.opn.eshopify.presentation.designsystem.indicator.GeneralLoadingIndicator
import com.opn.eshopify.presentation.store.components.CheckoutBottomBar
import com.opn.eshopify.presentation.store.components.EmptyProductsList
import com.opn.eshopify.presentation.store.components.ProductItem
import com.opn.eshopify.presentation.store.components.StoreHeader
import kotlinx.coroutines.flow.distinctUntilChanged
import org.koin.androidx.compose.koinViewModel

private const val PREFETCH_DISTANCE = 3

@Composable
fun StoreDetailRoute(
    onCheckout: () -> Unit,
    viewModel: StoreDetailViewModel = koinViewModel(),
) {
    val uiState by viewModel.uiState.collectAsStateWithLifecycle()

    StoreDetailScreen(
        uiState = uiState,
        onProductIncrement = viewModel::incrementProductQuantity,
        onProductDecrement = viewModel::decrementProductQuantity,
        onCheckout = onCheckout,
        onRetry = viewModel::loadData,
        onLoadMore = viewModel::loadMoreProducts,
        onRefresh = viewModel::refreshProducts,
        onRetryLoadMore = viewModel::loadMoreProducts
    )
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun StoreDetailScreen(
    uiState: StoreDetailUiState,
    onProductIncrement: (Product) -> Unit,
    onProductDecrement: (Product) -> Unit,
    onCheckout: () -> Unit,
    onRetry: () -> Unit,
    onLoadMore: () -> Unit,
    onRefresh: () -> Unit,
    onRetryLoadMore: () -> Unit,
) {
    val listState = rememberLazyListState()

    LoadMoreEffect(
        lazyListState = listState,
        onLoadMore = onLoadMore,
    )

    Scaffold(
        bottomBar = {
            CheckoutBottomBar(
                modifier = Modifier.navigationBarsPadding(),
                totalPrice = uiState.totalPrice,
                hasSelectedProducts = uiState.hasSelectedProducts,
                onCheckoutClicked = onCheckout,
            )
        },
    ) { paddingValues ->
        if (uiState.isLoading) {
            Box(modifier = Modifier.fillMaxSize()) {
                GeneralLoadingIndicator(modifier = Modifier.align(Alignment.Center))
            }
        } else if (uiState.error != null) {
            Box(modifier = Modifier.fillMaxSize()) {
                ErrorMessage(
                    error = uiState.error.toUiString(),
                    onRetry = onRetry
                )
            }
        } else {
            PullToRefreshBox(
                isRefreshing = uiState.isRefreshing,
                onRefresh = onRefresh,
                modifier = Modifier.fillMaxSize()
            ) {
                StoreWithProductsList(
                    uiState = uiState,
                    listState = listState,
                    paddingValues = paddingValues,
                    onProductIncrement = onProductIncrement,
                    onProductDecrement = onProductDecrement,
                    onRetryLoadMore = onRetryLoadMore
                )
            }
        }
    }
}

@Composable
private fun StoreWithProductsList(
    uiState: StoreDetailUiState,
    listState: LazyListState,
    paddingValues: androidx.compose.foundation.layout.PaddingValues,
    onProductIncrement: (Product) -> Unit,
    onProductDecrement: (Product) -> Unit,
    onRetryLoadMore: () -> Unit
) {
    LazyColumn(
        modifier = Modifier
            .fillMaxSize()
            .testTag("store_with_products_list"),
        state = listState,
        contentPadding = paddingValues
    ) {
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

            if (uiState.isLoadingMore) {
                item {
                    Box(
                        modifier = Modifier
                            .fillMaxSize()
                            .padding(vertical = 16.dp)
                    ) {
                        GeneralLoadingIndicator(modifier = Modifier.align(Alignment.Center))
                    }
                }
            }

            uiState.paginationError?.let { error ->
                item {
                    ErrorMessage(
                        error = error.toUiString(),
                        onRetry = onRetryLoadMore
                    )
                }
            }
        }
    }
}

@Composable
private fun LoadMoreEffect(
    lazyListState: LazyListState,
    onLoadMore: () -> Unit,
) {
    LaunchedEffect(lazyListState) {
        snapshotFlow { lazyListState.shouldLoadMore() }
            .distinctUntilChanged()
            .collect { shouldLoadMore ->
                if (shouldLoadMore) {
                    onLoadMore()
                }
            }
    }
}

private fun LazyListState.shouldLoadMore(): Boolean {
    val totalItems = layoutInfo.totalItemsCount
    val lastVisibleIndex = layoutInfo.visibleItemsInfo.lastOrNull()?.index ?: -1
    val prefetchedIndex = totalItems - PREFETCH_DISTANCE

    if (totalItems == 0) return false

    // Check if user has scrolled close to the end
    return lastVisibleIndex >= prefetchedIndex
}
