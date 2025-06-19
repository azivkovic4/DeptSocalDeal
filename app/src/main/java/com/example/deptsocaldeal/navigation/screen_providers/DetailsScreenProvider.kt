package com.example.deptsocaldeal.navigation.screen_providers

import androidx.navigation.NavController
import androidx.navigation.NavGraphBuilder
import androidx.navigation.compose.composable
import com.example.deptsocaldeal.presentation.screens.DetailsScreen
import kotlinx.serialization.Serializable

@Serializable
data class Details(val id:String)

fun NavGraphBuilder.detailsScreen(onBack: () -> Unit = {}) {
    composable<Details> {
        DetailsScreen(
            id = it.id,
            onNavigateBack = onBack
        )
    }
}

fun NavController.navigateToDetailsScreen(id: String) = navigate(Details(id))

