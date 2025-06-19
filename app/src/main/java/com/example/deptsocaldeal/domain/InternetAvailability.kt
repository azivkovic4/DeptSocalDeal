package com.example.deptsocaldeal.domain

import kotlinx.coroutines.flow.Flow

interface InternetAvailability {
    val internetConnection: Flow<Status>
    fun hasInternet(): Boolean
}

enum class Status {
    Available, Unavailable, Losing, Lost, Unknown
}