package com.example.khizar_kotlin.features.direction.domain.model

data class Routes(
    val summary: String,
    val overview_polyline: OverviewPolyline,
    val legs: List<Legs>
)
