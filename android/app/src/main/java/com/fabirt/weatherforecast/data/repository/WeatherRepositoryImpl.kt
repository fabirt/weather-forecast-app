package com.fabirt.weatherforecast.data.repository

import android.util.Log
import androidx.lifecycle.Transformations
import com.fabirt.weatherforecast.core.extensions.asDomainEntity
import com.fabirt.weatherforecast.data.database.WeatherDao
import com.fabirt.weatherforecast.data.models.WeatherLocation
import com.fabirt.weatherforecast.data.network.WeatherApiService
import com.fabirt.weatherforecast.data.providers.LocationProvider
import com.fabirt.weatherforecast.data.providers.UpdateTimeProvider
import com.fabirt.weatherforecast.domain.entities.CurrentWeather
import com.fabirt.weatherforecast.domain.repository.WeatherRepository
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.withContext
import org.threeten.bp.LocalDate
import org.threeten.bp.format.DateTimeFormatter
import java.util.*
import javax.inject.Inject

class WeatherRepositoryImpl @Inject constructor(
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
                val date = LocalDate.ofEpochDay(it.localtimeEpoch / 86400L)
                it.copy(localtime = date.format(formatter))
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
    
    override suspend fun fetchCurrentWeatherMandatory(): Pair<CurrentWeather?, WeatherLocation?> {
        return try {
            fetchCurrentWeather()
        } catch (e: Exception) {
            Pair(null, null)
        }
    }

    private suspend fun fetchCurrentWeather(): Pair<CurrentWeather, WeatherLocation> {
        val location = locationProvider.getPreferredLocationString()
        val weatherResponse = weatherService.getCurrentWeatherAsync(location, "").await()
        weatherDao.upsertCurrentWeather(weatherResponse.currentWeather.asDomainEntity())
        weatherDao.upsertCurrentWeatherLocation(weatherResponse.location)
        updateTimeProvider.setLatestUpdateTime(System.currentTimeMillis())
        return Pair(weatherResponse.currentWeather.asDomainEntity(), weatherResponse.location)
    }
}