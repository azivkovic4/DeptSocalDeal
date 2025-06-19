package com.example.deptsocaldeal.presentation.viewmodels

import androidx.lifecycle.viewModelScope
import com.example.deptsocaldeal.domain.Currency
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
class SettingsViewModel @Inject constructor(
    private val dealsRepository: Repository
) : BaseViewModel<SettingsScreenState, SettingsAction, SettingsEffect>() {

    init {
        dealsRepository.preferredCurrency().onEach { currency ->
            setScreenState {
                copy(currentCurrency = currency)
            }
        }.launchIn(viewModelScope)
    }

    override fun createInitialScreenState() = SettingsScreenState()

    override suspend fun handleActions(action: SettingsAction) {
        when (action) {
            is SettingsAction.ToggleCurrentCurrency -> {
                val newCurrency = if (action.isChecked) Currency.USD else Currency.EUR
                dealsRepository.savePreferredCurrency(newCurrency)
            }
        }
    }
}

data class SettingsScreenState(
    val currentCurrency: Currency = Currency.EUR,
) : ScreenState

sealed class SettingsAction : Action {
    data class ToggleCurrentCurrency(val isChecked: Boolean) : SettingsAction()
}

sealed class SettingsEffect : Effect