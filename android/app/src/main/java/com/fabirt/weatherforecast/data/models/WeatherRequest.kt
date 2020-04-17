package com.fabirt.weatherforecast.data.models


import com.google.gson.annotations.SerializedName

data class WeatherRequest(
    @SerializedName("language")
    val language: String,
    @SerializedName("query")
    val query: String,
    @SerializedName("type")
    val type: String,
    @SerializedName("unit")
    val unit: String
)