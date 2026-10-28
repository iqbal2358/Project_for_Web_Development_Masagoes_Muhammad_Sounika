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

            /*
            // Remote API call - uncomment when backend API / database is ready
            try {
                val eventsList = RetrofitClient.apiService.getEvents()
                _uiState.value = EventUiState.Success(eventsList)
            } catch (e: Exception) {
                _uiState.value = EventUiState.Error(
                    e.localizedMessage ?: "Failed to connect to the backend server"
                )
            }
            */

            // Local sample data for development and testing
            val sampleEvents = listOf(
                Event(
                    id = "1",
                    title = "Tech Hackathon 2026",
                    description = "Join us for a 24-hour coding challenge! Build innovative web & mobile apps and win exciting prizes.",
                    location = "Student Center - Main Hall",
                    date = "Oct 25, 2026",
                    price = 0.0,
                    availableSeats = 50
                ),
                Event(
                    id = "2",
                    title = "Campus Music Festival",
                    description = "An evening of live music featuring top student bands, food trucks, and fun activities.",
                    location = "University Amphitheater",
                    date = "Nov 02, 2026",
                    price = 15.00,
                    availableSeats = 120
                ),
                Event(
                    id = "3",
                    title = "Web Development Workshop",
                    description = "Learn modern frontend and backend development techniques using Kotlin, Compose, and REST APIs.",
                    location = "Engineering Building, Room 204",
                    date = "Nov 10, 2026",
                    price = 5.00,
                    availableSeats = 30
                ),
                Event(
                    id = "4",
                    title = "Career & Internship Fair",
                    description = "Meet recruiters from top tech companies, present your resume, and explore career opportunities.",
                    location = "Campus Recreation Center",
                    date = "Nov 18, 2026",
                    price = 0.0,
                    availableSeats = 200
                )
            )

            _uiState.value = EventUiState.Success(sampleEvents)
        }
    }
}