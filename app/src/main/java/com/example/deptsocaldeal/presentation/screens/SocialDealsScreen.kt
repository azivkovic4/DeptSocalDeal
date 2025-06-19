package com.example.deptsocaldeal.presentation.screens

import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.hilt.navigation.compose.hiltViewModel
import com.example.deptsocaldeal.presentation.components.DealItem
import com.example.deptsocaldeal.presentation.components.InternetAvailabilitySnackBar
import com.example.deptsocaldeal.presentation.viewmodels.SocialDealsAction
import com.example.deptsocaldeal.presentation.viewmodels.SocialDealsEffect
import com.example.deptsocaldeal.presentation.viewmodels.SocialDealsScreenState
import com.example.deptsocaldeal.presentation.viewmodels.SocialDealsViewModel
import com.example.deptsocaldeal.util.ErrorHandlerComp
import com.example.deptsocaldeal.util.HandleEffects

@Composable
fun SocialDealsScreen(
    onNavigateToDetailsScreen: (String) -> Unit = {},
    viewModel: SocialDealsViewModel = hiltViewModel()
) {
    val state by viewModel.screenState.collectAsState()

    HandleEffects(effectFlow = viewModel.effect) {
        when (it) {
            is SocialDealsEffect.NavigateToDetailsEffect -> {
                onNavigateToDetailsScreen(it.id)
            }
        }
    }
    UIContent(state, viewModel::setAction)
    InternetAvailabilitySnackBar(status = state.hasInternet)
}

@Composable
private fun UIContent(state: SocialDealsScreenState, setAction: (SocialDealsAction) -> Unit) =
    ErrorHandlerComp(
        result = state.result,
        loadingContent = {
            CircularProgressIndicator()
        },
        successContent = {
            LazyColumn {
                items(it, key = { it.id }) { deal ->
                    DealItem(
                        deal = deal,
                        onItemClicked = {
                            setAction(SocialDealsAction.NavigateToDealDetailsAction(deal.id))
                        },
                        onFavoriteClicked = {
                            setAction(SocialDealsAction.ToggleFavoriteAction(deal, it))
                        }
                    )
                }
            }
        },
        errorContent = {
            Text(text = "Error: $it")
        }
    )

