package com.opn.eshopify.data.extension

import com.opn.eshopify.domain.DataError
import com.opn.eshopify.domain.Result

suspend inline fun <reified Data> safeApiCall(call: suspend () -> Data): Result<Data, DataError> {
    return try {
        Result.Success(call())
    } catch (e: Exception) {
        Result.Error(DataError.Network.NotFound, e)
    }
}
