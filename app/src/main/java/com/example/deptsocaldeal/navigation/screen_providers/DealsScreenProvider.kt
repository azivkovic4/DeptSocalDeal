package com.example.deptsocaldeal.navigation.screen_providers

import androidx.navigation.NavController
import androidx.navigation.NavGraphBuilder
import androidx.navigation.compose.composable
import com.example.deptsocaldeal.presentation.screens.SocialDealsScreen
import kotlinx.serialization.Serializable

@Serializable
data object Deals

fun NavGraphBuilder.socialDealsScreen(
    onNavigateToDetailsScreen: (String) -> Unit = {}
) {
    composable<Deals> {
        SocialDealsScreen(
            onNavigateToDetailsScreen = onNavigateToDetailsScreen
        )
    }
}

fun NavController.navigateToSocialDealsScreen() = navigate(Deals)
