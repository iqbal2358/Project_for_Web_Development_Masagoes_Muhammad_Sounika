package com.example.projectforwebdevelopmentmasagoesmuhammadsounika

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import java.io.IOException
import java.net.UnknownHostException

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

    fun updateCartQuantity(event: Event, newQuantity: Int) {
        if (newQuantity <= 0) {
            removeFromCart(event)
        } else {
            _cartItems.update { current ->
                current.map {
                    if (it.event.id == event.id) it.copy(quantity = newQuantity) else it
                }
            }
        }
    }

    fun removeFromCart(event: Event) {
        _cartItems.update { current ->
            current.filterNot { it.event.id == event.id }
        }
    }

    fun clearCart() {
        _cartItems.value = emptyList()
    }

    fun fetchEvents() {
        viewModelScope.launch {
            _uiState.value = EventUiState.Loading
            try {
                val eventsList = withContext(Dispatchers.IO) {
                    RetrofitClient.apiService.getEvents()
                }
                if (eventsList.isEmpty()) {
                    _uiState.value = EventUiState.Error("No tickets available right now.")
                } else {
                    _uiState.value = EventUiState.Success(eventsList)
                }
            } catch (e: Exception) {
                val errorMessage = when (e) {
                    is UnknownHostException, is IOException ->
                        "No internet connection. Please check your Wi-Fi or Mobile Data and try again."
                    else ->
                        e.localizedMessage ?: "Unable to connect to the backend server. Please try again."
                }
                _uiState.value = EventUiState.Error(errorMessage)
            }
        }
    }
}
