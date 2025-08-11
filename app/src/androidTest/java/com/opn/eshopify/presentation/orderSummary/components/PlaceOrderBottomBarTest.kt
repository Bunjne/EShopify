package com.opn.eshopify.presentation.orderSummary.components

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

class PlaceOrderBottomBarTest {

    @get:Rule
    val composeRule = createComposeRule()

    @Test
    fun whenBottomBarDisabled_shouldNotBeClickable() {
        val ctx = InstrumentationRegistry.getInstrumentation().targetContext
        val text = ctx.getString(R.string.place_order)

        composeRule.setContent {
            EShopifyTheme {
                PlaceOrderBottomBar(
                    onPlaceOrderClicked = {},
                    isLoading = false,
                    isEnabled = false
                )
            }
        }

        composeRule.onNodeWithText(text).assertIsDisplayed().assertIsNotEnabled()
    }

    @Test
    fun whenBottomBarEnabled_shouldBeClickable() {
        val ctx = InstrumentationRegistry.getInstrumentation().targetContext
        val text = ctx.getString(R.string.place_order)
        var clicked = false

        composeRule.setContent {
            EShopifyTheme {
                PlaceOrderBottomBar(
                    onPlaceOrderClicked = { clicked = true },
                    isLoading = false,
                    isEnabled = true
                )
            }
        }

        composeRule.onNodeWithText(text).assertIsEnabled().performClick()
        assert(clicked)
    }
}
