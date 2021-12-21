package com.nakib.iamhere.model.home

data class NormalHomeResponseModel(
    val PlaceName: String,
    val attDate: String,
    val attPlace: String,
    val fromTime: String,
    val recordId: String,
    val toTime: String,
    val preiod: String,
    val userId: String,
    var checked : Boolean = false
)