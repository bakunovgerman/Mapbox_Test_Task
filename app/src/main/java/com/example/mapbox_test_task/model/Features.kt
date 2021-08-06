package com.example.mapbox_test_task.model

data class Features (
    val type : String,
    val id : Long,
    val geometry : Geometry,
    val properties : Properties
)
