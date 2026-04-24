package com.hiof.danieljr.dartme.ui.screens

import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.hiof.danieljr.dartme.navigation.Screen

@Composable
fun LaunchApp() {
    val navController = rememberNavController()
    // Vi lagrer spillernavnene her slik at vi kan sende dem til GameScreen
    var sharedPlayerNames by remember { mutableStateOf(listOf<String>()) }

    Scaffold { innerPadding ->
        NavHost(
            navController = navController,
            startDestination = Screen.Splash.route,
            modifier = Modifier.padding(innerPadding)
        ) {
            composable(Screen.Splash.route) {
                SplashScreen(onTimeout = {
                    navController.navigate(Screen.Setup.route) {
                        // Fjern splash fra backstack så man ikke går tilbake til den
                        popUpTo(Screen.Splash.route) { inclusive = true }
                    }
                })
            }

            composable(Screen.Setup.route) {
                DartInputScreen(onStartGame = { names ->
                    sharedPlayerNames = names
                    navController.navigate(Screen.Game.route)
                })
            }
            
            composable(Screen.Game.route) {
                DartGameScreen(
                    playerNames = sharedPlayerNames,
                    onBack = {
                        navController.popBackStack(Screen.Setup.route, inclusive = false)
                    }
                )
            }
        }
    }
}
