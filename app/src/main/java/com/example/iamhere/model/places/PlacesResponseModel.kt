package com.example.iamhere.model.places

data class PlacesResponseModel(
    val Note: String,
    val PlaceID: String,
    val PlaceName: String,
    val latituideFrom: String,
    val latituideTo: String,
    val longituideFrom: String,
    val longituideTo: String
)