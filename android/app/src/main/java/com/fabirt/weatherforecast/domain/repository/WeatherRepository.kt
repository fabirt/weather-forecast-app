package com.fabirt.weatherforecast.domain.repository

import androidx.lifecycle.LiveData
import com.fabirt.weatherforecast.domain.entities.CurrentWeather
import com.fabirt.weatherforecast.data.models.WeatherLocation

interface WeatherRepository {
    /**
     * Current weather [LiveData] stored in the database.
     */
    val currentWeather: LiveData<CurrentWeather?>

    /**
     * Current location [LiveData] stored in the database.
     */
    val currentLocation: LiveData<WeatherLocation?>

    /**
     * Obtains data from the API service and updates [currentWeather] and [currentLocation]
     * if the conditions for making the request are met.
     */
    suspend fun fetchCurrentWeatherRacionale()

    /**
     * Obtains data from the API service and updates [currentWeather] and [currentLocation].
     */
    suspend fun fetchCurrentWeatherMandatory()
}