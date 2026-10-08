package com.AB.brewkery.UI.cart

import androidx.lifecycle.ViewModel
import com.AB.brewkery.domain.CartItem
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlin.random.Random

data class PlacedOrder(val ticketId: String, val itemCount: Int)

class CartViewModel : ViewModel() {

    private val _items = MutableStateFlow<List<CartItem>>(emptyList())
    val items: StateFlow<List<CartItem>> = _items.asStateFlow()

    private val _placedOrder = MutableStateFlow<PlacedOrder?>(null)
    val placedOrder: StateFlow<PlacedOrder?> = _placedOrder.asStateFlow()

    fun add(newItem: CartItem) {
        _items.update { current ->
            if (current.any { it.key == newItem.key }) {
                current.map {
                    if (it.key == newItem.key) it.copy(quantity = it.quantity + newItem.quantity) else it
                }
            } else {
                current + newItem
            }
        }
    }

    fun changeQuantity(key: String, delta: Int) {
        _items.update { current ->
            current.mapNotNull {
                if (it.key != key) it
                else {
                    val q = it.quantity + delta
                    if (q <= 0) null else it.copy(quantity = q)
                }
            }
        }
    }

    fun remove(key: String) {
        _items.update { current -> current.filterNot { it.key == key } }
    }

    fun placeOrder(): PlacedOrder {
        val order = PlacedOrder(
            ticketId = "#BK-" + Random.nextInt(10000, 100000),
            itemCount = _items.value.sumOf { it.quantity }
        )
        _placedOrder.value = order
        _items.value = emptyList()
        return order
    }
}