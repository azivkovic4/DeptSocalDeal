package com.example.deptsocaldeal.domain

import com.example.deptsocaldeal.data.local.DealsDatabase
import com.example.deptsocaldeal.data.local.PreferredCurrencyEntity
import com.example.deptsocaldeal.data.models.DealPresentation
import com.example.deptsocaldeal.data.services.DealsApi
import com.example.deptsocaldeal.data.services.DealsApi.Companion.BASE_IMAGE_URL
import com.example.deptsocaldeal.data.toCurrency
import com.example.deptsocaldeal.data.toDealEntity
import com.example.deptsocaldeal.data.toDealPresentation
import com.example.deptsocaldeal.data.toPreferredCurrencyEntity
import com.example.deptsocaldeal.domain.Currency.Companion.calculatePrice
import com.example.deptsocaldeal.util.Result
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.firstOrNull
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.map
import javax.inject.Inject

class RepositoryImpl @Inject constructor(
    private val dealsApi: DealsApi,
    private val dealsDatabase: DealsDatabase,
    private val htmlMapper: HtmlMapperUseCase,
) : Repository {

    override fun discoverDeals() = flow {
        emit(Result.Loading)
        try {
            val favoriteDealsIds =
                dealsDatabase.dealsDao.getFavoriteDeals().firstOrNull()?.map { it.id }
            val currentCurrency = preferredCurrency().first()
            val dealsResult = dealsApi.discoverDeals().deals.map {
                val isFavorite = favoriteDealsIds?.contains(it.unique) ?: false
                it.toDealPresentation(isFavorite).copy(
                    image = "$BASE_IMAGE_URL${it.image}",
                    symbol = currentCurrency.symbol,
                    discountPrice = it.prices.from_price?.amount?.calculatePrice(wantedCurrency = currentCurrency, from = it.prices.price.currency.symbol),
                    currentPrice = it.prices.price.amount.calculatePrice(wantedCurrency = currentCurrency, from = it.prices.price.currency.symbol)
                )
            }
            emit(Result.Success(dealsResult))
        } catch (exception: Exception) {
            emit(Result.Error(exception))
        }
    }

    override fun dealDetails(id: String) = flow {
        emit(Result.Loading)
        try {
            val favoriteDealsIds =
                dealsDatabase.dealsDao.getFavoriteDeals().firstOrNull()?.map { it.id }
            val currentCurrency = preferredCurrency().first()
            val dealDetailsResult = dealsApi.dealDetails(id)
            val isFavorite = favoriteDealsIds?.contains(dealDetailsResult.unique) ?: false
            val dealDetailsMapped = dealDetailsResult.toDealPresentation(isFavorite).copy(
                image = "$BASE_IMAGE_URL${dealDetailsResult.image}",
                symbol = currentCurrency.symbol,
                discountPrice = dealDetailsResult.prices.from_price?.amount?.calculatePrice(
                    wantedCurrency = currentCurrency, from = dealDetailsResult.prices.price.currency.symbol
                ),
                currentPrice = dealDetailsResult.prices.price.amount.calculatePrice(
                    wantedCurrency = currentCurrency, from = dealDetailsResult.prices.price.currency.symbol
                ),
                description = dealDetailsResult.description?.let { desc -> htmlMapper(desc) }
            )
            emit(Result.Success(dealDetailsMapped))
        } catch (exception: Exception) {
            emit(Result.Error(exception))
        }
    }

    override suspend fun addOrRemoveFromFavorites(deal: DealPresentation, isFavorite: Boolean) {
        val dealEntity = deal.toDealEntity().copy(isFavorite = isFavorite)
        dealsDatabase.dealsDao.insertDeal(dealEntity)
    }

    override fun favoriteDeals(): Flow<List<DealPresentation>> =
        dealsDatabase.dealsDao.getFavoriteDeals().map { deals ->
            val currentCurrency = preferredCurrency().first()
            deals.map { dealEntity ->
                dealEntity.toDealPresentation().copy(
                    symbol = currentCurrency.symbol,
                    discountPrice = dealEntity.discountPrice?.toDouble()?.toInt()?.calculatePrice(
                        wantedCurrency = currentCurrency, from = dealEntity.symbol
                    ),
                    currentPrice = dealEntity.currentPrice.toDouble().toInt().calculatePrice(
                        wantedCurrency = currentCurrency, from = dealEntity.symbol
                    )
                )
            }
        }

    override suspend fun savePreferredCurrency(currency: Currency) {
        dealsDatabase.dealsDao.savePreferredCurrency(currency.toPreferredCurrencyEntity())
    }

    override fun preferredCurrency(): Flow<Currency> =
        dealsDatabase.dealsDao.getPreferredCurrency().map { entity ->
            (entity ?: PreferredCurrencyEntity(0, Currency.EUR.symbol)).toCurrency()
        }
}
