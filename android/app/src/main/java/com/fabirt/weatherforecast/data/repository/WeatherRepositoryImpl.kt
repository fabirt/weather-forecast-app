package com.fabirt.weatherforecast.data.repository

import android.util.Log
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
import java.lang.Exception
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

    override suspend fun fetchCurrentWeather() {
        try {
            withContext(Dispatchers.IO) {
                Log.i("WeatherRepositoryImpl", "fetchCurrentWeather")
                val location = locationProvider.getPreferredLocationString()
                if (updateTimeProvider.isCurrentWeatherUpdateNeeded()) {
                    Log.i("WeatherRepositoryImpl", "isCurrentWeatherUpdateNeeded")
                    Log.i("WeatherRepositoryImpl", location)
                    val weatherResponse = weatherService.getCurrentWeatherAsync(location, "").await()
                    Log.i("WeatherRepositoryImpl", weatherResponse.toString())
                    weatherDao.upsertCurrentWeather(weatherResponse.currentWeather.asDomainEntity())
                    weatherDao.upsertCurrentWeatherLocation(weatherResponse.location)
                    updateTimeProvider.setLatestUpdateTime(System.currentTimeMillis())
                }
            }
        } catch (e: Exception) {
            Log.i("WeatherRepositoryImpl", e.message.toString())

        }
    }
}