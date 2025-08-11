package com.opn.eshopify.presentation.navigation

import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.test.junit4.createComposeRule
import androidx.compose.ui.test.onNodeWithTag
import androidx.compose.ui.test.performClick
import androidx.navigation.NavDestination.Companion.hasRoute
import androidx.navigation.compose.ComposeNavigator
import androidx.navigation.testing.TestNavHostController
import com.opn.eshopify.di.testRepositoryModule
import com.opn.eshopify.presentation.app.MainAppState
import com.opn.eshopify.presentation.app.rememberMainAppState
import com.opn.eshopify.util.KoinTestRule
import org.junit.Before
import org.junit.Rule
import org.junit.Test

class NavigationFlowTest {

    @get:Rule
    private val koinRule = KoinTestRule(listOf(testRepositoryModule))

    @get:Rule
    val composeRule = createComposeRule()

    private lateinit var appState: MainAppState
    private lateinit var navController: TestNavHostController

    @Before
    fun setup() {
        composeRule.setContent {
            navController = TestNavHostController(LocalContext.current)
            appState = rememberMainAppState(navController = navController)
            appState.navController.navigatorProvider.addNavigator(ComposeNavigator())

            MainNavHost(appState = appState)
        }
    }

    @Test
    fun verifyStartDestinationIsStoreDetail() {
        assert(navController.currentBackStackEntry?.destination?.hasRoute<Route.StoreDetail>() == true)
    }

    @Test
    fun whenUserClicksCheckoutOnStoreDetail_shouldNavigateToSummary() {
        composeRule.onNodeWithTag("checkout_button").performClick()
        assert(navController.currentBackStackEntry?.destination?.hasRoute<Route.Summary>() == true)
    }

    @Test
    fun whenUserClicksPlaceOrderOnSummary_shouldNavigateToSuccess() {
        composeRule.runOnUiThread {
            navController.navigate(Route.Summary)
        }
        composeRule.onNodeWithTag("place_order_button").performClick()

        assert(navController.currentBackStackEntry?.destination?.hasRoute<Route.Summary>() == true)
    }

    @Test
    fun whenUserClicksBackButtonOnSummary_shouldNavigateBackToStoreDetail() {
        composeRule.runOnUiThread {
            navController.navigate(Route.Summary)
        }
        composeRule.onNodeWithTag("navigate_back").performClick()

        assert(navController.currentBackStackEntry?.destination?.hasRoute<Route.StoreDetail>() == true)
        assert(navController.backStack.none { it.destination.hasRoute<Route.Summary>() })
    }

    @Test
    fun whenUserClicksBackToStoreOnSuccess_shouldNavigateToStoreDetail() {
        composeRule.runOnUiThread {
            navController.navigate(Route.Success)
        }
        composeRule.onNodeWithTag("back_to_store_button").performClick()

        assert(navController.currentBackStackEntry?.destination?.hasRoute<Route.StoreDetail>() == true)
        assert(navController.backStack.none {
            it.destination.hasRoute<Route.Summary>() && it.destination.hasRoute<Route.Success>()
        })
    }
}
