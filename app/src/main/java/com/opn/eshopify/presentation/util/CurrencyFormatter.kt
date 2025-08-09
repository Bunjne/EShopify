package com.opn.eshopify.presentation.util

import java.text.NumberFormat
import java.util.Locale

fun formatCurrency(amount: Double, locale: Locale = Locale("th", "TH")): String {
    val formatter = NumberFormat.getCurrencyInstance(locale)
    return formatter.format(amount)
}
