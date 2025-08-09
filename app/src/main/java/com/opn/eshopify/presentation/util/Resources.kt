package com.opn.eshopify.presentation.util

import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.res.vectorResource

@Composable
fun Int.asImageVector() = ImageVector.vectorResource(this)
