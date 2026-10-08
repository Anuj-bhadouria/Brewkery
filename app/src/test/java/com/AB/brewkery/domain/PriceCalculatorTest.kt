package com.AB.brewkery.domain

import org.junit.Assert.assertEquals
import org.junit.Test

class PriceCalculatorTest {

    @Test
    fun unitPrice_addsSizeAndMilkExtras() {
        // Caramel Macchiato, Grande (+0.65), Oat Milk (+0.00) = 5.50
        assertEquals(5.50, PriceCalculator.unitPrice(4.85, 0.65, 0.0, "50% Mild"), 0.001)
    }

    @Test
    fun unitPrice_parsesPriceHiddenInSugarLabel() {
        // Nitro Cherry Cascara 5.60 + honey (+0.40) = 6.00
        assertEquals(6.00, PriceCalculator.unitPrice(5.60, 0.0, 0.0, "Light Wildflower Honey (+0.40)"), 0.001)
    }

    @Test
    fun lineTotal_multipliesByQuantity() {
        assertEquals(11.00, PriceCalculator.lineTotal(5.50, 2), 0.001)
    }

    @Test
    fun summary_matchesPrototype() {
        // Prototype: subtotal 9.40, delivery 2.50, tax 0.75, total 12.65
        val s = PriceCalculator.summarize(9.40, 2.50, 8.0)
        assertEquals(0.75, s.tax, 0.001)
        assertEquals(12.65, s.total, 0.001)
    }
}