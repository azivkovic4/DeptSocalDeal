package com.example.deptsocaldeal.di

import com.example.deptsocaldeal.domain.InternetAvailability
import com.example.deptsocaldeal.domain.InternetAvailabilityImpl
import com.example.deptsocaldeal.domain.Repository
import com.example.deptsocaldeal.domain.RepositoryImpl
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
internal abstract class NetworkModule {

    @Binds
    @Singleton
    abstract fun bindNetworkChecker(networkCheckerImpl: InternetAvailabilityImpl): InternetAvailability

    @Binds
    @Singleton
    abstract fun repository(repositoryImpl: RepositoryImpl): Repository
}
