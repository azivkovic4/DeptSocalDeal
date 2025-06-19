package com.example.deptsocaldeal

import com.example.deptsocaldeal.data.local.DealEntity
import com.example.deptsocaldeal.data.local.PreferredCurrencyEntity
import com.example.deptsocaldeal.data.models.CurrencyDto
import com.example.deptsocaldeal.data.models.DealDto
import com.example.deptsocaldeal.data.models.DealsResult
import com.example.deptsocaldeal.data.models.PriceDetailDto
import com.example.deptsocaldeal.data.models.PricesDto

val testDealEntity = DealEntity(
    id = "123",
    title = "Test Deal",
    image = "https://example.com/image.jpg",
    soldLabel = "Sold Out",
    company = "Test Company",
    city = "Test City",
    discountPrice = "10.00",
    currentPrice = "20.00",
    symbol = "$",
    isFavorite = false
)

val testDealDto = DealDto(
    unique = "123",
    title = "Test Deal",
    image = "image.jpg",
    sold_label = "Sold Out",
    company = "Test Company",
    city = "Test City",
    description = "This is a test deal description.",
    prices = PricesDto(
        from_price = PriceDetailDto(amount = 10, currency = CurrencyDto("$", "USD")),
        price = PriceDetailDto(amount = 20, currency = CurrencyDto("$", "USD")),
        price_label = "Price",
        discount_label = "Discount"
    ),
)

val testPreferredCurrencyEntity = PreferredCurrencyEntity(symbol = "$")

val testDealsResponse = DealsResult(
    num_deals = 100,
    deals = listOf(
        testDealDto,
        testDealDto.copy(unique = "456"),
        testDealDto.copy(unique = "789")
    ),
)

