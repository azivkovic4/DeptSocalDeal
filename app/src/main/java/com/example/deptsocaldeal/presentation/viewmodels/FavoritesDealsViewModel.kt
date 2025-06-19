package com.example.deptsocaldeal.presentation.viewmodels

import androidx.lifecycle.viewModelScope
import com.example.deptsocaldeal.data.models.DealPresentation
import com.example.deptsocaldeal.domain.Repository
import com.example.deptsocaldeal.util.Action
import com.example.deptsocaldeal.util.BaseViewModel
import com.example.deptsocaldeal.util.Effect
import com.example.deptsocaldeal.util.ScreenState
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import javax.inject.Inject

@HiltViewModel
class FavoritesDealsViewModel @Inject constructor(
    private val dealsRepository: Repository
) : BaseViewModel<FavoriteDealsScreenState, FavoriteDealsAction, FavoriteDealsEffect>() {

    init {
        fetchFavoriteDeals()
    }

    private fun fetchFavoriteDeals() {
        dealsRepository.favoriteDeals().onEach { result ->
            setScreenState {
                copy(result = result)
            }
        }.launchIn(viewModelScope)
    }

    override fun createInitialScreenState() = FavoriteDealsScreenState()

    override suspend fun handleActions(action: FavoriteDealsAction) {
        when (action) {
            is FavoriteDealsAction.NavigateToDealDetailsAction -> {
                setEffect {
                    FavoriteDealsEffect.NavigateToDetailsEffect(action.id)
                }
            }

            is FavoriteDealsAction.RemoveFavoriteDeal -> {
                dealsRepository.addOrRemoveFromFavorites(deal = action.deal, action.isFavorite)
            }
        }
    }
}

data class FavoriteDealsScreenState(
    val result: List<DealPresentation> = emptyList()
) : ScreenState

sealed class FavoriteDealsAction : Action {
    data class NavigateToDealDetailsAction(val id: String) : FavoriteDealsAction()
    data class RemoveFavoriteDeal(val deal: DealPresentation, val isFavorite: Boolean = false) :
        FavoriteDealsAction()
}

sealed class FavoriteDealsEffect : Effect {
    data class NavigateToDetailsEffect(val id: String) : FavoriteDealsEffect()
}
