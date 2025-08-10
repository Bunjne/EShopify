package com.opn.eshopify.domain.usecase.cart

import com.opn.eshopify.domain.model.Cart
import com.opn.eshopify.domain.model.Product
import com.opn.eshopify.domain.repository.CartRepository
import io.mockk.every
import io.mockk.mockk
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.test.runTest
import org.junit.Assert.assertEquals
import org.junit.Before
import org.junit.Test

@ExperimentalCoroutinesApi
class GetCartDetailUseCaseTest {

    private lateinit var useCase: GetCartDetailUseCase
    private lateinit var cartRepository: CartRepository

    private val testProduct1 =
        Product(id = "1", name = "Product 1", price = 10.0, imageUrl = "url1")
    private val testProduct2 =
        Product(id = "2", name = "Product 2", price = 20.0, imageUrl = "url2")
    private val testSelectedProducts = mapOf(testProduct1 to 1, testProduct2 to 2)
    private val testAddress = "123 Test Street"

    private val selectedProductsFlow = MutableStateFlow(testSelectedProducts)
    private val deliveryAddressFlow = MutableStateFlow(testAddress)

    @Before
    fun setup() {
        cartRepository = mockk {
            every { selectedProducts } returns selectedProductsFlow
            every { deliveryAddress } returns deliveryAddressFlow
        }

        useCase = GetCartDetailUseCase(cartRepository)
    }

    @Test
    fun `invoke should return cart with correct total price and hasProducts flag when products exist`() =
        runTest {
            // When
            val result = useCase().first()

            // Then
            val expectedTotalPrice = 10.0 * 1 + 20.0 * 2 // 50.0
            assertEquals(testSelectedProducts, result.products)
            assertEquals(testAddress, result.deliveryAddress)
            assertEquals(expectedTotalPrice, result.totalPrice, 0.01)
            assertEquals(true, result.hasProducts)
        }

    @Test
    fun `invoke should return cart with zero total price and false hasProducts flag when no products`() =
        runTest {
            // Given
            selectedProductsFlow.value = emptyMap()

            // When
            val result = useCase().first()

            // Then
            assertEquals(emptyMap<Product, Int>(), result.products)
            assertEquals(testAddress, result.deliveryAddress)
            assertEquals(0.0, result.totalPrice, 0.01)
            assertEquals(false, result.hasProducts)
        }

    @Test
    fun `invoke should calculate correct total price with multiple quantities`() = runTest {
        // Given
        val product1 = Product(id = "1", name = "Product 1", price = 10.0, imageUrl = "url1")
        val product2 = Product(id = "2", name = "Product 2", price = 20.0, imageUrl = "url2")
        val product3 = Product(id = "3", name = "Product 3", price = 30.0, imageUrl = "url3")

        val products = mapOf(
            product1 to 2,  // 20.0
            product2 to 3,  // 60.0
            product3 to 1   // 30.0
        )

        selectedProductsFlow.value = products

        // When
        val result = useCase().first()

        // Then
        val expectedTotalPrice = 20.0 + 60.0 + 30.0 // 110.0
        assertEquals(products, result.products)
        assertEquals(expectedTotalPrice, result.totalPrice, 0.01)
    }

    @Test
    fun `invoke should update cart when repository flows emit new values`() = runTest {
        // Given - Initial state
        val initialCart = useCase().first()
        assertEquals(testSelectedProducts, initialCart.products)
        assertEquals(testAddress, initialCart.deliveryAddress)

        // When - Update products
        val newProducts = mapOf(testProduct1 to 3)
        selectedProductsFlow.value = newProducts

        // Then - Cart should reflect new products
        val cartAfterProductUpdate = useCase().first()
        assertEquals(newProducts, cartAfterProductUpdate.products)
        assertEquals(30.0, cartAfterProductUpdate.totalPrice, 0.01) // 10.0 * 3

        // When - Update address
        val newAddress = "456 New Street"
        deliveryAddressFlow.value = newAddress

        // Then - Cart should reflect new address
        val cartAfterAddressUpdate = useCase().first()
        assertEquals(newAddress, cartAfterAddressUpdate.deliveryAddress)
    }
}
