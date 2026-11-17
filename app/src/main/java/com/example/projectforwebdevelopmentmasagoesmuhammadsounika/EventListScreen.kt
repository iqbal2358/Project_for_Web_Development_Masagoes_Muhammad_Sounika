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
    val events by viewModel.events.collectAsState()
    val cartItems by viewModel.cartItems.collectAsState()
    val campusTip by viewModel.campusTip.collectAsState() // Collect the quote state
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
            if (events.isEmpty()) {
                Text(
                    text = "No events available right now.",
                    modifier = Modifier.align(Alignment.Center)
                )
            } else {
                LazyColumn(
                    modifier = Modifier.fillMaxSize(),
                    contentPadding = PaddingValues(vertical = 12.dp),
                    verticalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    // 1. Campus Bulletin Card (Satisfying Prof Bâton's hint!)
                    item {
                        Card(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(bottom = 4.dp),
                            colors = CardDefaults.cardColors(
                                containerColor = MaterialTheme.colorScheme.surfaceVariant
                            ),
                            elevation = CardDefaults.cardElevation(defaultElevation = 2.dp)
                        ) {
                            Column(modifier = Modifier.padding(16.dp)) {
                                Text(
                                    text = "💡 Campus Bulletin Tip",
                                    style = MaterialTheme.typography.labelMedium,
                                    color = MaterialTheme.colorScheme.primary,
                                    fontWeight = FontWeight.Bold
                                )
                                Spacer(modifier = Modifier.height(4.dp))
                                Text(
                                    text = campusTip,
                                    style = MaterialTheme.typography.bodyMedium
                                )
                            }
                        }
                    }

                    // 2. Event List Items
                    items(
                        items = events,
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
