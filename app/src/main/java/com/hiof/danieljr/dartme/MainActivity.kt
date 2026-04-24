package com.hiof.danieljr.dartme

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import com.hiof.danieljr.dartme.ui.screens.DartInputScreen
import com.hiof.danieljr.dartme.ui.screens.LaunchApp
import com.hiof.danieljr.dartme.ui.screens.TestShimmerScreen
import com.hiof.danieljr.dartme.ui.theme.DartMeTheme

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

