package com.example.deptsocaldeal.navigation.screen_providers

import androidx.navigation.NavGraphBuilder
import androidx.navigation.compose.composable
import com.example.deptsocaldeal.presentation.screens.MainScreen
import kotlinx.serialization.Serializable

@Serializable
data object Main

fun NavGraphBuilder.mainScreen(
    onNavigateToDetailsScreen: (String) -> Unit = { }
) {
    composable<Main> {
        MainScreen(
            onNavigateToDetailsScreen = onNavigateToDetailsScreen
        )
    }
}
