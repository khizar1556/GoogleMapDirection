package com.example.khizar_kotlin.features.direction.di

import com.example.khizar_kotlin.features.direction.data.remote.GoogleDirectionApi
import com.example.khizar_kotlin.features.direction.data.repository.DirectionRepositoryImpl
import com.example.khizar_kotlin.features.direction.domain.repository.DirectionRepository
import com.example.khizar_kotlin.features.direction.domain.use_case.GetDirectionInfo
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object GoogleDirectionModule {

    @Provides
    @Singleton
    fun provideGetDirectionInfo(repository: DirectionRepository): GetDirectionInfo {
        return GetDirectionInfo(repository = repository)
    }

    @Provides
    @Singleton
    fun provideDirectionInfoRepository(api: GoogleDirectionApi): DirectionRepository{
        return DirectionRepositoryImpl(api = api)
    }

    @Provides
    @Singleton
    fun provideGooglePlacesApi(): GoogleDirectionApi{
        return Retrofit.Builder()
            .baseUrl(GoogleDirectionApi.BASE_URL)
            .addConverterFactory(GsonConverterFactory.create())
            .build()
            .create(GoogleDirectionApi::class.java)
    }
}