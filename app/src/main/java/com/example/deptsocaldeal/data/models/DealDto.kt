package com.example.deptsocaldeal.data.models

data class DealDto(
    val unique: String,
    val title: String,
    val image: String,
    val company: String,
    val sold_label: String,
    val description: String?,
    val city: String,
    val prices: PricesDto
)
