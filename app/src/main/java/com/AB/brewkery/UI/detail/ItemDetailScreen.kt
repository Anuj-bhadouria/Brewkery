package com.AB.brewkery.UI.detail

import androidx.compose.foundation.clickable
import androidx.compose.foundation.horizontalScroll
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material3.AssistChip
import androidx.compose.material3.Button
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.RadioButton
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import coil.compose.AsyncImage
import com.AB.brewkery.UI.menu.asMoney
import com.AB.brewkery.data.model.MenuItem
import com.AB.brewkery.domain.CartItem
import com.AB.brewkery.domain.PriceCalculator

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ItemDetailScreen(
    item: MenuItem,
    symbol: String,
    onBack: () -> Unit,
    onAddToCart: (CartItem) -> Unit
) {
    var sizeIdx by rememberSaveable { mutableStateOf(0) }
    var milkIdx by rememberSaveable { mutableStateOf(0) }
    var sugarIdx by rememberSaveable { mutableStateOf(0) }
    var qty by rememberSaveable { mutableStateOf(1) }

    val c = item.customizations
    val size = c.sizes[sizeIdx]
    val milk = c.milkOptions[milkIdx]
    val sugar = c.sugarLevels[sugarIdx]

    val unit = PriceCalculator.unitPrice(item.basePrice, size.extraPrice, milk.extraPrice, sugar)
    val total = PriceCalculator.lineTotal(unit, qty)

    Scaffold(
        topBar = {
            TopAppBar(
                title = {},
                navigationIcon = {
                    IconButton(onClick = onBack) {
                        Icon(Icons.AutoMirrored.Filled.ArrowBack, contentDescription = "Back")
                    }
                }
            )
        },
        bottomBar = {
            Button(
                onClick = { onAddToCart(CartItem(item, size, milk, sugar, qty)) },
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(16.dp)
            ) {
                Text("Add to Cart • ${total.asMoney(symbol)}")
            }
        }
    ) { padding ->
        Column(
            Modifier
                .padding(padding)
                .verticalScroll(rememberScrollState())
        ) {
            AsyncImage(
                model = item.imageUrl,
                contentDescription = item.name,
                contentScale = ContentScale.Crop,
                modifier = Modifier
                    .fillMaxWidth()
                    .height(260.dp)
            )

            Column(Modifier.padding(16.dp)) {
                item.badge?.let {
                    Text(
                        it,
                        style = MaterialTheme.typography.labelMedium,
                        color = MaterialTheme.colorScheme.primary,
                        fontWeight = FontWeight.Bold
                    )
                }
                Text(item.name, style = MaterialTheme.typography.headlineSmall, fontWeight = FontWeight.Bold)
                Text(item.tagline, style = MaterialTheme.typography.bodyMedium)
                Spacer(Modifier.height(4.dp))
                Text(
                    "★ ${item.rating} (${item.reviewCount}) · ${item.prepTime} · ${item.calories} cal",
                    style = MaterialTheme.typography.bodySmall
                )
                Spacer(Modifier.height(8.dp))
                Text(item.basePrice.asMoney(symbol), style = MaterialTheme.typography.titleLarge)
                Spacer(Modifier.height(8.dp))
                Text(item.description, style = MaterialTheme.typography.bodyMedium)

                Text(
                    "Key Ingredients",
                    style = MaterialTheme.typography.titleMedium,
                    fontWeight = FontWeight.Bold,
                    modifier = Modifier.padding(top = 16.dp, bottom = 8.dp)
                )
                Row(
                    Modifier.horizontalScroll(rememberScrollState()),
                    horizontalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    item.ingredients.forEach { AssistChip(onClick = {}, label = { Text(it) }) }
                }

                OptionGroup("Size Selection", c.sizes, sizeIdx, { it.label }, { it.extraPrice }, symbol) { sizeIdx = it }
                OptionGroup("Milk Options / Spreads", c.milkOptions, milkIdx, { it.name }, { it.extraPrice }, symbol) { milkIdx = it }
                // The sugar label already shows its price when it has one, e.g. "(+0.40)"
                OptionGroup("Sugar Levels / Serving", c.sugarLevels, sugarIdx, { it }, { 0.0 }, symbol) { sugarIdx = it }

                Row(
                    Modifier
                        .fillMaxWidth()
                        .padding(top = 20.dp),
                    horizontalArrangement = Arrangement.Center,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    OutlinedButton(onClick = { if (qty > 1) qty-- }) { Text("-") }
                    Text("$qty", Modifier.padding(horizontal = 20.dp), style = MaterialTheme.typography.titleLarge)
                    OutlinedButton(onClick = { if (qty < 20) qty++ }) { Text("+") }
                }
            }
        }
    }
}

@Composable
private fun <T> OptionGroup(
    title: String,
    options: List<T>,
    selectedIndex: Int,
    label: (T) -> String,
    extraPrice: (T) -> Double,
    symbol: String,
    onSelect: (Int) -> Unit
) {
    Text(
        title,
        style = MaterialTheme.typography.titleMedium,
        fontWeight = FontWeight.Bold,
        modifier = Modifier.padding(top = 16.dp)
    )
    options.forEachIndexed { index, option ->
        Row(
            Modifier
                .fillMaxWidth()
                .clickable { onSelect(index) }
                .padding(vertical = 2.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            RadioButton(selected = index == selectedIndex, onClick = { onSelect(index) })
            Text(label(option), Modifier.weight(1f))
            val extra = extraPrice(option)
            if (extra > 0) Text("+${extra.asMoney(symbol)}")
        }
    }
}