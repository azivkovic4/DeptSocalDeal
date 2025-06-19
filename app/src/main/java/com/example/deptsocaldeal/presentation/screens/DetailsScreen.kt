package com.example.deptsocaldeal.presentation.screens

import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.hilt.navigation.compose.hiltViewModel
import com.example.deptsocaldeal.presentation.components.DealItem
import com.example.deptsocaldeal.presentation.viewmodels.DealDetailsAction
import com.example.deptsocaldeal.presentation.viewmodels.DealDetailsEffect
import com.example.deptsocaldeal.presentation.viewmodels.DealDetailsScreenState
import com.example.deptsocaldeal.presentation.viewmodels.DetailsViewModel
import com.example.deptsocaldeal.util.ErrorHandlerComp
import com.example.deptsocaldeal.util.HandleEffects

@Composable
fun DetailsScreen(
    id: String,
    onNavigateBack: () -> Unit = {},
    viewModel: DetailsViewModel = hiltViewModel()
) {
    val state by viewModel.screenState.collectAsState()

    LaunchedEffect(id) {
        viewModel.setData(id)
    }

    HandleEffects(effectFlow = viewModel.effect) {
        when (it) {
            is DealDetailsEffect.OnBackEffect -> {
                onNavigateBack()
            }
        }
    }
    UIContent(state, viewModel::setAction)
}

@Composable
private fun UIContent(state: DealDetailsScreenState, setAction: (DealDetailsAction) -> Unit) =
    ErrorHandlerComp(
        result = state.result,
        loadingContent = {
            CircularProgressIndicator()
        },
        successContent = {deal->
            DealItem(
                deal = deal,
                onFavoriteClicked = {
                    setAction(DealDetailsAction.ToggleFavoriteDeal(deal, it))
                }
            )
        },
        errorContent = {
            Text(text = "Error: $it")
        }
    )
