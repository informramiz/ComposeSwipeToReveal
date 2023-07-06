package com.example.composeswipetoreveal.ui.screens

import androidx.lifecycle.ViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow

class MainViewModel : ViewModel() {
    private val _viewStateFlow = MutableStateFlow(
        MainViewState(
            listItems = List(15) { index ->
                MainItem(index, "Hello $index")
            }
        ))

    val viewState: StateFlow<MainViewState>
        get() = _viewStateFlow.asStateFlow()

    fun onDismissItem(item: MainItem) {
        updateState { currentState ->
            currentState.copy(listItems = currentState.listItems.filter { it != item })
        }
    }

    private fun updateState(block: (MainViewState) -> MainViewState) {
        _viewStateFlow.value = block(viewState.value)
    }
}