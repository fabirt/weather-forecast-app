package com.fabirt.weatherforecast.data.repository

import com.fabirt.weatherforecast.data.models.WeatherData

interface WeatherRepository {
    suspend fun getCurrentWeather(): WeatherData
}