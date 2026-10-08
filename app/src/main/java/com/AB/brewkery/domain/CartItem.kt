package com.AB.brewkery.domain

import com.AB.brewkery.data.model.MenuItem
import com.AB.brewkery.data.model.MilkOption
import com.AB.brewkery.data.model.SizeOption

data class CartItem(
    val item: MenuItem,
    val size: SizeOption,
    val milk: MilkOption,
    val sugar: String,
    val quantity: Int
) {
    // Same item with identical choices = same cart line
    val key: String get() = "${item.id}|${size.id}|${milk.id}|$sugar"

    val unitPrice: Double
        get() = PriceCalculator.unitPrice(item.basePrice, size.extraPrice, milk.extraPrice, sugar)

    val lineTotal: Double
        get() = PriceCalculator.lineTotal(unitPrice, quantity)
}