package com.example.khizar_kotlin.features.direction.viewmodel

import com.example.khizar_kotlin.features.direction.domain.model.GooglePlacesInfo


data class GoogleDirectionInfoState(val direction: GooglePlacesInfo? = null, val isLoading: Boolean = false)