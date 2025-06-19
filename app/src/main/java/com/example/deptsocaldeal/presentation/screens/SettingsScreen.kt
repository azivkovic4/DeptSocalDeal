package com.example.deptsocaldeal.presentation.screens

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Switch
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import com.example.deptsocaldeal.R
import com.example.deptsocaldeal.domain.Currency
import com.example.deptsocaldeal.presentation.viewmodels.SettingsAction
import com.example.deptsocaldeal.presentation.viewmodels.SettingsScreenState
import com.example.deptsocaldeal.presentation.viewmodels.SettingsViewModel
import com.example.deptsocaldeal.util.HandleEffects

@Composable
fun SettingsScreen(
    viewModel: SettingsViewModel = hiltViewModel()
) {
    val state by viewModel.screenState.collectAsState()

    HandleEffects(effectFlow = viewModel.effect) {
        when (it) {
            else -> {}
        }
    }
    UIContent(state, viewModel::setAction)
}

@Composable
private fun UIContent(state: SettingsScreenState, setAction: (SettingsAction) -> Unit) =
    Column(
        modifier = Modifier.fillMaxSize(),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.spacedBy(10.dp)
    ) {
        var isChecked by remember(state.currentCurrency) {
            mutableStateOf(state.currentCurrency != Currency.EUR)
        }
        Text(
            text = stringResource(R.string.preferred_currency),
            style = MaterialTheme.typography.titleLarge
        )

        Row(verticalAlignment = Alignment.CenterVertically, horizontalArrangement = Arrangement.spacedBy(10.dp)) {
            Text(
                text = stringResource(R.string.eur),
                style = MaterialTheme.typography.titleLarge.copy(Color.Blue)
            )
            Switch(
                checked = isChecked,
                onCheckedChange = {
                    isChecked = it
                    setAction(SettingsAction.ToggleCurrentCurrency(isChecked))
                }
            )
            Text(
                text = stringResource(R.string.usd),
                style = MaterialTheme.typography.titleLarge.copy(Color.Green)
            )
        }
    }
