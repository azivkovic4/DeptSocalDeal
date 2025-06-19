package com.example.deptsocaldeal.util

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharedFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

abstract class BaseViewModel<S : ScreenState, A : Action, E : Effect> :
    ViewModel() {

    private val initialScreenState: S by lazy { createInitialScreenState() }
    protected abstract fun createInitialScreenState(): S

    private val lastClickEvent: MutableStateFlow<Long> = MutableStateFlow(0L)

    protected val currentScreenState: S get() = screenState.value

    private val _screenState: MutableStateFlow<S> by lazy { MutableStateFlow(initialScreenState) }

    val screenState by lazy { _screenState.asStateFlow() }

    private val _actions: MutableSharedFlow<A> = MutableSharedFlow()
    protected val actions = _actions.asSharedFlow()

    private val _effect: MutableSharedFlow<E> = MutableSharedFlow()
    val effect: SharedFlow<E> = _effect

    init {
        viewModelScope.launch {
            actions.collect {
                when (it is ClickAction) {
                    true -> avoidMultiTouch(it)
                    false -> handleActions(it)
                }
            }
        }
    }

    private suspend fun avoidMultiTouch(action: A) {
        if (System.currentTimeMillis().minus(lastClickEvent.value) > 300) {
            handleActions(action)
            lastClickEvent.value = System.currentTimeMillis()
        }
    }

    protected abstract suspend fun handleActions(action: A)

    protected fun setScreenState(reduce: S.() -> S) {
        _screenState.update {
            it.reduce()
        }
    }

    fun setAction(action: A) = viewModelScope.launch { _actions.emit(action) }

    protected fun setEffect(builder: () -> E) = viewModelScope.launch { _effect.emit(builder()) }
}
