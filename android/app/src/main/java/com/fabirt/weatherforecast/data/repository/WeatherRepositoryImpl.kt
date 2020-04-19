package com.fabirt.weatherforecast.data.repository

import androidx.lifecycle.Transformations
import com.fabirt.weatherforecast.core.extensions.asDomainEntity
import com.fabirt.weatherforecast.data.database.WeatherDao
import com.fabirt.weatherforecast.data.network.WeatherApiService
import com.fabirt.weatherforecast.data.providers.LocationProvider
import com.fabirt.weatherforecast.data.providers.UpdateTimeProvider
import com.fabirt.weatherforecast.domain.repository.WeatherRepository
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import org.threeten.bp.LocalDate
import org.threeten.bp.format.DateTimeFormatter
import java.util.*

class WeatherRepositoryImpl(
    private val weatherDao: WeatherDao,
    private val weatherService: WeatherApiService,
    private val updateTimeProvider: UpdateTimeProvider,
    private val locationProvider: LocationProvider
) : WeatherRepository {

    override val currentWeather = weatherDao.getCurrentWeather()

    override val currentLocation =
        Transformations.map(weatherDao.getCurrentWeatherLocation()) { location ->
            location?.let {
                val formatter = DateTimeFormatter.ofPattern("EEE dd MMMM yyyy", Locale.ENGLISH)
                val date = LocalDate.ofEpochDay(location.localtimeEpoch / 86400L)
                location.copy(localtime = date.format(formatter))
            }
        }

    override suspend fun fetchCurrentWeatherRacionale() {
        try {
            withContext(Dispatchers.IO) {
                if (updateTimeProvider.isCurrentWeatherUpdateNeeded()) {
                    fetchCurrentWeather()
                }
            }
        } catch (e: Exception) {
        }
    }
    
    override suspend fun fetchCurrentWeatherMandatory() {
        try {
            fetchCurrentWeather()
        } catch (e: Exception) {

        }
    }

    private suspend fun fetchCurrentWeather() {
        val location = locationProvider.getPreferredLocationString()
        val weatherResponse = weatherService.getCurrentWeatherAsync(location, "").await()
        weatherDao.upsertCurrentWeather(weatherResponse.currentWeather.asDomainEntity())
        weatherDao.upsertCurrentWeatherLocation(weatherResponse.location)
        updateTimeProvider.setLatestUpdateTime(System.currentTimeMillis())
    }
}