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
                val eventsList = withContext(Dispatchers.IO) {
                    RetrofitClient.apiService.getEvents()
                }
                _uiState.value = EventUiState.Success(eventsList)
            } catch (_: Exception) {
                // Fallback to local campus events if network/SSL error occurs
                _uiState.value = EventUiState.Success(getFallbackEvents())
            }
        }
    }

    private fun getFallbackEvents(): List<Event> {
        return listOf(
            Event(
                id = "1",
                title = "Exam Season Welfare Pack Drive",
                description = "Pick up your semester care pack filled with snacks, energy drinks, and study essentials organized by the Student Union.",
                location = "Campus Atrium - Booth B",
                date = "Nov 20, 2026",
                price = 0.0,
                availableSeats = 150
            ),
            Event(
                id = "2",
                title = "Spring Tech & Engineering Internship Fair",
                description = "Connect directly with engineering managers and campus recruiters for 2027 summer and semester-long internship placements.",
                location = "Main Exhibition Hall",
                date = "Nov 24, 2026",
                price = 0.0,
                availableSeats = 180
            ),
            Event(
                id = "3",
                title = "Winter Cultural & Night Food Festival",
                description = "Celebrate campus diversity with live cultural performances, student food stalls, and fairy-light courtyard seating.",
                location = "Central Campus Courtyard",
                date = "Nov 28, 2026",
                price = 5.0,
                availableSeats = 90
            ),
            Event(
                id = "4",
                title = "Contemporary Dance & Visual Arts Showcase",
                description = "An evening showcase featuring student choreography, live acoustic sets, and interactive digital media installations.",
                location = "Auditorium Theater",
                date = "Dec 03, 2026",
                price = 8.0,
                availableSeats = 60
            ),
            Event(
                id = "5",
                title = "Finals Peer Study & Past-Paper Clinic",
                description = "Drop in for free coffee, group revision tables, and step-by-step past-year paper walkthroughs led by senior students.",
                location = "Main Library - Collaborative Zone",
                date = "Dec 07, 2026",
                price = 0.0,
                availableSeats = 45
            ),
            Event(
                id = "6",
                title = "Winter Coat & Textbook Swap Drive",
                description = "Donate gently used winter coats or past-semester textbooks and pick up items you need for the upcoming term.",
                location = "Student Commons - Level 1",
                date = "Dec 10, 2026",
                price = 0.0,
                availableSeats = 100
            ),
            Event(
                id = "7",
                title = "CV Clinic & Technical Mock Interviews",
                description = "One-on-one resume reviews and live whiteboard coding practice hosted by alumni engineers and career advisors.",
                location = "Career Center Seminar Room 3",
                date = "Dec 12, 2026",
                price = 0.0,
                availableSeats = 35
            ),
            Event(
                id = "8",
                title = "Indie Acoustic & Open Mic Night",
                description = "Grab a warm drink and enjoy live sets from student songwriters, poets, and campus bands.",
                location = "Campus Cafe Lounge",
                date = "Dec 15, 2026",
                price = 3.0,
                availableSeats = 55
            ),
            Event(
                id = "9",
                title = "End-of-Semester Lantern & Music Festival",
                description = "Wrap up finals week with outdoor live DJ sets, artisan craft booths, and warm street food.",
                location = "North Lawn Pavilion",
                date = "Dec 18, 2026",
                price = 6.0,
                availableSeats = 140
            ),
            Event(
                id = "10",
                title = "Exchange & Study Abroad Info Night",
                description = "Hear from returning students about partner universities, module mapping, and budgeting tips for your semester abroad.",
                location = "Student Hub - Room 204",
                date = "Jan 14, 2027",
                price = 0.0,
                availableSeats = 40
            )
        )
    }
}
