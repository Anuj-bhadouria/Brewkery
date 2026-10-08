package com.AB.brewkery.domain

object PriceCalculator {

    // Matches prices hidden in text, e.g. "Light Wildflower Honey (+0.40)"
    private val SUGAR_EXTRA = Regex("""\(\+(\d+(?:\.\d+)?)\)""")

    fun round2(value: Double): Double = Math.round(value * 100) / 100.0

    fun sugarExtra(label: String): Double =
        SUGAR_EXTRA.find(label)?.groupValues?.get(1)?.toDoubleOrNull() ?: 0.0

    fun unitPrice(basePrice: Double, sizeExtra: Double, milkExtra: Double, sugarLabel: String): Double =
        round2(basePrice + sizeExtra + milkExtra + sugarExtra(sugarLabel))

    fun lineTotal(unitPrice: Double, quantity: Int): Double = round2(unitPrice * quantity)

    data class Summary(
        val subtotal: Double,
        val deliveryFee: Double,
        val tax: Double,
        val total: Double
    )

    fun summarize(subtotal: Double, deliveryFee: Double, taxRatePercent: Double): Summary {
        val sub = round2(subtotal)
        val tax = round2(sub * taxRatePercent / 100.0)
        return Summary(sub, deliveryFee, tax, round2(sub + deliveryFee + tax))
    }
}