package com.opn.eshopify.presentation.designsystem.error

import androidx.compose.ui.test.assertIsDisplayed
import androidx.compose.ui.test.junit4.createComposeRule
import androidx.compose.ui.test.onNodeWithText
import androidx.compose.ui.test.performClick
import androidx.test.platform.app.InstrumentationRegistry
import com.opn.eshopify.R
import com.opn.eshopify.presentation.theme.EShopifyTheme
import org.junit.Rule
import org.junit.Test

class ErrorMessageTest {

    @get:Rule
    val composeRule = createComposeRule()

    @Test
    fun shouldShowErrorAndRetryWorks() {
        val ctx = InstrumentationRegistry.getInstrumentation().targetContext
        val title = ctx.getString(R.string.error_title)
        val retry = ctx.getString(R.string.retry)

        var retried = false
        composeRule.setContent {
            EShopifyTheme {
                ErrorMessage(error = "Network error", onRetry = { retried = true })
            }
        }

        composeRule.onNodeWithText(title).assertIsDisplayed()
        composeRule.onNodeWithText("Network error").assertIsDisplayed()
        composeRule.onNodeWithText(retry).assertIsDisplayed().performClick()

        assert(retried)
    }
}
