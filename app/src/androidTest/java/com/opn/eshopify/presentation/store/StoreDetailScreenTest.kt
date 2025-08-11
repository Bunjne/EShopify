package com.opn.eshopify.presentation.store

import androidx.compose.ui.test.assertIsDisplayed
import androidx.compose.ui.test.assertIsEnabled
import androidx.compose.ui.test.assertIsNotEnabled
import androidx.compose.ui.test.junit4.createComposeRule
import androidx.compose.ui.test.onNodeWithContentDescription
import androidx.compose.ui.test.onNodeWithTag
import androidx.compose.ui.test.onNodeWithText
import androidx.compose.ui.test.performClick
import androidx.test.platform.app.InstrumentationRegistry
import com.opn.eshopify.R
import com.opn.eshopify.domain.model.Product
import com.opn.eshopify.presentation.theme.EShopifyTheme
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
                    onRetry = {}
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
                    onRetry = {}
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
                    onRetry = {}
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
                    onRetry = {}
                )
            }
        }

        composeRule.onNodeWithTag("product_1").assertIsDisplayed()
        composeRule.onNodeWithTag("product_2").assertIsDisplayed()
    }
}
