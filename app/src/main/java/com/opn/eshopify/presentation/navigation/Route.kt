package com.opn.eshopify.presentation.navigation

import kotlinx.serialization.Serializable

sealed interface Route {

    @Serializable
    data object StoreDetail : Route

    @Serializable
    data object Success : Route

    @Serializable
    data object Summary : Route
}
