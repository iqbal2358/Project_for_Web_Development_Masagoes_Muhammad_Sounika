package com.example.projectforwebdevelopmentmasagoesmuhammadsounika

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import java.util.Locale

@Composable
fun EventDetailDialog(
    event: Event,
    onDismiss: () -> Unit,
    onAddToCart: (Event, Int) -> Unit
) {
    var ticketCount by remember { mutableIntStateOf(1) }
    val maxTickets = if (event.availableSeats > 0) event.availableSeats else 1
    val scrollState = rememberScrollState()

    AlertDialog(
        onDismissRequest = onDismiss,
        title = {
            Text(text = event.title, fontWeight = FontWeight.Bold, fontSize = 20.sp)
        },
        text = {
            Column(
                modifier = Modifier.verticalScroll(scrollState)
            ) {
                // Event Banner Placeholder
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(90.dp)
                        .clip(RoundedCornerShape(8.dp))
                        .background(MaterialTheme.colorScheme.primaryContainer),
                    contentAlignment = Alignment.Center
                ) {
                    Text(
                        text = "🎟️ " + event.title,
                        fontWeight = FontWeight.Bold,
                        color = MaterialTheme.colorScheme.onPrimaryContainer,
                        fontSize = 16.sp
                    )
                }

                Spacer(modifier = Modifier.height(12.dp))

                Text(
                    text = "📅 ${event.date}  |  📍 ${event.location}",
                    style = MaterialTheme.typography.bodyMedium,
                    color = MaterialTheme.colorScheme.onSurfaceVariant
                )

                Spacer(modifier = Modifier.height(8.dp))

                Text(
                    text = event.description,
                    style = MaterialTheme.typography.bodyMedium
                )

                Spacer(modifier = Modifier.height(12.dp))

                Text(
                    text = "🎟️ Remaining seats: ${event.availableSeats}",
                    style = MaterialTheme.typography.bodySmall,
                    color = if (event.availableSeats < 10) MaterialTheme.colorScheme.error else MaterialTheme.colorScheme.secondary,
                    fontWeight = FontWeight.Medium
                )

                Spacer(modifier = Modifier.height(16.dp))

                // Quantity Selector
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.SpaceBetween,
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Text(text = "Ticket Quantity:", fontWeight = FontWeight.SemiBold)
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        OutlinedButton(
                            onClick = { if (ticketCount > 1) ticketCount-- },
                            contentPadding = PaddingValues(0.dp),
                            modifier = Modifier.size(36.dp)
                        ) { Text("-") }

                        Text(
                            text = "$ticketCount",
                            modifier = Modifier.padding(horizontal = 12.dp),
                            fontWeight = FontWeight.Bold
                        )

                        OutlinedButton(
                            onClick = { if (ticketCount < maxTickets) ticketCount++ },
                            contentPadding = PaddingValues(0.dp),
                            modifier = Modifier.size(36.dp)
                        ) { Text("+") }
                    }
                }

                Spacer(modifier = Modifier.height(12.dp))

                // Total Calculation
                val totalPrice = event.price * ticketCount
                Text(
                    text = if (totalPrice == 0.0) "Total: FREE" else "Total: $" + String.format(Locale.US, "%.2f", totalPrice),
                    fontWeight = FontWeight.Bold,
                    color = MaterialTheme.colorScheme.primary,
                    fontSize = 18.sp
                )
            }
        },
        confirmButton = {
            Button(
                onClick = { onAddToCart(event, ticketCount) },
                enabled = event.availableSeats > 0
            ) {
                Text("Add to Cart")
            }
        },
        dismissButton = {
            TextButton(onClick = onDismiss) {
                Text("Cancel")
            }
        }
    )
}