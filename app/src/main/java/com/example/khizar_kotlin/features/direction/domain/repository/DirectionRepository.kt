package com.example.khizar_kotlin.features.direction.domain.repository

import com.example.khizar_kotlin.common.ResultResource
import com.example.khizar_kotlin.features.direction.domain.model.GooglePlacesInfo
import kotlinx.coroutines.flow.Flow

interface DirectionRepository {
    fun getDirection(origin: String, destination: String, key: String): Flow<ResultResource<GooglePlacesInfo>>
}