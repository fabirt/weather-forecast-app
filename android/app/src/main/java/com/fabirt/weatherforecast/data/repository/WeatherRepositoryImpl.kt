package com.fabirt.weatherforecast.data.repository

import androidx.lifecycle.Transformations
import com.fabirt.weatherforecast.data.database.WeatherDao
import com.fabirt.weatherforecast.data.models.WeatherData
import com.fabirt.weatherforecast.data.network.WeatherApiService
import com.fabirt.weatherforecast.data.providers.LocationProvider
import com.fabirt.weatherforecast.data.providers.UpdateTimeProvider
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
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
                val date = LocalDate.ofEpochDay(location.localtimeEpoch / 86400000L)
                location.copy(localtime = date.format(formatter))
            }
        }

    override suspend fun fetchCurrentWeather() {
        val fixedData = WeatherData.getFixed()
        try {
            withContext(Dispatchers.IO) {
                val location = locationProvider.getPreferredLocationString()
                if (updateTimeProvider.isCurrentWeatherUpdateNeeded()) {
                    delay(1000)
                    // val weatherData = weatherService.getCurrentWeather(location")
                    weatherDao.upsertCurrentWeather(fixedData.currentWeather)
                    weatherDao.upsertCurrentWeatherLocation(fixedData.location)
                    updateTimeProvider.setLatestUpdateTime(System.currentTimeMillis())
                }
            }
        } catch (e: Exception) {

        }
    }
}