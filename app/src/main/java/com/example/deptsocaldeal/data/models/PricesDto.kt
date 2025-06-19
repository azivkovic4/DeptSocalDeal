package com.example.deptsocaldeal.data.models

data class PricesDto(
    val price: PriceDetailDto,
    val from_price: PriceDetailDto?,
    val price_label: String?,
    val discount_label: String
)
