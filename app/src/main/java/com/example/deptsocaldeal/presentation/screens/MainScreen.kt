package com.example.deptsocaldeal.presentation.screens

import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Icon
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.navigation.NavDestination
import androidx.navigation.NavDestination.Companion.hasRoute
import androidx.navigation.NavDestination.Companion.hierarchy
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import com.example.deptsocaldeal.R
import com.example.deptsocaldeal.navigation.screen_providers.Deals
import com.example.deptsocaldeal.navigation.screen_providers.Favorites
import com.example.deptsocaldeal.navigation.screen_providers.Settings
import com.example.deptsocaldeal.navigation.screen_providers.favoritesScreen
import com.example.deptsocaldeal.navigation.screen_providers.navigateToFavoritesScreen
import com.example.deptsocaldeal.navigation.screen_providers.navigateToSettingsScreen
import com.example.deptsocaldeal.navigation.screen_providers.navigateToSocialDealsScreen
import com.example.deptsocaldeal.navigation.screen_providers.settingsScreen
import com.example.deptsocaldeal.navigation.screen_providers.socialDealsScreen

@Composable
fun MainScreen(
    onNavigateToDetailsScreen: (String) -> Unit = {},
) {
    val navController = rememberNavController()
    Scaffold(
        bottomBar = {
            MainBottomBar(
                hierarchy = navController.currentBackStackEntryAsState().value?.destination?.hierarchy,
                onNavigateToDeals = {
                    navController.navigateToSocialDealsScreen()
                },
                onNavigateToFavorites = {
                    navController.navigateToFavoritesScreen()
                },
                onNavigateToSettings = {
                    navController.navigateToSettingsScreen()
                }

            )
        }) { padding ->
        NavHost(
            modifier = Modifier.padding(padding),
            navController = navController,
            startDestination = Deals
        ) {
            socialDealsScreen(onNavigateToDetailsScreen = onNavigateToDetailsScreen)
            favoritesScreen(onNavigateToDetailsScreen = onNavigateToDetailsScreen)
            settingsScreen()
        }
    }
}

@Composable
private fun MainBottomBar(
    hierarchy: Sequence<NavDestination>?,
    onNavigateToDeals: () -> Unit,
    onNavigateToFavorites: () -> Unit,
    onNavigateToSettings: () -> Unit
) {
    NavigationBar {
        NavigationBarItem(
            selected = hierarchy?.any { it.hasRoute(Deals::class) } == true,
            icon = {
                Icon(painter = painterResource(R.drawable.ic_social_deals), contentDescription = "deals")
            },
            onClick = onNavigateToDeals
        )
        NavigationBarItem(
            selected = hierarchy?.any { it.hasRoute(Favorites::class) } == true,
            icon = {
                Icon(painter = painterResource(R.drawable.ic_favorites), contentDescription = "favorites")
            },
            onClick = onNavigateToFavorites
        )

        NavigationBarItem(
            selected = hierarchy?.any { it.hasRoute(Settings::class) } == true,
            icon = {
                Icon(painter = painterResource(R.drawable.ic_settings), contentDescription = "settings")
            },
            onClick = onNavigateToSettings
        )
    }
}
