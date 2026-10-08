package com.AB.brewkery.UI.menu

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.FilterChip
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.lifecycle.viewmodel.compose.viewModel
import coil.compose.AsyncImage
import com.AB.brewkery.data.model.MenuItem
import com.AB.brewkery.data.model.MenuResponse
import java.util.Locale

fun Double.asMoney(symbol: String): String =
    "$symbol${String.format(Locale.US, "%.2f", this)}"

@Composable
fun MenuScreen(
    viewModel: MenuViewModel = viewModel(),
    onItemClick: (Int) -> Unit = {},
    cartCount: Int = 0,
    cartTotal: Double = 0.0,
    onCartClick: () -> Unit = {},
    activeTicketId: String? = null
) {
    val state by viewModel.uiState.collectAsStateWithLifecycle()
    val selected by viewModel.selectedCategoryId.collectAsStateWithLifecycle()

    Scaffold(
        bottomBar = {
            if (cartCount > 0) {
                val symbol = (state as? MenuUiState.Success)?.data?.meta?.currencySymbol ?: "$"
                Button(
                    onClick = onCartClick,
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(16.dp)
                ) {
                    Text("View Your Cart  ${cartTotal.asMoney(symbol)}")
                }
            }
        }
    ) { padding ->
        Box(
            modifier = Modifier
                .padding(padding)
                .fillMaxSize()
        ) {
            when (val s = state) {
                is MenuUiState.Loading ->
                    CircularProgressIndicator(Modifier.align(Alignment.Center))

                is MenuUiState.Error ->
                    ErrorView(
                        message = s.message,
                        onRetry = viewModel::loadMenu,
                        modifier = Modifier.align(Alignment.Center)
                    )

                is MenuUiState.Success ->
                    MenuContent(
                        data = s.data,
                        selectedCategoryId = selected,
                        onCategorySelected = viewModel::selectCategory,
                        onItemClick = onItemClick,
                        activeTicketId = activeTicketId
                    )
            }
        }
    }
}

@Composable
private fun MenuContent(
    data: MenuResponse,
    selectedCategoryId: String?,
    onCategorySelected: (String?) -> Unit,
    onItemClick: (Int) -> Unit,
    activeTicketId: String?
) {
    val symbol = data.meta.currencySymbol
    val visibleItems = if (selectedCategoryId == null) data.items
    else data.items.filter { it.categoryId == selectedCategoryId }

    LazyColumn(contentPadding = PaddingValues(bottom = 16.dp)) {
        item {
            Column(Modifier.padding(16.dp)) {
                Text(data.meta.tagline, style = MaterialTheme.typography.labelMedium)
                Text(
                    data.meta.app,
                    style = MaterialTheme.typography.headlineMedium,
                    fontWeight = FontWeight.Bold
                )
            }
        }

        // Store announcement banner
        item {
            Card(Modifier.padding(horizontal = 16.dp).fillMaxWidth()) {
                Row(
                    Modifier.padding(16.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Column(Modifier.weight(1f)) {
                        Text(
                            "Delivery in ${data.meta.estimatedDeliveryTime}",
                            fontWeight = FontWeight.Bold
                        )
                        Text("${data.meta.deliveryFee.asMoney(symbol)} flat fee")
                    }
                    Text("Open", color = MaterialTheme.colorScheme.primary)
                }
            }
        }

        // Active order tracking (shown after an order is placed)
        if (activeTicketId != null) {
            item {
                Card(
                    Modifier.padding(horizontal = 16.dp, vertical = 8.dp).fillMaxWidth(),
                    colors = CardDefaults.cardColors(
                        containerColor = MaterialTheme.colorScheme.secondaryContainer
                    )
                ) {
                    Text(
                        "Order $activeTicketId · PREPARING",
                        Modifier.padding(16.dp),
                        fontWeight = FontWeight.Bold
                    )
                }
            }
        }

        // Category chips
        item {
            LazyRow(
                contentPadding = PaddingValues(horizontal = 16.dp, vertical = 12.dp),
                horizontalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                item {
                    FilterChip(
                        selected = selectedCategoryId == null,
                        onClick = { onCategorySelected(null) },
                        label = { Text("All Items") }
                    )
                }
                items(data.categories, key = { it.id }) { category ->
                    FilterChip(
                        selected = selectedCategoryId == category.id,
                        onClick = { onCategorySelected(category.id) },
                        label = { Text("${category.icon} ${category.name}") }
                    )
                }
            }
        }

        items(visibleItems, key = { it.id }) { menuItem ->
            ItemCard(menuItem, symbol) { onItemClick(menuItem.id) }
        }
    }
}

@Composable
private fun ItemCard(item: MenuItem, symbol: String, onClick: () -> Unit) {
    Card(
        onClick = onClick,
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 16.dp, vertical = 6.dp)
    ) {
        Row(Modifier.padding(12.dp), verticalAlignment = Alignment.CenterVertically) {
            AsyncImage(
                model = item.imageUrl,
                contentDescription = item.name,
                contentScale = ContentScale.Crop,
                modifier = Modifier
                    .size(88.dp)
                    .clip(RoundedCornerShape(12.dp))
            )
            Spacer(Modifier.width(12.dp))
            Column(Modifier.weight(1f)) {
                item.badge?.let {
                    Text(
                        it,
                        style = MaterialTheme.typography.labelSmall,
                        color = MaterialTheme.colorScheme.primary,
                        fontWeight = FontWeight.Bold
                    )
                }
                Text(item.name, fontWeight = FontWeight.Bold)
                Text(
                    "★ ${item.rating} (${item.reviewCount})",
                    style = MaterialTheme.typography.bodySmall
                )
                Text(
                    item.basePrice.asMoney(symbol),
                    style = MaterialTheme.typography.titleMedium
                )
            }
        }
    }
}

@Composable
private fun ErrorView(message: String, onRetry: () -> Unit, modifier: Modifier = Modifier) {
    Column(
        modifier = modifier.padding(24.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text("Something went wrong", style = MaterialTheme.typography.titleMedium)
        Spacer(Modifier.height(8.dp))
        Text(message)
        Spacer(Modifier.height(16.dp))
        Button(onClick = onRetry) { Text("Retry") }
    }
}
