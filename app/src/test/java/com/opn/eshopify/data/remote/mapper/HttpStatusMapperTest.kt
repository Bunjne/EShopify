package com.opn.eshopify.data.remote.mapper

import com.opn.eshopify.domain.DataError
import org.junit.Assert.assertEquals
import org.junit.Test

class HttpStatusMapperTest {

    @Test
    fun `toDataError should map HTTP status codes to correct DataError types`() {
        assertEquals(DataError.Network.BadRequest, 400.toDataError())
        assertEquals(DataError.Network.Unauthorized, 401.toDataError())
        assertEquals(DataError.Network.Forbidden, 403.toDataError())
        assertEquals(DataError.Network.NotFound, 404.toDataError())
        assertEquals(DataError.Network.InternalServerError, 500.toDataError())
        assertEquals(DataError.Network.ServiceUnavailable, 503.toDataError())
    }

    @Test
    fun `toDataError should map undefined HTTP status codes to Unknown error`() {
        assertEquals(DataError.Network.Unknown, 402.toDataError())
        assertEquals(DataError.Network.Unknown, 405.toDataError())
        assertEquals(DataError.Network.Unknown, 409.toDataError())
        assertEquals(DataError.Network.Unknown, 422.toDataError())
        assertEquals(DataError.Network.Unknown, 501.toDataError())
        assertEquals(DataError.Network.Unknown, 502.toDataError())
        assertEquals(DataError.Network.Unknown, 504.toDataError())
    }

    @Test
    fun `toDataError should handle extreme values`() {
        assertEquals(DataError.Network.Unknown, (-1).toDataError())
        assertEquals(DataError.Network.Unknown, 0.toDataError())
        assertEquals(DataError.Network.Unknown, 999.toDataError())
        assertEquals(DataError.Network.Unknown, Int.MAX_VALUE.toDataError())
        assertEquals(DataError.Network.Unknown, Int.MIN_VALUE.toDataError())
    }
}
