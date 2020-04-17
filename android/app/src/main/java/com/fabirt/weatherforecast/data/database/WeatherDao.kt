package com.fabirt.weatherforecast.data.database

import androidx.lifecycle.LiveData
import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.fabirt.weatherforecast.data.models.CURRENT_WEATHER_ID
import com.fabirt.weatherforecast.data.models.CURRENT_WEATHER_LOCATION_ID
import com.fabirt.weatherforecast.data.models.CurrentWeather
import com.fabirt.weatherforecast.data.models.WeatherLocation

@Dao
interface WeatherDao {
    @Query("select * from current_weather where id = $CURRENT_WEATHER_ID")
    fun getCurrentWeather(): LiveData<CurrentWeather?>

    @Query("select * from weather_location where id = $CURRENT_WEATHER_LOCATION_ID")
    fun getCurrentWeatherLocation(): LiveData<WeatherLocation?>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun upsertCurrentWeather(currentWeather: CurrentWeather)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun upsertCurrentWeatherLocation(weatherLocation: WeatherLocation)
}