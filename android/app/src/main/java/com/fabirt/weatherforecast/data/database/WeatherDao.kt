package com.fabirt.weatherforecast.data.database

import androidx.lifecycle.LiveData
import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.fabirt.weatherforecast.core.constants.CURRENT_WEATHER_ID
import com.fabirt.weatherforecast.core.constants.CURRENT_WEATHER_LOCATION_ID
import com.fabirt.weatherforecast.domain.entities.CurrentWeather
import com.fabirt.weatherforecast.data.models.WeatherLocation

@Dao
interface WeatherDao {
    /**
     * @return [LiveData] with the latest stored [CurrentWeather]
     * object if exist, otherwise `null`. Each observer will be triggered
     * whenever is change in the database,
     */
    @Query("select * from current_weather where id = $CURRENT_WEATHER_ID")
    fun readCurrentWeather(): LiveData<CurrentWeather?>

    /**
     * @return [LiveData] with the latest stored [WeatherLocation]
     * object if exist, otherwise `null`. Each observer will be triggered
     * whenever is change in the database,
     */
    @Query("select * from weather_location where id = $CURRENT_WEATHER_LOCATION_ID")
    fun readCurrentWeatherLocation(): LiveData<WeatherLocation?>

    /**
     * Insert the given [currentWeather] into the database, if there is a previous
     * stored value with the same [CurrentWeather.id], it will be replaced.
     */
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun upsertCurrentWeather(currentWeather: CurrentWeather)

    /**
     * Insert the given [weatherLocation] into the database, if there is a previous
     * stored value with the same [WeatherLocation.id], it will be replaced.
     */
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun upsertCurrentWeatherLocation(weatherLocation: WeatherLocation)
}