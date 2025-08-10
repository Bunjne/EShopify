package com.opn.eshopify.data.repository

import com.opn.eshopify.domain.model.Product
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.launch
import kotlinx.coroutines.test.UnconfinedTestDispatcher
import kotlinx.coroutines.test.advanceUntilIdle
import kotlinx.coroutines.test.runTest
import org.junit.Assert.assertEquals
import org.junit.Assert.assertFalse
import org.junit.Assert.assertTrue
import org.junit.Before
import org.junit.Test

@ExperimentalCoroutinesApi
class DefaultCartRepositoryTest {

    private lateinit var cartRepository: DefaultCartRepository
    private val testProduct1 =
        Product(id = "1", name = "Test Product 1", price = 10.0, imageUrl = "url1")
    private val testProduct2 =
        Product(id = "2", name = "Test Product 2", price = 20.0, imageUrl = "url2")

    @Before
    fun setup() {
        cartRepository = DefaultCartRepository()
    }

    @Test
    fun `incrementProductQuantity should add product with quantity 1 when not in cart`() = runTest {
        // When
        cartRepository.incrementProductQuantity(testProduct1)

        // Then
        val selectedProducts = cartRepository.selectedProducts.first()
        assertEquals(1, selectedProducts.size)
        assertEquals(1, selectedProducts[testProduct1])
    }

    @Test
    fun `incrementProductQuantity should increase quantity when product already in cart`() =
        runTest {
            // Given
            cartRepository.incrementProductQuantity(testProduct1)

            // When
            cartRepository.incrementProductQuantity(testProduct1)

            // Then
            val selectedProducts = cartRepository.selectedProducts.first()
            assertEquals(1, selectedProducts.size)
            assertEquals(2, selectedProducts[testProduct1])
        }

    @Test
    fun `decrementProductQuantity should remove product when quantity is 1`() = runTest {
        // Given
        cartRepository.incrementProductQuantity(testProduct1)

        // When
        cartRepository.decrementProductQuantity(testProduct1)

        // Then
        val selectedProducts = cartRepository.selectedProducts.first()
        assertEquals(0, selectedProducts.size)
        assertFalse(selectedProducts.containsKey(testProduct1))
    }

    @Test
    fun `decrementProductQuantity should decrease quantity when product quantity is greater than 1`() =
        runTest {
            // Given
            cartRepository.incrementProductQuantity(testProduct1)
            cartRepository.incrementProductQuantity(testProduct1)

            // When
            cartRepository.decrementProductQuantity(testProduct1)

            // Then
            val selectedProducts = cartRepository.selectedProducts.first()
            assertEquals(1, selectedProducts.size)
            assertEquals(1, selectedProducts[testProduct1])
        }

    @Test
    fun `decrementProductQuantity should do nothing when product not in cart`() = runTest {
        // When
        cartRepository.decrementProductQuantity(testProduct1)

        // Then
        val selectedProducts = cartRepository.selectedProducts.first()
        assertEquals(0, selectedProducts.size)
    }

    @Test
    fun `updateDeliveryAddress should update the address`() = runTest {
        // Given
        val address = "123 Test Street"

        // When
        cartRepository.updateDeliveryAddress(address)

        // Then
        assertEquals(address, cartRepository.deliveryAddress.first())
    }

    @Test
    fun `reset should clear selected products and delivery address`() = runTest {
        // Given
        cartRepository.incrementProductQuantity(testProduct1)
        cartRepository.updateDeliveryAddress("123 Test Street")

        // When
        cartRepository.reset()

        // Then
        val selectedProducts = cartRepository.selectedProducts.first()
        val deliveryAddress = cartRepository.deliveryAddress.first()

        assertEquals(0, selectedProducts.size)
        assertEquals("", deliveryAddress)
    }

    @Test
    fun `incrementing multiple distinct products should track each with correct quantity`() =
        runTest {
            // When
            cartRepository.incrementProductQuantity(testProduct1)
            cartRepository.incrementProductQuantity(testProduct2)
            cartRepository.incrementProductQuantity(testProduct2)

            // Then
            val selected = cartRepository.selectedProducts.first()
            assertEquals(2, selected.size)
            assertEquals(1, selected[testProduct1])
            assertEquals(2, selected[testProduct2])
        }

    @Test
    fun `selectedProducts should emit on each change including reset`() = runTest(
        UnconfinedTestDispatcher()
    ) {
        // Given
        val emissions = mutableListOf<Map<Product, Int>>()
        val job = launch {
            cartRepository.selectedProducts.collect { emissions.add(it) }
        }

        // When
        cartRepository.incrementProductQuantity(testProduct1)
        cartRepository.incrementProductQuantity(testProduct2)
        cartRepository.decrementProductQuantity(testProduct1)
        cartRepository.reset()

        // Then
        // Initial empty + 4 updates => 5 emissions total
        assertEquals(5, emissions.size)
        // Last emission after reset should be empty
        assertTrue(emissions.last().isEmpty())

        job.cancel()
    }
}
