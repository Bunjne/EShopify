package com.opn.eshopify.presentation.orderSummary.components

import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import com.opn.eshopify.R
import com.opn.eshopify.presentation.designsystem.button.GeneralTextButton

@Composable
fun PlaceOrderBottomBar(
    onPlaceOrderClicked: () -> Unit,
    isLoading: Boolean,
    isEnabled: Boolean,
    modifier: Modifier = Modifier
) {
    Surface(
        color = MaterialTheme.colorScheme.surface,
        shadowElevation = 16.dp
    ) {
        GeneralTextButton(
            modifier = modifier
                .fillMaxWidth()
                .padding(8.dp)
                .testTag("place_order_button"),
            onClicked = onPlaceOrderClicked,
            text = stringResource(R.string.place_order),
            isEnabled = isEnabled,
            isLoading = isLoading,
        )
    }
}
