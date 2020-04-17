package com.fabirt.weatherforecast.data.repository

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.Transformations
import com.fabirt.weatherforecast.data.database.WeatherDao
import com.fabirt.weatherforecast.data.database.getDatabase
import com.fabirt.weatherforecast.data.models.CurrentWeather
import com.fabirt.weatherforecast.data.models.WeatherData
import com.fabirt.weatherforecast.data.models.WeatherLocation
import com.fabirt.weatherforecast.data.network.WeatherApiService
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.withContext
import org.threeten.bp.LocalDate
import org.threeten.bp.format.DateTimeFormatter
import java.lang.Exception

class WeatherRepositoryImpl(
    private val weatherService: WeatherApiService,
    private val weatherDao: WeatherDao
) : WeatherRepository {

    override val currentWeather = weatherDao.getCurrentWeather()

    override val currentLocation =
        Transformations.map(weatherDao.getCurrentWeatherLocation()) { location ->
            location?.let {
                val formatter = DateTimeFormatter.ofPattern("EEE dd MMMM yyyy")
                val date = LocalDate.ofEpochDay(location.localtimeEpoch / 86400000L)
                location.copy(localtime = date.format(formatter))
            }
        }

    override suspend fun fetchCurrentWeather() {
        val fixedData = WeatherData.getFixed()
        try {
            withContext(Dispatchers.IO) {
                delay(1000)
                weatherDao.upsertCurrentWeather(fixedData.currentWeather)
                weatherDao.upsertCurrentWeatherLocation(fixedData.location)
            }
        } catch (e: Exception) {
            weatherDao.upsertCurrentWeather(fixedData.currentWeather)
            weatherDao.upsertCurrentWeatherLocation(fixedData.location)
         }
    }
}