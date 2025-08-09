package com.opn.eshopify.util

import androidx.annotation.StringRes
import androidx.compose.runtime.Composable
import androidx.compose.ui.res.stringResource

/**
 * Represents a text value which can either be a raw string or a string resource.
 */
sealed interface TextValue {
    /**
     * A raw text value.
     *
     * @property value the raw string content.
     */
    data class Raw(val value: String) : TextValue
    /**
     * A text value represented by a string resource.
     *
     * @property resource the string resource to be used.
     */
    data class Resource(@StringRes val resource: Int) : TextValue
    /**
     * Converts the [TextValue] to a UI-ready string.
     *
     * For [Raw] instances, it returns the raw string.
     * For [Resource] instances, it resolves the string resource using
     * [stringResource].
     *
     * @return the string to be displayed in the UI.
     */
    @Composable
    fun toUiString(): String = when (this) {
        is Raw -> value
        is Resource -> stringResource(resource)
    }
}

fun String.toTextValue(): TextValue = TextValue.Raw(this)

fun Int.toTextValue(): TextValue = TextValue.Resource(this)
