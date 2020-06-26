package com.fabirt.weatherforecast.data.repository

import androidx.lifecycle.Transformations
import com.fabirt.weatherforecast.core.error.AppException
import com.fabirt.weatherforecast.core.error.Failure
import com.fabirt.weatherforecast.core.error.NetworkFailure
import com.fabirt.weatherforecast.core.error.UnexpectedFailure
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

    override suspend fun fetchCurrentWeatherRacionale(): Either<Failure, Unit> {
        try {
            return withContext(Dispatchers.IO) {
                if (updateTimeProvider.isCurrentWeatherUpdateNeeded()) {
                    fetchCurrentWeather()
                }
                return@withContext Either.Right(Unit)
            }
        } catch (e: Exception) {
            return when (e) {
                is AppException -> Either.Left(e.toFailure())
                is UnknownHostException -> Either.Left(NetworkFailure())
                else -> Either.Left(UnexpectedFailure())
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
        val location = locationProvider.getPreferredLocationString()
        val weatherResponse = weatherService.getCurrentWeatherAsync(location, "").await()
        weatherDao.upsertCurrentWeather(weatherResponse.currentWeather.asDomainEntity())
        weatherDao.upsertCurrentWeatherLocation(weatherResponse.location)
        updateTimeProvider.setLatestUpdateTime(System.currentTimeMillis())
        return Pair(weatherResponse.currentWeather.asDomainEntity(), weatherResponse.location)
    }
}