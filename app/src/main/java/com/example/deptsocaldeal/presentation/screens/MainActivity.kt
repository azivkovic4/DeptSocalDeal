package com.example.deptsocaldeal.presentation.screens

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.rememberNavController
import com.example.deptsocaldeal.navigation.screen_providers.Main
import com.example.deptsocaldeal.navigation.screen_providers.detailsScreen
import com.example.deptsocaldeal.navigation.screen_providers.mainScreen
import com.example.deptsocaldeal.navigation.screen_providers.navigateToDetailsScreen
import com.example.deptsocaldeal.ui.theme.DeptSocalDealTheme
import com.example.deptsocaldeal.util.LocalSnackBarHostState
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            DeptSocalDealTheme {
                val snackBarHostState = remember {
                    SnackbarHostState()
                }
                CompositionLocalProvider(LocalSnackBarHostState provides snackBarHostState) {
                    Scaffold(
                        modifier = Modifier.fillMaxSize(),
                        snackbarHost = {
                            SnackbarHost(snackBarHostState)
                        }
                    ) { innerPadding ->
                        AppRoot(modifier = Modifier.padding(innerPadding))
                    }
                }
            }
        }
    }
}

@Composable
fun AppRoot(modifier: Modifier = Modifier) {
    val navController = rememberNavController()
    NavHost(
        modifier = modifier,
        navController = navController,
        startDestination = Main,
    ) {
        detailsScreen(onBack = { navController.navigateUp() })
        mainScreen(onNavigateToDetailsScreen = { navController.navigateToDetailsScreen(it) })
    }
}
