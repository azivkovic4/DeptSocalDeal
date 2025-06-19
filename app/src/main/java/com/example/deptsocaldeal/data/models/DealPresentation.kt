package com.example.deptsocaldeal.data.models

data class DealPresentation(
    val id: String,
    val title: String,
    val image: String,
    val soldLabel: String,
    val company: String,
    val city: String,
    val description: String? = null,
    val symbol: String = "$",
    val discountPrice:String?,
    val currentPrice: String,
    var isFavorite: Boolean
)
