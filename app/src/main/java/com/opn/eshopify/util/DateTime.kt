package com.opn.eshopify.util

import kotlinx.datetime.LocalTime
import kotlinx.datetime.format
import kotlinx.datetime.format.char

fun LocalTime.formatTime24Hour(): String {
    val formatter = LocalTime.Format {
        hour(); char(':'); minute()
    }
    return this.format(formatter)
}
