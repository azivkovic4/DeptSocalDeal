package com.example.deptsocaldeal.data.local

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "preferred_currency")
data class PreferredCurrencyEntity(
    @PrimaryKey val id: Int = 0,
    val symbol: String
)
