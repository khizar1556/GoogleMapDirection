package com.example.khizar_kotlin.features.direction.domain.use_case

import com.example.khizar_kotlin.common.ResultResource
import com.example.khizar_kotlin.features.direction.domain.model.GooglePlacesInfo
import com.example.khizar_kotlin.features.direction.domain.repository.DirectionRepository
import kotlinx.coroutines.flow.Flow

class GetDirectionInfo(private val repository: DirectionRepository) {
    operator fun invoke(origin: String, destination: String, key: String): Flow<ResultResource<GooglePlacesInfo>> = repository.getDirection(origin = origin, destination = destination, key = key)
}