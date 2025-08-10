package com.opn.eshopify.presentation.app

import androidx.compose.foundation.background
import androidx.compose.foundation.gestures.detectTapGestures
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.WindowInsetsSides
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.only
import androidx.compose.foundation.layout.safeDrawing
import androidx.compose.foundation.layout.windowInsetsPadding
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.platform.LocalFocusManager
import com.opn.eshopify.presentation.navigation.MainNavHost

@Composable
fun MainApp(
    appState: MainAppState = rememberMainAppState()
) {
    val localFocusManager = LocalFocusManager.current

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(MaterialTheme.colorScheme.background)
            .windowInsetsPadding(
                WindowInsets.safeDrawing.only(
                    WindowInsetsSides.Horizontal, // Top and bottom would be handled by each specific screen
                ),
            )
            .pointerInput(Unit) {
                detectTapGestures(
                    onTap = {
                        localFocusManager.clearFocus()
                    }
                )
            },
    ) {
        MainNavHost(
            appState = appState,
        )
    }
}
