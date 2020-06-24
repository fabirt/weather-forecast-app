package com.fabirt.weatherforecast.data.network

import com.fabirt.weatherforecast.data.models.WeatherResponse
import kotlinx.coroutines.Deferred
import retrofit2.http.GET
import retrofit2.http.Query

interface WeatherApiService {
    /**
     * Request the current weather information for the given [location].
     * The response is retrieved in the given [languageCode].
     */
    @GET("current")
    fun getCurrentWeatherAsync(
        @Query("query") location: String,
        @Query("language") languageCode: String
    ): Deferred<WeatherResponse>
}