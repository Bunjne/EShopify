package com.opn.eshopify.presentation.navigation

import androidx.compose.runtime.Composable
import androidx.navigation.compose.NavHost
import com.opn.eshopify.presentation.app.MainAppState
import com.opn.eshopify.presentation.orderSuccess.OrderSuccessRoute
import com.opn.eshopify.presentation.orderSummary.SummaryRoute
import com.opn.eshopify.presentation.store.StoreDetailRoute
import com.opn.eshopify.presentation.util.animatedComposable

@Composable
fun MainNavHost(
    appState: MainAppState,
    startDestination: Route = Route.StoreDetail
) {
    NavHost(
        navController = appState.navController,
        startDestination = startDestination
    ) {
        animatedComposable<Route.StoreDetail> {
            StoreDetailRoute(
                onCheckout = {
                    appState.navController.navigate(Route.Summary)
                }
            )
        }

        animatedComposable<Route.Summary> {
            SummaryRoute(
                onNavigateBack = {
                    appState.navController.popBackStack()
                },
                onOrderSuccess = {
                    appState.navController.navigate(Route.Success) {
                        popUpTo(Route.StoreDetail) {
                            saveState = false
                        }
                    }
                }
            )
        }

        animatedComposable<Route.Success> {
            OrderSuccessRoute(
                onBackToStore = {
                    appState.navController.navigate(Route.StoreDetail) {
                        popUpTo<Route.StoreDetail>()
                    }
                }
            )
        }
    }
}
