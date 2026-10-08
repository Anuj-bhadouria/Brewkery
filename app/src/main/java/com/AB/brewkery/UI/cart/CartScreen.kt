package com.AB.brewkery.UI.cart

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.AB.brewkery.UI.menu.asMoney
import com.AB.brewkery.data.model.Meta
import com.AB.brewkery.domain.CartItem
import com.AB.brewkery.domain.PriceCalculator

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun CartScreen(
    items: List<CartItem>,
    meta: Meta,
    onQuantityChange: (key: String, delta: Int) -> Unit,
    onRemove: (key: String) -> Unit,
    onPlaceOrder: () -> Unit,
    onBack: () -> Unit
) {
    val symbol = meta.currencySymbol
    val summary = PriceCalculator.summarize(
        subtotal = items.sumOf { it.lineTotal },
        deliveryFee = meta.deliveryFee,
        taxRatePercent = meta.taxRatePercent
    )

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Your Cart") },
                navigationIcon = {
                    IconButton(onClick = onBack) {
                        Icon(Icons.AutoMirrored.Filled.ArrowBack, contentDescription = "Back")
                    }
                }
            )
        },
        bottomBar = {
            if (items.isNotEmpty()) {
                Button(
                    onClick = onPlaceOrder,
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(16.dp)
                ) {
                    Text("Place Order Now • ${summary.total.asMoney(symbol)}")
                }
            }
        }
    ) { padding ->
        if (items.isEmpty()) {
            Box(
                Modifier.padding(padding).fillMaxSize(),
                contentAlignment = Alignment.Center
            ) {
                Text("Your cart is empty")
            }
        } else {
            LazyColumn(
                modifier = Modifier.padding(padding),
                contentPadding = PaddingValues(16.dp),
                verticalArrangement = Arrangement.spacedBy(12.dp)
            ) {
                items(items, key = { it.key }) { cartItem ->
                    Card(Modifier.fillMaxWidth()) {
                        Column(Modifier.padding(12.dp)) {
                            Row(verticalAlignment = Alignment.CenterVertically) {
                                Column(Modifier.weight(1f)) {
                                    Text(cartItem.item.name, fontWeight = FontWeight.Bold)
                                    Text(
                                        "${cartItem.size.label} · ${cartItem.milk.name} · ${cartItem.sugar}",
                                        style = MaterialTheme.typography.bodySmall
                                    )
                                }
                                Text(cartItem.lineTotal.asMoney(symbol), fontWeight = FontWeight.Bold)
                            }
                            Row(
                                Modifier.fillMaxWidth(),
                                verticalAlignment = Alignment.CenterVertically
                            ) {
                                OutlinedButton(onClick = { onQuantityChange(cartItem.key, -1) }) { Text("-") }
                                Text("${cartItem.quantity}", Modifier.padding(horizontal = 16.dp))
                                OutlinedButton(onClick = { onQuantityChange(cartItem.key, 1) }) { Text("+") }
                                Spacer(Modifier.weight(1f))
                                TextButton(onClick = { onRemove(cartItem.key) }) { Text("Remove") }
                            }
                        }
                    }
                }

                item {
                    Column(Modifier.padding(top = 8.dp)) {
                        SummaryRow("Subtotal", summary.subtotal.asMoney(symbol))
                        SummaryRow("Delivery Fee", summary.deliveryFee.asMoney(symbol))
                        SummaryRow("Est. Tax (${meta.taxRatePercent}%)", summary.tax.asMoney(symbol))
                        HorizontalDivider(Modifier.padding(vertical = 8.dp))
                        SummaryRow("Total Payable", summary.total.asMoney(symbol), bold = true)
                    }
                }
            }
        }
    }
}

@Composable
private fun SummaryRow(label: String, value: String, bold: Boolean = false) {
    Row(Modifier.fillMaxWidth().padding(vertical = 2.dp)) {
        Text(label, Modifier.weight(1f), fontWeight = if (bold) FontWeight.Bold else null)
        Text(value, fontWeight = if (bold) FontWeight.Bold else null)
    }
}