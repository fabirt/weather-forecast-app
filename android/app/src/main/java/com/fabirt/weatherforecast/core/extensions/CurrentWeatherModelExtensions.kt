package com.fabirt.weatherforecast.core.extensions

import com.fabirt.weatherforecast.data.models.CurrentWeatherModel
import com.fabirt.weatherforecast.domain.entities.CurrentWeather

fun CurrentWeatherModel.asDomainEntity(): CurrentWeather {
    return CurrentWeather(
        observationTime = this.observationTime,
        temperature = this.temperature,
        weatherCode = this.weatherCode,
        windSpeed = this.windSpeed,
        windDegree = this.windDegree,
        windDir = this.windDir,
        pressure = this.pressure,
        precip = this.precip,
        humidity = this.humidity,
        cloudcover = this.cloudcover,
        feelslike = this.feelslike,
        uvIndex = this.uvIndex,
        visibility = this.visibility,
        weatherDescription = this.weatherDescriptions.first()
    )
}