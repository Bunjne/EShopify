package com.opn.eshopify.presentation.store.components

import androidx.compose.ui.test.assertIsDisplayed
import androidx.compose.ui.test.assertIsEnabled
import androidx.compose.ui.test.assertIsNotEnabled
import androidx.compose.ui.test.junit4.createComposeRule
import androidx.compose.ui.test.onNodeWithText
import androidx.compose.ui.test.performClick
import androidx.test.platform.app.InstrumentationRegistry
import com.opn.eshopify.R
import com.opn.eshopify.presentation.theme.EShopifyTheme
import org.junit.Rule
import org.junit.Test

class CheckoutBottomBarTest {

    @get:Rule
    val composeRule = createComposeRule()

    @Test
    fun whenNoProducts_shouldDisableCheckout() {
        val ctx = InstrumentationRegistry.getInstrumentation().targetContext
        val totalText = ctx.getString(R.string.total)
        val checkoutText = ctx.getString(R.string.checkout)

        composeRule.setContent {
            EShopifyTheme {
                CheckoutBottomBar(
                    totalPrice = 0.0,
                    hasSelectedProducts = false,
                    onCheckoutClicked = {}
                )
            }
        }

        composeRule.onNodeWithText(totalText).assertIsDisplayed()
        composeRule.onNodeWithText(checkoutText).assertIsDisplayed().assertIsNotEnabled()
    }

    @Test
    fun whenHavingProductsAndCheckoutClicked_shouldTriggerCallback() {
        val ctx = InstrumentationRegistry.getInstrumentation().targetContext
        val checkoutText = ctx.getString(R.string.checkout)
        var clicked = false

        composeRule.setContent {
            EShopifyTheme {
                CheckoutBottomBar(
                    totalPrice = 99.99,
                    hasSelectedProducts = true,
                    onCheckoutClicked = { clicked = true }
                )
            }
        }

        composeRule.onNodeWithText(checkoutText).assertIsEnabled().performClick()
        assert(clicked)
    }
}
