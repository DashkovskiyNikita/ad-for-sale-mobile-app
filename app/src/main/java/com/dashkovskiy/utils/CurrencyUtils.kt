package com.dashkovskiy.utils

import java.math.BigDecimal
import java.util.*

/**
Convert price in minimal currency unit to price in max unit with currency symbol.
Return empty string if currencyIso code is not supported
 */
fun formatToPriceWithCurrency(price: Int, currencyIso: String): String {
    return runCatching {
        val currency = Currency.getInstance(currencyIso)
        val decimalPrice = BigDecimal(price)
        val fractionDigits = BigDecimal.TEN.pow(currency.defaultFractionDigits)
        val convertedPrice = decimalPrice.divide(fractionDigits)
        val currencySymbol = currency.getSymbol(Locale.getDefault())
        "${convertedPrice.toPlainString()} $currencySymbol"
    }.getOrElse { "" }
}