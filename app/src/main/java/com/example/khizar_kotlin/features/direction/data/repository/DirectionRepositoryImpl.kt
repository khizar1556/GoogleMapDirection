package com.example.khizar_kotlin.features.direction.data.repository

import com.example.khizar_kotlin.common.ResultResource
import com.example.khizar_kotlin.features.direction.data.remote.GoogleDirectionApi
import com.example.khizar_kotlin.features.direction.domain.model.GooglePlacesInfo
import com.example.khizar_kotlin.features.direction.domain.repository.DirectionRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import retrofit2.HttpException
import java.io.IOException

class DirectionRepositoryImpl(private val api: GoogleDirectionApi):
    DirectionRepository {
    override fun getDirection(
        origin: String,
        destination: String,
        key: String
    ): Flow<ResultResource<GooglePlacesInfo>> = flow{
        emit(ResultResource.Loading())
        try {
            val directionData = api.getDirection(origin = origin, destination = destination, key=key)
            emit(ResultResource.Success(data = directionData))
        }catch (e: HttpException){
            emit(ResultResource.Error(message = "Oops something is not right: $e"))
        }catch (e: IOException){
            emit(ResultResource.Error(message = "No Internet Connection: $e"))
        }
    }

}