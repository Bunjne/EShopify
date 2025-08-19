package com.opn.eshopify.data.extension

import android.content.Context
import kotlinx.serialization.json.Json

inline fun <reified Data> Json.jsonFileToModel(context: Context, fileName: String): Data {
    val jsonString = context.assets.open(fileName).bufferedReader().use { it.readText() }
    return decodeFromString<Data>(jsonString)
}
