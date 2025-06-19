package com.example.deptsocaldeal.navigation.screen_providers

import androidx.navigation.NavController
import androidx.navigation.NavGraphBuilder
import androidx.navigation.compose.composable
import com.example.deptsocaldeal.presentation.screens.FavoriteScreen
import kotlinx.serialization.Serializable

@Serializable
data object Favorites

fun NavGraphBuilder.favoritesScreen(
    onNavigateToDetailsScreen: (String) -> Unit = {}
) {
    composable<Favorites> {
        FavoriteScreen(
            onNavigateToDetailsScreen = onNavigateToDetailsScreen
        )
    }
}

fun NavController.navigateToFavoritesScreen() = navigate(Favorites)
