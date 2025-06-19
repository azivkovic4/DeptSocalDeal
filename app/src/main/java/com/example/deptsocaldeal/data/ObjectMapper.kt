package com.example.deptsocaldeal.data

import com.example.deptsocaldeal.data.local.DealEntity
import com.example.deptsocaldeal.data.local.PreferredCurrencyEntity
import com.example.deptsocaldeal.data.models.DealDto
import com.example.deptsocaldeal.data.models.DealPresentation
import com.example.deptsocaldeal.domain.Currency

fun DealDto.toDealPresentation(isFavorite: Boolean) =  DealPresentation(
    id = unique,
    title = title,
    image = image,
    soldLabel = sold_label,
    company = company,
    city = city,
    description = description,
    discountPrice = prices.from_price?.amount?.toString(),
    currentPrice = prices.price.amount.toString(),
    isFavorite = isFavorite
)

fun DealEntity.toDealPresentation() = DealPresentation(
        id = id,
        title = title,
        image = image,
        soldLabel = soldLabel,
        company = company,
        city = city,
        discountPrice = discountPrice,
        currentPrice = currentPrice,
        isFavorite = isFavorite
    )

fun DealPresentation.toDealEntity() = DealEntity(
    id = id,
    title = title,
    image = image,
    soldLabel = soldLabel,
    company = company,
    city = city,
    discountPrice = discountPrice,
    currentPrice = currentPrice,
    symbol = symbol,
    isFavorite = isFavorite
)

fun PreferredCurrencyEntity.toCurrency() = Currency.entries.first { it.symbol == symbol }

fun Currency.toPreferredCurrencyEntity() = PreferredCurrencyEntity(symbol = this.symbol)
