package com.opn.eshopify.presentation.store

import androidx.compose.ui.test.assertIsDisplayed
import androidx.compose.ui.test.assertIsEnabled
import androidx.compose.ui.test.assertIsNotEnabled
import androidx.compose.ui.test.junit4.createComposeRule
import androidx.compose.ui.test.onNodeWithContentDescription
import androidx.compose.ui.test.onNodeWithTag
import androidx.compose.ui.test.onNodeWithText
import androidx.compose.ui.test.performClick
import androidx.compose.ui.test.performScrollToIndex
import androidx.test.platform.app.InstrumentationRegistry
import com.opn.eshopify.R
import com.opn.eshopify.domain.model.Product
import com.opn.eshopify.presentation.theme.EShopifyTheme
import com.opn.eshopify.presentation.util.TextValue
import org.junit.Rule
import org.junit.Test

class StoreDetailScreenTest {

    @get:Rule
    val composeRule = createComposeRule()

    private fun fakeProduct(index: Int) = Product(
        id = index.toString(),
        name = "Product $index",
        price = 10.0 * index,
        imageUrl = "https://example.com/$index.png"
    )

    @Test
    fun whenNoSelectedProducts_shouldEnableCheckout() {
        val state = StoreDetailUiState(
            isLoading = false,
            error = null,
            store = null,
            products = listOf(fakeProduct(1), fakeProduct(2)),
            selectedProducts = emptyMap(),
            totalPrice = 0.0,
            hasSelectedProducts = false
        )

        composeRule.setContent {
            EShopifyTheme {
                StoreDetailScreen(
                    uiState = state,
                    onProductIncrement = {},
                    onProductDecrement = {},
                    onCheckout = {},
                    onRetry = {},
                    onLoadMore = {},
                    onRefresh = {},
                    onRetryLoadMore = {}
                )
            }
        }

        val checkoutText =
            InstrumentationRegistry.getInstrumentation().targetContext.getString(R.string.checkout)
        composeRule.onNodeWithText(checkoutText).assertIsDisplayed().assertIsNotEnabled()
    }

    @Test
    fun whenHavingSelectedProducts_shouldEnableCheckout() {
        val p1 = fakeProduct(1)
        var clickedCheckout = false
        var selected = mapOf(p1 to 1)
        val state = StoreDetailUiState(
            isLoading = false,
            error = null,
            store = null,
            products = listOf(p1),
            selectedProducts = selected,
            totalPrice = 10.0,
            hasSelectedProducts = true
        )

        composeRule.setContent {
            EShopifyTheme {
                StoreDetailScreen(
                    uiState = state,
                    onProductIncrement = {},
                    onProductDecrement = {},
                    onCheckout = { clickedCheckout = true },
                    onRetry = {},
                    onLoadMore = {},
                    onRefresh = {},
                    onRetryLoadMore = {}
                )
            }
        }

        composeRule.onNodeWithContentDescription("Increment quantity").assertIsDisplayed()
        composeRule.onNodeWithContentDescription("Decrement quantity").assertIsDisplayed()

        val checkoutText =
            InstrumentationRegistry.getInstrumentation().targetContext.getString(R.string.checkout)
        composeRule.onNodeWithText(checkoutText).assertIsDisplayed().assertIsEnabled()
            .performClick()

        assert(clickedCheckout)
    }

    @Test
    fun whenNoProducts_shouldShowEmptyProductsMessage() {
        val ctx = InstrumentationRegistry.getInstrumentation().targetContext
        val noProducts = ctx.getString(R.string.no_products_available)
        val checkLater = ctx.getString(R.string.check_back_later)

        val state = StoreDetailUiState(
            isLoading = false,
            error = null,
            store = null,
            products = emptyList(),
            selectedProducts = emptyMap(),
            totalPrice = 0.0,
            hasSelectedProducts = false
        )

        composeRule.setContent {
            EShopifyTheme {
                StoreDetailScreen(
                    uiState = state,
                    onProductIncrement = {},
                    onProductDecrement = {},
                    onCheckout = {},
                    onRetry = {},
                    onLoadMore = {},
                    onRefresh = {},
                    onRetryLoadMore = {}
                )
            }
        }

        composeRule.onNodeWithText(noProducts).assertIsDisplayed()
        composeRule.onNodeWithText(checkLater).assertIsDisplayed()
    }

