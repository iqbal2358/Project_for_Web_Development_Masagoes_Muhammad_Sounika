package com.example.projectforwebdevelopmentmasagoesmuhammadsounika

import android.widget.Toast
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun EventListScreen(
    viewModel: EventViewModel,
    onEventSelected: (Event) -> Unit = {}
) {
    val uiState by viewModel.uiState.collectAsState()
    val cartItems by viewModel.cartItems.collectAsState()
    val context = LocalContext.current

    var selectedEvent by remember { mutableStateOf<Event?>(null) }

    val totalCartTickets = cartItems.sumOf { it.quantity }

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Campus Events") },
                actions = {
                    if (totalCartTickets > 0) {
                        Surface(
                            shape = MaterialTheme.shapes.extraLarge,
                            color = MaterialTheme.colorScheme.primary,
                            contentColor = MaterialTheme.colorScheme.onPrimary,
                            modifier = Modifier.padding(end = 12.dp)
                        ) {
                            Text(
                                text = "🛒 Cart ($totalCartTickets)",
                                style = MaterialTheme.typography.labelMedium,
                                fontWeight = FontWeight.Bold,
                                modifier = Modifier.padding(horizontal = 12.dp, vertical = 6.dp)
                            )
                        }
                    }
                },
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = MaterialTheme.colorScheme.primaryContainer
                )
            )
        }
    ) { paddingValues ->
        Box(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues)
                .padding(horizontal = 16.dp)
        ) {
            when (val state = uiState) {
                is EventUiState.Loading -> {
                    CircularProgressIndicator(
                        modifier = Modifier.align(Alignment.Center)
                    )
                }
                is EventUiState.Error -> {
                    Column(
                        modifier = Modifier.align(Alignment.Center),
                        horizontalAlignment = Alignment.CenterHorizontally
                    ) {
                        Text(
                            text = state.message,
                            color = MaterialTheme.colorScheme.error
                        )
                        Spacer(modifier = Modifier.height(8.dp))
                        Button(onClick = { viewModel.fetchEvents() }) {
                            Text("Retry")
                        }
                    }
                }
                is EventUiState.Success -> {
                    if (state.events.isEmpty()) {
                        Text(
                            text = "No events available right now.",
                            modifier = Modifier.align(Alignment.Center)
                        )
                    } else {
                        LazyColumn(
                            modifier = Modifier.fillMaxSize(),
                            contentPadding = PaddingValues(vertical = 12.dp)
                        ) {
                            items(state.events) { event ->
                                EventCard(
                                    event = event,
                                    onBookClick = { clickedEvent ->
                                        selectedEvent = clickedEvent
                                        onEventSelected(clickedEvent)
                                    }
                                )
                            }
                        }
                    }
                }
            }

            // Show Event Detail Dialog when an event is selected
            selectedEvent?.let { event ->
                EventDetailDialog(
                    event = event,
                    onDismiss = { selectedEvent = null },
                    onAddToCart = { itemEvent, quantity ->
                        viewModel.addToCart(itemEvent, quantity)
                        Toast.makeText(
                            context,
                            "Added $quantity ticket(s) for ${itemEvent.title} to Cart!",
                            Toast.LENGTH_SHORT
                        ).show()
                        selectedEvent = null
                    }
                )
            }
        }
    }
}