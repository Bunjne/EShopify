package com.opn.eshopify.presentation.navigation

import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.navigation
import com.opn.eshopify.presentation.app.MainAppState
import com.opn.eshopify.presentation.orderSuccess.OrderSuccessRoute
import com.opn.eshopify.presentation.orderSummary.SummaryRoute
import com.opn.eshopify.presentation.store.StoreDetailRoute
import com.opn.eshopify.presentation.store.StoreDetailViewModel
import org.koin.androidx.compose.koinViewModel

@Composable
fun MainNavHost(
    appState: MainAppState,
    startDestination: Route = Route.Store
) {
    NavHost(
        navController = appState.navController,
        startDestination = startDestination
    ) {
        navigation<Route.Store>(
            startDestination = Route.StoreDetail
        ) {
            composable<Route.StoreDetail> {
                StoreDetailRoute(
                    onCheckout = {
                        appState.navController.navigate(Route.Summary)
                    }
                )
            }

            composable<Route.Summary> {
                SummaryRoute(
                    onNavigateBack = {
                        appState.navController.popBackStack()
                    },
                    onOrderSuccess = {
                        appState.navController.navigate(Route.Success) {
                            popUpTo(Route.Store) {
                                saveState = false
                            }
                        }
                    }
                )
            }
        }

        composable<Route.Success> {
            OrderSuccessRoute(
                onBackToStore = {
                    appState.navController.navigate(Route.Store) {
                        popUpTo(Route.Success) {
                            inclusive = true
                        }
                    }
                }
            )
        }
    }
}