    @Test
    fun whenProductsAvailable_shouldShowProducts() {
        val p1 = fakeProduct(1)
        val p2 = fakeProduct(2)
        val state = StoreDetailUiState(
            isLoading = false,
            error = null,
            store = null,
            products = listOf(p1, p2),
            selectedProducts = mapOf(p1 to 1),
            totalPrice = 2.5,
            hasSelectedProducts = true
        )

        composeRule.setContent {
            EShopifyTheme {
                StoreDetailScreen(
                    uiState = state,
                    onProductIncrement = {},
                    onProductDecrement = {},
                    onCheckout = {},
                    onRetry = {},
                    onLoadMore = {},
                    onRefresh = {},
                    onRetryLoadMore = {}
                )
            }
        }

        composeRule.onNodeWithTag("product_1").assertIsDisplayed()
        composeRule.onNodeWithTag("product_2").assertIsDisplayed()
    }

    @Test
    fun whenPaginationLoading_shouldShowLoadingIndicator() {
        val products = (1..5).map { fakeProduct(it) }
        val state = StoreDetailUiState(
            isLoading = false,
            error = null,
            store = null,
            products = products,
            selectedProducts = emptyMap(),
            totalPrice = 0.0,
            hasSelectedProducts = false,
            hasMorePages = true,
            isLoadingMore = true
        )

        composeRule.setContent {
            EShopifyTheme {
                StoreDetailScreen(
                    uiState = state,
                    onProductIncrement = {},
                    onProductDecrement = {},
                    onCheckout = {},
                    onRetry = {},
                    onLoadMore = {},
                    onRefresh = {},
                    onRetryLoadMore = {}
                )
            }
        }

        composeRule.onNodeWithTag("product_1").assertIsDisplayed()
        composeRule.onNodeWithTag("loading_indicator").assertIsDisplayed()
    }

    @Test
    fun whenPaginationError_shouldShowErrorMessageAndAbelToRetry() {
        val products = (1..5).map { fakeProduct(it) }
        val errorMessage = TextValue.Resource(R.string.error_no_internet)
        val state = StoreDetailUiState(
            products = products,
            hasMorePages = true,
            paginationError = errorMessage,
        )

        var retryClicked = false

        composeRule.setContent {
            EShopifyTheme {
                StoreDetailScreen(
                    uiState = state,
                    onProductIncrement = {},
                    onProductDecrement = {},
                    onCheckout = {},
                    onRetry = {},
                    onLoadMore = {},
                    onRefresh = {},
                    onRetryLoadMore = { retryClicked = true }
                )
            }
        }

        composeRule.onNodeWithTag("product_1").assertIsDisplayed()
        
        val ctx = InstrumentationRegistry.getInstrumentation().targetContext
        val networkError = ctx.getString(R.string.error_no_internet)
        composeRule.onNodeWithText(networkError).assertIsDisplayed()
        
        val retryText = ctx.getString(R.string.retry)
        composeRule.onNodeWithText(retryText).assertIsDisplayed().performClick()
        
        assert(retryClicked)
    }

    @Test
    fun whenScrollingToEnd_shouldTriggerLoadMore() {
        val products = (1..10).map { fakeProduct(it) }
        var loadMoreCalled = false
        
        val state = StoreDetailUiState(
            products = products,
            hasMorePages = true,
        )

        composeRule.setContent {
            EShopifyTheme {
                StoreDetailScreen(
                    uiState = state,
                    onProductIncrement = {},
                    onProductDecrement = {},
                    onCheckout = {},
                    onRetry = {},
                    onLoadMore = { loadMoreCalled = true },
                    onRefresh = {},
                    onRetryLoadMore = {}
                )
            }
        }

        composeRule.onNodeWithTag("store_with_products_list")
            .performScrollToIndex(products.size - 1)

        composeRule.waitForIdle()
        assert(loadMoreCalled)
    }
}
