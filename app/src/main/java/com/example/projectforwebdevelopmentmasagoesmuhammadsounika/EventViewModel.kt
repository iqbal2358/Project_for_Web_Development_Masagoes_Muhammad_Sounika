package com.example.projectforwebdevelopmentmasagoesmuhammadsounika

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

data class CartItem(
    val event: Event,
    val quantity: Int
)

sealed interface EventUiState {
    object Loading : EventUiState
    data class Success(val events: List<Event>) : EventUiState
    data class Error(val message: String) : EventUiState
}

class EventViewModel : ViewModel() {

    private val _uiState = MutableStateFlow<EventUiState>(EventUiState.Loading)
    val uiState: StateFlow<EventUiState> = _uiState

    private val _cartItems = MutableStateFlow<List<CartItem>>(emptyList())
    val cartItems: StateFlow<List<CartItem>> = _cartItems.asStateFlow()

    init {
        fetchEvents()
    }

    fun addToCart(event: Event, quantity: Int) {
        _cartItems.update { current ->
            val index = current.indexOfFirst { it.event.id == event.id }
            if (index >= 0) {
                current.toMutableList().apply {
                    val existing = this[index]
                    this[index] = existing.copy(quantity = existing.quantity + quantity)
                }
            } else {
                current + CartItem(event, quantity)
            }
        }
    }

    fun fetchEvents() {
        viewModelScope.launch {
            _uiState.value = EventUiState.Loading
            try {
                val eventsList = RetrofitClient.apiService.getEvents()
                _uiState.value = EventUiState.Success(eventsList)
            } catch (e: Exception) {
                _uiState.value = EventUiState.Error(
                    e.localizedMessage ?: "Failed to connect to the backend server"
                )
            }
        }
    }
}
