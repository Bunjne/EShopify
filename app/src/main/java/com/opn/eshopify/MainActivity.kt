package com.opn.eshopify

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import com.opn.eshopify.presentation.app.MainApp
import com.opn.eshopify.presentation.theme.EShopifyTheme

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            EShopifyTheme {
                MainApp()
            }
        }
    }
}

