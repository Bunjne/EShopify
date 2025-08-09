package com.opn.eshopify.data.extension

import com.opn.eshopify.data.remote.mapper.toDataError
import com.opn.eshopify.domain.DataError
import com.opn.eshopify.domain.Result
import java.io.IOException
import java.net.SocketTimeoutException
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import retrofit2.HttpException

suspend inline fun <Data> safeApiCall(
    dispatcher: CoroutineDispatcher = Dispatchers.IO,
    crossinline call: suspend () -> Data
): Result<Data, DataError> {
    return withContext(dispatcher) {
        try {
            Result.Success(call())
        } catch (throwable: Throwable) {
            when (throwable) {
                is HttpException -> {
                    val errorCode = throwable.code().toDataError()
                    Result.Error(errorCode)
                }

                is SocketTimeoutException -> Result.Error(DataError.Network.Timeout)

                is IOException -> Result.Error(DataError.Network.NoInternet)

                else -> Result.Error(DataError.Network.Unknown, throwable)
            }
        }
    }
}
