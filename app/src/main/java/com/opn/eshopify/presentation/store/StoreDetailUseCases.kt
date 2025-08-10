package com.opn.eshopify.presentation.store

import com.opn.eshopify.domain.usecase.store.GetProductsUseCase
import com.opn.eshopify.domain.usecase.store.GetStoreDetailUseCase
import com.opn.eshopify.domain.usecase.cart.AddProductToCartUseCase
import com.opn.eshopify.domain.usecase.cart.GetCartDetailUseCase
import com.opn.eshopify.domain.usecase.cart.RemoveProductFromCartUseCase

class StoreDetailUseCases(
    val getStoreDetail: GetStoreDetailUseCase,
    val getProducts: GetProductsUseCase,
    val addProductToCart: AddProductToCartUseCase,
    val removeProductFromCart: RemoveProductFromCartUseCase,
    val getCartDetail: GetCartDetailUseCase,
)
