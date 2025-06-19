package com.example.deptsocaldeal.presentation.screens

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import com.example.deptsocaldeal.presentation.components.DealItem
import com.example.deptsocaldeal.presentation.viewmodels.FavoriteDealsAction
import com.example.deptsocaldeal.presentation.viewmodels.FavoriteDealsEffect
import com.example.deptsocaldeal.presentation.viewmodels.FavoriteDealsScreenState
import com.example.deptsocaldeal.presentation.viewmodels.FavoritesDealsViewModel
import com.example.deptsocaldeal.util.HandleEffects

@Composable
fun FavoriteScreen(
    onNavigateToDetailsScreen: (String) -> Unit = {},
    viewModel: FavoritesDealsViewModel = hiltViewModel()
) {
    val state by viewModel.screenState.collectAsState()

    HandleEffects(effectFlow = viewModel.effect) {
        when (it) {
            is FavoriteDealsEffect.NavigateToDetailsEffect -> {
                onNavigateToDetailsScreen(it.id)
            }
        }
    }
    UIContent(state, viewModel::setAction)
}

@Composable
private fun UIContent(state: FavoriteDealsScreenState, setAction: (FavoriteDealsAction) -> Unit) =
    Box(
        modifier = Modifier
            .padding(16.dp)
            .fillMaxSize(),
        contentAlignment = Alignment.Center
    ) {
        LazyColumn {
            if (state.result.isEmpty()) {
                item {
                    Text(
                        text = "No favorite deals found",
                        style = MaterialTheme.typography.titleLarge
                    )
                }
            } else {
                items(state.result, key = { it.id }) { deal ->
                    DealItem(
                        deal = deal,
                        onItemClicked = {
                            setAction(FavoriteDealsAction.NavigateToDealDetailsAction(deal.id))
                        },
                        onFavoriteClicked = {
                            setAction(FavoriteDealsAction.RemoveFavoriteDeal(deal, it))
                        }
                    )
                }
            }
        }
    }
