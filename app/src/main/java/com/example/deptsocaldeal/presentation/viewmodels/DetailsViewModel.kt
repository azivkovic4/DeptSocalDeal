package com.example.deptsocaldeal.presentation.viewmodels

import androidx.lifecycle.viewModelScope
import com.example.deptsocaldeal.data.models.DealPresentation
import com.example.deptsocaldeal.domain.Repository
import com.example.deptsocaldeal.util.Action
import com.example.deptsocaldeal.util.BaseViewModel
import com.example.deptsocaldeal.util.Effect
import com.example.deptsocaldeal.util.Result
import com.example.deptsocaldeal.util.ScreenState
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import javax.inject.Inject

@HiltViewModel
class DetailsViewModel @Inject constructor(
    private val dealsRepository: Repository
) : BaseViewModel<DealDetailsScreenState, DealDetailsAction, DealDetailsEffect>() {

    fun setData(id: String) {
        dealsRepository.dealDetails(id).onEach {
            setScreenState {
                copy(result = it)
            }
        }.launchIn(viewModelScope)
    }

    override fun createInitialScreenState() = DealDetailsScreenState()

    override suspend fun handleActions(action: DealDetailsAction) {
        when (action) {
            is DealDetailsAction.NavigateBack -> {
                setEffect {
                    DealDetailsEffect.OnBackEffect
                }
            }

            is DealDetailsAction.ToggleFavoriteDeal -> {
                dealsRepository.addOrRemoveFromFavorites(deal = action.deal, action.isFavorite)
            }
        }
    }
}

data class DealDetailsScreenState(
    val result: Result<DealPresentation> = Result.Loading
) : ScreenState

sealed class DealDetailsAction : Action {
    data object NavigateBack : DealDetailsAction()
    data class ToggleFavoriteDeal(val deal: DealPresentation, val isFavorite: Boolean = false) :
        DealDetailsAction()
}

sealed class DealDetailsEffect : Effect {
    data object OnBackEffect : DealDetailsEffect()
}
