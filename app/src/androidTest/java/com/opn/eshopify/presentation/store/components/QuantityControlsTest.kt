package com.opn.eshopify.presentation.store.components

import androidx.compose.ui.test.assertIsDisplayed
import androidx.compose.ui.test.junit4.createComposeRule
import androidx.compose.ui.test.onNodeWithContentDescription
import androidx.compose.ui.test.onNodeWithText
import androidx.compose.ui.test.performClick
import com.opn.eshopify.presentation.theme.EShopifyTheme
import org.junit.Rule
import org.junit.Test

class QuantityControlsTest {

    @get:Rule
    val composeRule = createComposeRule()

    @Test
    fun whenQuantityIsZero_shouldOnlyShowIncrement() {
        composeRule.setContent {
            EShopifyTheme {
                QuantityControls(
                    quantity = 0,
                    onIncrement = {},
                    onDecrement = {}
                )
            }
        }

        composeRule.onNodeWithContentDescription("Increment quantity").assertIsDisplayed()
        composeRule.onNodeWithContentDescription("Decrement quantity").assertDoesNotExist()
    }

    @Test
    fun whenQuantityIsNotZero_shouldEnableIncrementAndDecrement() {
        var inc = 0
        var dec = 0

        composeRule.setContent {
            EShopifyTheme {
                QuantityControls(
                    quantity = 2,
                    onIncrement = { inc++ },
                    onDecrement = { dec++ }
                )
            }
        }

        // Quantity text
        composeRule.onNodeWithText("2").assertIsDisplayed()
        composeRule.onNodeWithContentDescription("Increment quantity").performClick()
        composeRule.onNodeWithContentDescription("Decrement quantity").performClick()

        assert(inc == 1)
        assert(dec == 1)
    }
}
