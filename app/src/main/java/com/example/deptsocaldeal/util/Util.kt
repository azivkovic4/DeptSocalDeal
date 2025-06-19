package com.example.deptsocaldeal.util

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.SnackbarHostState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.compositionLocalOf
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.flow.Flow

@Composable
fun <T : Effect> HandleEffects(
    effectFlow: Flow<T>,
    key: Any? = Unit,
    content: CoroutineScope.(effect: T) -> Unit
) {
    LaunchedEffect(key) {
        effectFlow.collect { this.content(it) }
    }
}
sealed class Result<out T> {
    data class Success<out T>(val data: T) : Result<T>()
    data class Error(val exception: Exception) : Result<Nothing>()
    data object Loading: Result<Nothing>()
}

@Composable
fun <T> ErrorHandlerComp(
    result: Result<T>,
    successContent: @Composable (T) -> Unit,
    loadingContent: @Composable () -> Unit = {},
    errorContent: @Composable (String) -> Unit = { }
) {
    Box(Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
        when (result) {
            is Result.Loading -> loadingContent()
            is Result.Success -> successContent(result.data)
            is Result.Error -> errorContent(result.exception.message ?: "Unknown error")
        }
    }
}

val LocalSnackBarHostState = compositionLocalOf<SnackbarHostState> {
    error("SnackbarHostState wasn't provided")
}
