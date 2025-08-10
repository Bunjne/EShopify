package com.opn.eshopify.data.remote.mapper

import com.opn.eshopify.data.remote.model.ProductDto
import com.opn.eshopify.domain.model.Product
import org.junit.Assert.assertEquals
import org.junit.Test

class ProductMapperTest {

    @Test
    fun `asDomain should map ProductDto to Product correctly`() {
        // Given
        val productId = "123"
        val productDto = ProductDto(
            name = "Test Product",
            price = 99.99,
            imageUrl = "https://example.com/image.jpg"
        )

        // When
        val product = productDto.asDomain(productId)

        // Then
        assertEquals(productId, product.id)
        assertEquals(productDto.name, product.name)
        assertEquals(productDto.price, product.price, 0.001) // Using delta for double comparison
        assertEquals(productDto.imageUrl, product.imageUrl)
    }

    @Test
    fun `asDto should map Product to ProductDto correctly`() {
        // Given
        val product = Product(
            id = "123",
            name = "Test Product",
            price = 99.99,
            imageUrl = "https://example.com/image.jpg"
        )

        // When
        val productDto = product.asDto()

        // Then
        assertEquals(product.name, productDto.name)
        assertEquals(product.price, productDto.price, 0.001) // Using delta for double comparison
        assertEquals(product.imageUrl, productDto.imageUrl)
    }
}
