package com.opn.eshopify.domain.usecase.cart

import com.opn.eshopify.domain.model.Product
import com.opn.eshopify.domain.repository.CartRepository
import io.mockk.mockk
import io.mockk.verify
import org.junit.Before
import org.junit.Test

class RemoveProductFromCartUseCaseTest {

    private lateinit var useCase: RemoveProductFromCartUseCase
    private lateinit var cartRepository: CartRepository

    private val testProduct = Product(
        id = "1",
        name = "Test Product",
        price = 100.0,
        imageUrl = "test_url"
    )

    @Before
    fun setup() {
        cartRepository = mockk(relaxed = true)
        useCase = RemoveProductFromCartUseCase(cartRepository)
    }

    @Test
    fun `invoke should call decrementProductQuantity on repository`() {
        // When
        useCase(testProduct)

        // Then
        verify(exactly = 1) { cartRepository.decrementProductQuantity(testProduct) }
    }
}
