package com.example.deptsocaldeal.data.local

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import kotlinx.coroutines.flow.Flow

@Dao
internal interface DealsDao {
    @Query("SELECT * FROM deals WHERE isFavorite = 1")
    fun getFavoriteDeals(): Flow<List<DealEntity>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertDeal(deal: DealEntity)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun savePreferredCurrency(currency: PreferredCurrencyEntity)

    @Query("SELECT * FROM preferred_currency WHERE id = 0 LIMIT 1")
    fun getPreferredCurrency(): Flow<PreferredCurrencyEntity?>
}
