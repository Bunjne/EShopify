package com.opn.eshopify.presentation.store.components

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.scale
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.text.style.TextAlign
import com.opn.eshopify.R
import com.opn.eshopify.util.asImageVector

@Composable
fun QuantityControls(
    modifier: Modifier = Modifier,
    quantity: Int,
    onIncrement: () -> Unit,
    onDecrement: () -> Unit
) {
    val isMoreThanOne by remember(quantity) {
        derivedStateOf {
            quantity > 1
        }
    }

    Row(
        modifier = modifier,
        verticalAlignment = Alignment.CenterVertically
    ) {
        if (quantity > 0) {
            PilledButton(
                icon = if (isMoreThanOne) R.drawable.ic_minus.asImageVector() else Icons.Default.Delete,
                onClick = onDecrement,
                contentDescription = "Decrement quantity"
            )
            Text(
                text = quantity.toString(),
                style = MaterialTheme.typography.titleMedium,
                textAlign = TextAlign.Center
            )
        }

        PilledButton(
            icon = Icons.Default.Add,
            onClick = onIncrement,
            contentDescription = "Increment quantity"
        )
    }
}

@Composable
private fun PilledButton(
    icon: ImageVector,
    contentDescription: String?,
    onClick: () -> Unit,
) {
    IconButton(
        onClick = onClick,
        modifier = Modifier
            .scale(0.6f)
            .background(
                color = MaterialTheme.colorScheme.onPrimaryContainer,
                shape = CircleShape
            )
    ) {
        Icon(
            imageVector = icon,
            contentDescription = contentDescription,
            tint = MaterialTheme.colorScheme.primaryContainer,
        )
    }
}

