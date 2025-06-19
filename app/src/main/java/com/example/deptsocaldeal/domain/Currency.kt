package com.example.deptsocaldeal.domain

import android.annotation.SuppressLint


enum class Currency(val symbol: String) {
    USD("$"),
    EUR("€");

    companion object {
        private const val USD_TO_EUR_RATE = 0.85
        private const val EUR_TO_USD_RATE = 1.15
        @SuppressLint("DefaultLocale")
        fun Int.calculatePrice(wantedCurrency: Currency, from: String): String {
            if(wantedCurrency.symbol == from) return this.toString()
            val result = this * if (wantedCurrency == USD) EUR_TO_USD_RATE else USD_TO_EUR_RATE
            return String.format("%.2f", result)
        }
    }
}