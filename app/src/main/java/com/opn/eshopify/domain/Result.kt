package com.opn.eshopify.domain

typealias RootError = Error

sealed interface Result<out Data, out Error : RootError> {

    data class Success<out Data, out Error : RootError>(val data: Data) : Result<Data, Error>

    data class Error<out Data, out Error : RootError>(
        val error: Error,
        val throwable: Throwable? = null
    ) : Result<Data, Error>
}
