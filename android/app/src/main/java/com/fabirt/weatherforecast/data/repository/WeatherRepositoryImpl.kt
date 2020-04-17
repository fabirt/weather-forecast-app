package com.fabirt.weatherforecast.data.repository

import com.fabirt.weatherforecast.data.models.WeatherData
import com.fabirt.weatherforecast.data.network.WeatherApiService
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.withContext
import java.lang.Exception

class WeatherRepositoryImpl : WeatherRepository {
    private val weatherService = WeatherApiService()

    override suspend fun getCurrentWeather(): WeatherData {
        return try {
            withContext(Dispatchers.IO) {
                delay(1000)
                WeatherData.getFixed()
            }
        } catch (e: Exception) {
            WeatherData.getFixed()
        }
    }
}