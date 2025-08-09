package com.opn.eshopify.presentation.navigation

import androidx.compose.runtime.Composable
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import com.opn.eshopify.presentation.app.MainAppState
import com.opn.eshopify.presentation.store.StoreDetailRoute

@Composable
fun MainNavHost(
    appState: MainAppState,
    startDestination: Route = Route.StoreDetail
) {
    NavHost(
        navController = appState.navController,
        startDestination = startDestination
    ) {
        composable<Route.StoreDetail> {
            StoreDetailRoute(
                onCheckout = {}
            )
        }
    }
}
