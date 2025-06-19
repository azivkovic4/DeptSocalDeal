package com.example.deptsocaldeal.di

import android.content.Context
import android.net.ConnectivityManager
import androidx.room.Room
import com.example.deptsocaldeal.data.local.DealsDatabase
import com.example.deptsocaldeal.data.services.DealsApi
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import okhttp3.OkHttpClient
import retrofit2.Retrofit
import retrofit2.converter.moshi.MoshiConverterFactory
import retrofit2.create
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object ApiModule {

    @Provides
    @Singleton
    fun provideSuperheroesOkHttpClient(): OkHttpClient {
        val builder = OkHttpClient.Builder()
        return builder.build()
    }


    @Singleton
    @Provides
    fun providesRetrofit(okHttpClient: OkHttpClient): DealsApi {
        return Retrofit.Builder()
            .client(okHttpClient)
            .baseUrl(DealsApi.DEALS_BASE_URL)
            .addConverterFactory(MoshiConverterFactory.create())
            .build()
            .create()
    }

    @Singleton
    @Provides
    fun provideConnectivityManager(@ApplicationContext context: Context): ConnectivityManager =
        context.getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager

    @Provides
    @Singleton
    internal fun provideDealsDB(@ApplicationContext context: Context): DealsDatabase {
        return Room.databaseBuilder(
            context,
            DealsDatabase::class.java,
            "DealsDatabase.db"
        ).build()
    }
}