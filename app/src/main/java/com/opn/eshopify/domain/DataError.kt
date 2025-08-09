package com.opn.eshopify.domain

sealed interface DataError : Error {

    enum class Network : DataError {
        BadRequest,
        Unauthorized,
        Timeout,
        NoInternet,
        Forbidden,
        NotFound,
        InternalServerError,
        ServiceUnavailable,
        Unknown
    }
}
