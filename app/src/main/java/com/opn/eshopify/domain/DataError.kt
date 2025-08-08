package com.opn.eshopify.domain

sealed interface DataError : Error {

    enum class Network : DataError {
        BadRequest,
        Timeout,
        NoInternet,
        Forbidden,
        NotFound,
        Unknown
    }
}
