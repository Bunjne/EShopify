package com.opn.eshopify.data.remote.mapper

import com.opn.eshopify.domain.DataError

fun Int.toDataError(): DataError = when (this) {
    400 -> DataError.Network.BadRequest
    401 -> DataError.Network.Unauthorized
    403 -> DataError.Network.Forbidden
    404 -> DataError.Network.NotFound
    500 -> DataError.Network.InternalServerError
    503 -> DataError.Network.ServiceUnavailable
    else -> DataError.Network.Unknown
}
