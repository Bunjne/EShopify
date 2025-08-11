package com.opn.eshopify.presentation.orderSummary

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.compose.ui.test.assertIsDisplayed
import androidx.compose.ui.test.assertIsEnabled
import androidx.compose.ui.test.assertIsNotEnabled
import androidx.compose.ui.test.junit4.createComposeRule
import androidx.compose.ui.test.onNodeWithText
import androidx.compose.ui.test.performClick
import androidx.compose.ui.test.performTextInput
import androidx.test.platform.app.InstrumentationRegistry
import com.opn.eshopify.R
import com.opn.eshopify.domain.model.Product
import com.opn.eshopify.presentation.theme.EShopifyTheme
import org.junit.Rule
import org.junit.Test

class OrderSummaryScreenTest {

    @get:Rule
    val composeRule = createComposeRule()

    private fun product(i: Int) = Product(
        id = i.toString(),
        name = "Product $i",
        price = 5.0,
        imageUrl = ""
    )

    @Test
    fun whenNoProductsSelected_shouldDisablePlaceOrder() {
        val ctx = InstrumentationRegistry.getInstrumentation().targetContext
        val placeOrderText = ctx.getString(R.string.place_order)

        val emptyState = OrderSummaryUiState(
            selectedProducts = emptyMap(),
            deliveryAddress = "",
            totalPrice = 0.0,
            hasSelectedProducts = false,
            isLoading = false,
            error = null,
            isOrderPlaced = false
        )

        composeRule.setContent {
            EShopifyTheme {
                SummaryScreen(
                    uiState = emptyState,
                    onNavigateBack = {},
                    onDeliveryAddressChange = {},
                    onPlaceOrder = {}
                )
            }
        }

        composeRule.onNodeWithText(placeOrderText).assertIsDisplayed().assertIsNotEnabled()
    }

    @Test
    fun whenProductsSelectedAndHaveAddress_shouldEnablePlaceOrder() {
        val ctx = InstrumentationRegistry.getInstrumentation().targetContext
        val placeOrderText = ctx.getString(R.string.place_order)

        val p1 = product(1)
        val state = OrderSummaryUiState(
            selectedProducts = mapOf(p1 to 2),
            deliveryAddress = "Somewhere street 1",
            totalPrice = 10.0,
            hasSelectedProducts = true,
            isLoading = false,
            error = null,
            isOrderPlaced = false
        )

        var clicked = false
        composeRule.setContent {
            EShopifyTheme {
                SummaryScreen(
                    uiState = state,
                    onNavigateBack = {},
                    onDeliveryAddressChange = {},
                    onPlaceOrder = { clicked = true }
                )
            }
        }

        composeRule.onNodeWithText(placeOrderText).assertIsDisplayed().assertIsEnabled().performClick()
        assert(clicked)
    }


    @Test
    fun whenTypingAddressAndProductsSelected_shouldEnablePlaceOrder() {
        val ctx = InstrumentationRegistry.getInstrumentation().targetContext
        val label = ctx.getString(R.string.enter_delivery_address)
        val placeOrder = ctx.getString(R.string.place_order)

        val p1 = Product("1", "P1", 10.0, "")
        var state by mutableStateOf(
            OrderSummaryUiState(
                selectedProducts = mapOf(p1 to 1),
                deliveryAddress = "",
                totalPrice = 10.0,
                hasSelectedProducts = true,
                isLoading = false,
                error = null,
                isOrderPlaced = false
            )
        )

        var clicked = false
        composeRule.setContent {
            EShopifyTheme {
                SummaryScreen(
                    uiState = state,
                    onNavigateBack = {},
                    onDeliveryAddressChange = { addr -> state = state.copy(deliveryAddress = addr) },
                    onPlaceOrder = { clicked = true }
                )
            }
        }

        composeRule.onNodeWithText(label).performTextInput("Some address")
        composeRule.onNodeWithText(placeOrder).assertIsEnabled().performClick()
        assert(clicked)
    }
}
