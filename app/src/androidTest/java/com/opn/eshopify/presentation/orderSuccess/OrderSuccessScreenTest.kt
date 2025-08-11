package com.opn.eshopify.presentation.orderSuccess

import androidx.compose.ui.test.assertIsDisplayed
import androidx.compose.ui.test.junit4.createComposeRule
import androidx.compose.ui.test.onNodeWithText
import androidx.compose.ui.test.performClick
import androidx.test.ext.junit.runners.AndroidJUnit4
import androidx.test.platform.app.InstrumentationRegistry
import com.opn.eshopify.R
import com.opn.eshopify.presentation.theme.EShopifyTheme
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith

class OrderSuccessScreenTest {

    @get:Rule
    val composeRule = createComposeRule()

    @Test
    fun displays_texts_and_back_to_store_click_works() {
        val ctx = InstrumentationRegistry.getInstrumentation().targetContext
        val title = ctx.getString(R.string.order_success_title)
        val message = ctx.getString(R.string.order_success_message)
        val back = ctx.getString(R.string.back_to_store)

        var clicked = false
        composeRule.setContent {
            EShopifyTheme {
                OrderSuccessScreen(onBackToStore = { clicked = true })
            }
        }

        composeRule.onNodeWithText(title).assertIsDisplayed()
        composeRule.onNodeWithText(message).assertIsDisplayed()
        composeRule.onNodeWithText(back).assertIsDisplayed().performClick()

        assert(clicked)
    }
}
