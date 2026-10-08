package com.AB.brewkery.UI.status

import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.AB.brewkery.UI.cart.PlacedOrder

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun OrderStatusScreen(
    order: PlacedOrder,
    estimatedWait: String,
    onBackToMenu: () -> Unit
) {
    Column(
        Modifier
            .fillMaxSize()
            .padding(24.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        Text(
            "Brewing in Progress!",
            style = MaterialTheme.typography.headlineMedium,
            fontWeight = FontWeight.Bold
        )
        Text("Your ticket was dispatched to our barista.", Modifier.padding(top = 8.dp))

        Card(Modifier.fillMaxWidth().padding(vertical = 24.dp)) {
            Column(Modifier.padding(16.dp), verticalArrangement = Arrangement.spacedBy(8.dp)) {
                Text("Order Ticket  ${order.ticketId}", fontWeight = FontWeight.Bold)
                AssistChip(onClick = {}, label = { Text("PREPARING") })
                Text("Estimated Wait: $estimatedWait")
                Text("Items Ordered: ${order.itemCount} ${if (order.itemCount == 1) "Item" else "Items"}")
                Text("Status: Barista accepted your order!")
            }
        }

        Button(onClick = onBackToMenu, modifier = Modifier.fillMaxWidth()) {
            Text("Back to Menu")
        }
    }
}