package com.opn.eshopify.domain.usecase.cart

import com.opn.eshopify.domain.repository.CartRepository
import io.mockk.mockk
import io.mockk.verify
import org.junit.Before
import org.junit.Test

class ResetCartUseCaseTest {

    private lateinit var useCase: ResetCartUseCase
    private lateinit var cartRepository: CartRepository

    @Before
    fun setup() {
        cartRepository = mockk(relaxed = true)
        useCase = ResetCartUseCase(cartRepository)
    }

    @Test
    fun `invoke should call reset on repository`() {
        // When
        useCase()

        // Then
        verify(exactly = 1) { cartRepository.reset() }
    }
}
