package com.example.projectforwebdevelopmentmasagoesmuhammadsounika

import android.widget.Toast
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
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
    var showCartDialog by remember { mutableStateOf(false) }

    val totalCartTickets = cartItems.sumOf { it.quantity }

    Scaffold(
        containerColor = MaterialTheme.colorScheme.background,
        contentColor = MaterialTheme.colorScheme.onBackground,
        topBar = {
            TopAppBar(
                title = { Text("Campus Events") },
                actions = {
                    OutlinedButton(
                        onClick = { viewModel.fetchEvents() },
                        modifier = Modifier.padding(end = 6.dp)
                    ) {
                        Text("Refresh", fontWeight = FontWeight.Bold)
                    }

                    FilledTonalButton(
                        onClick = { showCartDialog = true },
                        modifier = Modifier.padding(end = 8.dp)
                    ) {
                        Text(
                            text = if (totalCartTickets > 0) "Cart ($totalCartTickets)" else "Cart",
                            fontWeight = FontWeight.Bold
                        )
                    }
                },
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = MaterialTheme.colorScheme.primaryContainer,
                    titleContentColor = MaterialTheme.colorScheme.onPrimaryContainer
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
                    Card(
                        modifier = Modifier
                            .fillMaxWidth()
                            .align(Alignment.Center)
                            .padding(16.dp),
                        colors = CardDefaults.cardColors(
                            containerColor = MaterialTheme.colorScheme.surfaceVariant
                        ),
                        elevation = CardDefaults.cardElevation(defaultElevation = 2.dp)
                    ) {
                        Column(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(24.dp),
                            horizontalAlignment = Alignment.CenterHorizontally
                        ) {
                            Text(
                                text = state.message,
                                style = MaterialTheme.typography.bodyMedium,
                                color = MaterialTheme.colorScheme.error,
                                textAlign = TextAlign.Center
                            )
                            Spacer(modifier = Modifier.height(16.dp))
                            Button(
                                onClick = { viewModel.fetchEvents() },
                                shape = RoundedCornerShape(8.dp)
                            ) {
                                Text("Retry", fontWeight = FontWeight.Bold)
                            }
                        }
                    }
                }
                is EventUiState.Success -> {
                    if (state.events.isEmpty()) {
                        Column(
                            modifier = Modifier.align(Alignment.Center),
                            horizontalAlignment = Alignment.CenterHorizontally
                        ) {
                            Text(
                                text = "No tickets available right now.",
                                style = MaterialTheme.typography.bodyLarge,
                                color = MaterialTheme.colorScheme.onSurfaceVariant
                            )
                            Spacer(modifier = Modifier.height(12.dp))
                            Button(
                                onClick = { viewModel.fetchEvents() },
                                shape = RoundedCornerShape(8.dp)
                            ) {
                                Text("Retry", fontWeight = FontWeight.Bold)
                            }
                        }
                    } else {
                        LazyColumn(
                            modifier = Modifier.fillMaxSize(),
                            contentPadding = PaddingValues(vertical = 12.dp)
                        ) {
                            items(
                                items = state.events,
                                key = { it.id }
                            ) { event ->
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

            // Show Shopping Cart Dialog
            if (showCartDialog) {
                CartDialog(
                    cartItems = cartItems,
                    onDismiss = { showCartDialog = false },
                    onUpdateQuantity = { event, qty -> viewModel.updateCartQuantity(event, qty) },
                    onRemoveItem = { event -> viewModel.removeFromCart(event) },
                    onClearCart = { viewModel.clearCart() },
                    onCheckout = {
                        viewModel.checkoutCart()
                        showCartDialog = false
                        Toast.makeText(context, "Order confirmed! Ticket seats updated.", Toast.LENGTH_LONG).show()
                    }
                )
            }
        }
    }
}
