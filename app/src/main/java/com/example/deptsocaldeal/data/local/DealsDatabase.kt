package com.example.deptsocaldeal.data.local

import androidx.room.Database
import androidx.room.RoomDatabase

@Database(
    entities = [DealEntity::class, PreferredCurrencyEntity::class],
    version = 2
)
abstract class DealsDatabase : RoomDatabase() {
    internal abstract val dealsDao: DealsDao
}
