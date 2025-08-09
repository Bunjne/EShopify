package com.opn.eshopify.util

import com.opn.eshopify.R
import com.opn.eshopify.domain.DataError

fun DataError.asTextValue(): TextValue = when (this) {
    DataError.Network.BadRequest -> TextValue.Resource(R.string.error_bad_request)
    DataError.Network.Unauthorized -> TextValue.Resource(R.string.error_unauthorized)
    DataError.Network.Forbidden -> TextValue.Resource(R.string.error_forbidden)
    DataError.Network.NotFound -> TextValue.Resource(R.string.error_not_found)
    DataError.Network.InternalServerError -> TextValue.Resource(R.string.error_internal_server_error)
    DataError.Network.ServiceUnavailable -> TextValue.Resource(R.string.error_service_unavailable)
    DataError.Network.Timeout -> TextValue.Resource(R.string.error_timeout)
    DataError.Network.NoInternet -> TextValue.Resource(R.string.error_no_internet)
    DataError.Network.Unknown -> TextValue.Resource(R.string.error_unexpected)
}
