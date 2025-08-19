package com.opn.eshopify.data.remote.mapper

import com.opn.eshopify.data.remote.model.ProductDto
import com.opn.eshopify.data.remote.model.ProductResponse
import com.opn.eshopify.domain.model.Product
import org.junit.Assert.assertEquals
import org.junit.Assert.assertFalse
import org.junit.Assert.assertTrue
import org.junit.Test

class ProductMapperTest {

    @Test
    fun `asDomain should map ProductDto to Product correctly`() {
        // Given
        val productDto = ProductDto(
            id = 1,
            name = "Test Product",
            price = 99.99,
            imageUrl = "https://example.com/image.jpg"
        )

        // When
        val product = productDto.asDomain()

        // Then
        assert(productDto.id.toString() == product.id)
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
        assert(product.id == productDto.id.toString())
        assertEquals(product.name, productDto.name)
        assertEquals(product.price, productDto.price, 0.001) // Using delta for double comparison
        assertEquals(product.imageUrl, productDto.imageUrl)
    }

    @Test
    fun `ProductResponse asDomain should map correctly with pagination info`() {
        // Given
        val productDtos = listOf(
            ProductDto(id = 1, name = "Product 1", price = 10.0, imageUrl = "url1"),
            ProductDto(id = 2, name = "Product 2", price = 20.0, imageUrl = "url2")
        )

        val paginationInfo = ProductResponse.PaginationInfo(
            currentPage = 1,
            totalPages = 3,
            totalCount = 50,
        )

        val productResponse = ProductResponse(
            data = ProductResponse.ProductData(
                productResult = ProductResponse.ProductResult(
                    products = productDtos,
                    pagination = paginationInfo
                ),
            )
        )

        // When
        val paginatedProducts = productResponse.asDomain()

        // Then
        assertEquals(2, paginatedProducts.items.size)
        assertEquals("1", paginatedProducts.items[0].id)
        assertEquals("Product 1", paginatedProducts.items[0].name)
        assertEquals(1, paginatedProducts.currentPage)
        assertEquals(3, paginatedProducts.totalPages)
        assertEquals(50, paginatedProducts.totalCount)
        assertTrue(paginatedProducts.hasNextPage)
    }

    @Test
    fun `ProductResponse asDomain should handle last page correctly`() {
        // Given
        val productDtos = listOf(
            ProductDto(id = 1, name = "Product 1", price = 10.0, imageUrl = "url1")
        )

        val paginationInfo = ProductResponse.PaginationInfo(
            currentPage = 3,
            totalPages = 3,
            totalCount = 50,
        )

        val productResponse = ProductResponse(
            data = ProductResponse.ProductData(
                productResult = ProductResponse.ProductResult(
                    products = productDtos,
                    pagination = paginationInfo
                ),

                )
        )

        // When
        val paginatedProducts = productResponse.asDomain()

        // Then
        assertEquals(3, paginatedProducts.currentPage)
        assertEquals(3, paginatedProducts.totalPages)
        assertFalse(paginatedProducts.hasNextPage)
    }
}
