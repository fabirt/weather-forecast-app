package com.fabirt.weatherforecast.domain.entities

import androidx.room.Entity
import androidx.room.PrimaryKey
import com.fabirt.weatherforecast.core.constants.CURRENT_WEATHER_ID

@Entity(tableName = "current_weather")
data class CurrentWeather(
    val cloudcover: Int,
    val feelslike: Int,
    val humidity: Int,
    val observationTime: String,
    val precip: Int,
    val pressure: Int,
    val temperature: Int,
    val uvIndex: Int,
    val visibility: Int,
    val weatherCode: Int,
    val weatherDescription: String,
    // val weatherIcons: List<String>,
    val windDegree: Int,
    val windDir: String,
    val windSpeed: Int
) {
    @PrimaryKey(autoGenerate = false)
    var id: Int = CURRENT_WEATHER_ID
}