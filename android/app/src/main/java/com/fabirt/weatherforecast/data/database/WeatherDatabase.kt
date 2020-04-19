package com.fabirt.weatherforecast.data.database

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import com.fabirt.weatherforecast.domain.entities.CurrentWeather
import com.fabirt.weatherforecast.data.models.WeatherLocation

private lateinit var instance: WeatherDatabase

@Database(entities = [CurrentWeather::class, WeatherLocation::class], version = 1)
abstract class WeatherDatabase : RoomDatabase() {
    /**
     * Weather data access object.
     */
    abstract val weatherDao: WeatherDao
}

fun getDatabase(context: Context): WeatherDatabase {
    synchronized(WeatherDatabase::class.java) {
        if (!::instance.isInitialized) {
            instance = Room.databaseBuilder(
                context.applicationContext,
                WeatherDatabase::class.java,
                "weather"
            ).build()
        }
    }
    return instance
}