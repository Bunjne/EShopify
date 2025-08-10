package com.opn.eshopify.presentation.designsystem.button

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.defaultMinSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonColors
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.ButtonElevation
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import com.opn.eshopify.R
import com.opn.eshopify.presentation.designsystem.indicator.GeneralLoadingIndicator
import com.opn.eshopify.presentation.util.TextValue

@Composable
fun GeneralTextButton(
    onClicked: () -> Unit,
    text: String,
    modifier: Modifier = Modifier,
    isEnabled: Boolean = true,
    isLoading: Boolean = false,
    colors: ButtonColors = ButtonDefaults.buttonColors(
        containerColor = MaterialTheme.colorScheme.primary,
        disabledContainerColor = MaterialTheme.colorScheme.surfaceVariant
    ),
    contentPadding: PaddingValues = PaddingValues(horizontal = 16.dp, vertical = 8.dp),
    shape: RoundedCornerShape = RoundedCornerShape(8.dp),
    interactionSource: MutableInteractionSource? = null,
    elevation: ButtonElevation? = ButtonDefaults.buttonElevation(),
    border: BorderStroke? = null,
) {
    Button(
        onClick = onClicked,
        enabled = isEnabled,
        modifier = modifier,
        colors = colors,
        shape = shape,
        contentPadding = contentPadding,
        interactionSource = interactionSource,
        elevation = elevation,
        border = border,
    ) {
        if (isLoading) {
            GeneralLoadingIndicator(modifier = Modifier.size(16.dp))
        } else {
            Text(text = text)
        }
    }
}
