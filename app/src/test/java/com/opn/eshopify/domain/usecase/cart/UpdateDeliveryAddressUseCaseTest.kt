package com.opn.eshopify.domain.usecase.cart

import com.opn.eshopify.domain.repository.CartRepository
import io.mockk.mockk
import io.mockk.verify
import org.junit.Before
import org.junit.Test

class UpdateDeliveryAddressUseCaseTest {

    private lateinit var useCase: UpdateDeliveryAddressUseCase
    private lateinit var cartRepository: CartRepository

    @Before
    fun setup() {
        cartRepository = mockk(relaxed = true)
        useCase = UpdateDeliveryAddressUseCase(cartRepository)
    }

    @Test
    fun `invoke should call updateDeliveryAddress on repository`() {
        // Given
        val testAddress = "123 Test Street"

        // When
        useCase(testAddress)

        // Then
        verify(exactly = 1) { cartRepository.updateDeliveryAddress(testAddress) }
    }
}
