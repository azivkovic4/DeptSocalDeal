package com.example.deptsocaldeal.data.services

import com.example.deptsocaldeal.data.models.DealDto
import com.example.deptsocaldeal.data.models.DealsResult
import retrofit2.http.GET
import retrofit2.http.Query

interface DealsApi {
    @GET("deals.json")
    suspend fun discoverDeals(): DealsResult

    @GET("details.json")
    suspend fun dealDetails(@Query("id") id: String): DealDto

    companion object {
        const val DEALS_BASE_URL = "https://media.socialdeal.nl/demo/"
        const val BASE_IMAGE_URL = "https://images.socialdeal.nl"
    }
}
