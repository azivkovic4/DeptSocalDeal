package com.example.deptsocaldeal.domain

import com.example.deptsocaldeal.data.models.DealPresentation
import com.example.deptsocaldeal.util.Result
import kotlinx.coroutines.flow.Flow

interface Repository {
    fun discoverDeals(): Flow<Result<List<DealPresentation>>>

    fun dealDetails(id: String): Flow<Result<DealPresentation>>

    suspend fun addOrRemoveFromFavorites(deal: DealPresentation, isFavorite:Boolean)

    fun favoriteDeals(): Flow<List<DealPresentation>>

    suspend fun savePreferredCurrency(currency: Currency)

    fun preferredCurrency(): Flow<Currency>
}
