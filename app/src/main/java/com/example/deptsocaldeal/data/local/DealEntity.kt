package com.example.deptsocaldeal.data.local

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "deals")
data class DealEntity(
    @PrimaryKey
    val id: String,
    val title: String,
    val image: String,
    val soldLabel: String,
    val company: String,
    val city: String,
    val symbol:String,
    val discountPrice: String? = null,
    val currentPrice: String,
    val isFavorite: Boolean = false
)
