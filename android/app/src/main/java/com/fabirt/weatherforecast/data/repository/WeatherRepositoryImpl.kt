package com.fabirt.weatherforecast.data.repository

import androidx.lifecycle.Transformations
import com.fabirt.weatherforecast.core.error.Failure
import com.fabirt.weatherforecast.core.extensions.asDomainEntity
import com.fabirt.weatherforecast.core.other.Either
import com.fabirt.weatherforecast.data.database.WeatherDao
import com.fabirt.weatherforecast.data.models.WeatherLocation
import com.fabirt.weatherforecast.data.network.WeatherApiService
import com.fabirt.weatherforecast.data.providers.LocationProvider
import com.fabirt.weatherforecast.data.providers.UpdateTimeProvider
import com.fabirt.weatherforecast.domain.entities.CurrentWeather
import com.fabirt.weatherforecast.domain.repository.WeatherRepository
import org.threeten.bp.LocalDate
import org.threeten.bp.format.DateTimeFormatter
import java.util.*
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class WeatherRepositoryImpl @Inject constructor(
    private val weatherDao: WeatherDao,
    private val weatherService: WeatherApiService,
    private val updateTimeProvider: UpdateTimeProvider,
    private val locationProvider: LocationProvider
) : BaseRepository(), WeatherRepository {

    override val currentWeather = weatherDao.readCurrentWeather()

    override val currentLocation =
        Transformations.map(weatherDao.readCurrentWeatherLocation()) { location ->
            location?.let {
                val formatter = DateTimeFormatter.ofPattern("EEE dd MMMM yyyy", Locale.ENGLISH)
                val date = LocalDate.ofEpochDay(it.localtimeEpoch / 86400L)
                it.copy(localtime = date.format(formatter))
            }
        }

    override suspend fun fetchCurrentWeatherRacionale(): Either<Failure, Unit> {
        return transformExceptionsAsync {
            if (updateTimeProvider.isCurrentWeatherUpdateNeeded()) {
                fetchCurrentWeather()
            }
            Either.Right(Unit)
        }
    }

    override suspend fun fetchCurrentWeatherMandatory(): Either<Failure, CurrentWeather> {
        return transformExceptionsAsync {
            val result = fetchCurrentWeather()
            Either.Right(result.first)
        }
    }

    private suspend fun fetchCurrentWeather(): Pair<CurrentWeather, WeatherLocation> {
        val location = locationProvider.requestPreferredLocation()
        val weatherResponse = weatherService.requestCurrentWeatherAsync(location, "").await()
        weatherDao.upsertCurrentWeather(weatherResponse.currentWeather.asDomainEntity())
        weatherDao.upsertCurrentWeatherLocation(weatherResponse.location)
        updateTimeProvider.saveLastUpdateTime(System.currentTimeMillis())
        return Pair(weatherResponse.currentWeather.asDomainEntity(), weatherResponse.location)
    }
}