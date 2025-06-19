package com.example.deptsocaldeal.presentation.viewmodels

import androidx.lifecycle.viewModelScope
import com.example.deptsocaldeal.data.models.DealPresentation
import com.example.deptsocaldeal.domain.InternetAvailability
import com.example.deptsocaldeal.domain.Repository
import com.example.deptsocaldeal.domain.Status
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
class SocialDealsViewModel @Inject constructor(
    private val dealsRepository: Repository,
    private val internetAvailability: InternetAvailability
) : BaseViewModel<SocialDealsScreenState, SocialDealsAction, SocialDealsEffect>() {

    init {
        observeInternetConnection()
        fetchDeals()
    }

    private fun observeInternetConnection() =
        internetAvailability.internetConnection.onEach { status ->
            setScreenState { copy(hasInternet = status) }
        }.launchIn(viewModelScope)

    private fun fetchDeals() {
        dealsRepository.discoverDeals().onEach { result ->
            setScreenState { copy(result = result) }
        }.launchIn(viewModelScope)
    }

    override fun createInitialScreenState() = SocialDealsScreenState()

    override suspend fun handleActions(action: SocialDealsAction) {
        when (action) {
            is SocialDealsAction.NavigateToDealDetailsAction -> {
                setEffect {
                    SocialDealsEffect.NavigateToDetailsEffect(action.id)
                }
            }

            is SocialDealsAction.ToggleFavoriteAction -> {
                dealsRepository.addOrRemoveFromFavorites(deal = action.deal, action.isFavorite)
            }
        }
    }
}

data class SocialDealsScreenState(
    val result: Result<List<DealPresentation>> = Result.Loading,
    val hasInternet: Status = Status.Unknown
) : ScreenState

sealed class SocialDealsAction : Action {
    data class NavigateToDealDetailsAction(val id: String) : SocialDealsAction()
    data class ToggleFavoriteAction(val deal: DealPresentation,val isFavorite:Boolean = false) : SocialDealsAction()
}

sealed class SocialDealsEffect : Effect {
    data class NavigateToDetailsEffect(val id: String) : SocialDealsEffect()
}
