package com.opn.eshopify.data.remote.mapper

import com.opn.eshopify.data.remote.model.ProductDto
import com.opn.eshopify.domain.model.Order
import com.opn.eshopify.domain.model.Product
import org.junit.Assert.assertEquals
import org.junit.Test

class OrderMapperTest {

    @Test
    fun `asDto should map Order to OrderDto`() {
        // Arrange
        val products = listOf(
            Product(
                id = "1",
                name = "Product 1",
                price = 10.0,
                imageUrl = "https://example.com/image1.jpg"
            ),
            Product(
                id = "2",
                name = "Product 2",
                price = 20.0,
                imageUrl = "https://example.com/image2.jpg"
            )
        )

        val order = Order(
            products = products,
            deliveryAddress = "123 Test Street, Test City"
        )

        // Act
        val orderDto = order.asDto()

        // Assert
        assertEquals(order.deliveryAddress, orderDto.deliveryAddress)
        assertEquals(order.products.size, orderDto.products.size)

        // Verify each product was mapped correctly
        order.products.forEachIndexed { index, product ->
            val productDto = orderDto.products[index]
            assertEquals(product.name, productDto.name)
            assertEquals(product.price, productDto.price, 0.001)
            assertEquals(product.imageUrl, productDto.imageUrl)
        }
    }

    @Test
    fun `asDto should handle empty product list`() {
        // Arrange
        val order = Order(
            products = emptyList(),
            deliveryAddress = "123 Test Street, Test City"
        )

        // Act
        val orderDto = order.asDto()

        // Assert
        assertEquals(order.deliveryAddress, orderDto.deliveryAddress)
        assertEquals(0, orderDto.products.size)
    }
}
