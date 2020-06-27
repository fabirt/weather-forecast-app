package com.fabirt.weatherforecast.data.repository

import androidx.lifecycle.Transformations
import com.fabirt.weatherforecast.core.error.AppException
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
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import org.threeten.bp.LocalDate
import org.threeten.bp.format.DateTimeFormatter
import java.net.UnknownHostException
import java.util.*
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class WeatherRepositoryImpl @Inject constructor(
    private val weatherDao: WeatherDao,
    private val weatherService: WeatherApiService,
    private val updateTimeProvider: UpdateTimeProvider,
    private val locationProvider: LocationProvider
) : WeatherRepository {

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
        return try {
            withContext(Dispatchers.IO) {
                if (updateTimeProvider.isCurrentWeatherUpdateNeeded()) {
                    fetchCurrentWeather()
                }
                Either.Right(Unit)
            }
        } catch (e: Exception) {
            when (e) {
                is AppException -> Either.Left(e.toFailure())
                is UnknownHostException -> Either.Left(Failure.NetworkFailure)
                else -> Either.Left(Failure.UnexpectedFailure)
            }
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
        val location = locationProvider.requestPreferredLocation()
        val weatherResponse = weatherService.requestCurrentWeatherAsync(location, "").await()
        weatherDao.upsertCurrentWeather(weatherResponse.currentWeather.asDomainEntity())
        weatherDao.upsertCurrentWeatherLocation(weatherResponse.location)
        updateTimeProvider.saveLastUpdateTime(System.currentTimeMillis())
        return Pair(weatherResponse.currentWeather.asDomainEntity(), weatherResponse.location)
    }
}