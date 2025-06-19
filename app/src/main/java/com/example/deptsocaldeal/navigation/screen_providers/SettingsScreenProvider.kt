package com.example.deptsocaldeal.navigation.screen_providers

import androidx.navigation.NavController
import androidx.navigation.NavGraphBuilder
import androidx.navigation.compose.composable
import com.example.deptsocaldeal.presentation.screens.SettingsScreen
import kotlinx.serialization.Serializable


@Serializable
data object Settings

fun NavGraphBuilder.settingsScreen() {
    composable<Settings> {
        SettingsScreen()
    }
}

fun NavController.navigateToSettingsScreen() = navigate(Settings)