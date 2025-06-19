package com.example.deptsocaldeal.presentation.components

import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.res.stringResource
import com.example.deptsocaldeal.R
import com.example.deptsocaldeal.domain.Status
import com.example.deptsocaldeal.util.LocalSnackBarHostState

@Composable
internal fun InternetAvailabilitySnackBar(status: Status) {
    val snackBarHostState = LocalSnackBarHostState.current
    val noInternetStatus = stringResource(R.string.no_internet_available)
    LaunchedEffect(key1 = status) {
        if (status != Status.Available) {
            snackBarHostState.showSnackbar(noInternetStatus)
        }
    }
}
