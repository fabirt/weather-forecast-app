package com.fabirt.weatherforecast.data.database

import androidx.room.Database
import androidx.room.RoomDatabase
import com.fabirt.weatherforecast.domain.entities.CurrentWeather
import com.fabirt.weatherforecast.data.models.WeatherLocation

@Database(
    version = 1,
    exportSchema = false,
    entities = [CurrentWeather::class, WeatherLocation::class]
)
abstract class WeatherDatabase : RoomDatabase() {
    /**
     * Weather data access object.
     */
    abstract val weatherDao: WeatherDao
}