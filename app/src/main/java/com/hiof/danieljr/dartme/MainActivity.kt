package com.danieljr.dartme

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import com.danieljr.dartme.ui.screens.LaunchApp
import com.danieljr.dartme.ui.theme.DartMeTheme

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            DartMeTheme {
                LaunchApp()
            }
        }
    }
}
