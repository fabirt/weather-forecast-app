package com.fabirt.weatherforecast.data.models


import com.google.gson.annotations.SerializedName

data class WeatherData(
    @SerializedName("current")
    val currentWeather: CurrentWeather,
    @SerializedName("location")
    val location: WeatherLocation,
    @SerializedName("request")
    val request: WeatherRequest
) {
    companion object {
        // weatherIcons = listOf("https://assets.weatherstack.com/images/wsymbols01_png_64/wsymbol_0001_sunny.png"),
        // weatherDescriptions = listOf("Sunny"),
        fun getFixed(): WeatherData {
            return WeatherData(
                CurrentWeather(
                    observationTime = "12:14 PM",
                    temperature = 13,
                    weatherCode = 113,
                    windSpeed = 0,
                    windDegree = 349,
                    windDir = "N",
                    pressure = 1010,
                    precip = 0,
                    humidity = 90,
                    cloudcover = 0,
                    feelslike = 13,
                    uvIndex = 4,
                    visibility = 16
                ),
                WeatherLocation(
                    name = "New York",
                    country = "United States of America",
                    region = "New York",
                    lat = "40.714",
                    lon = "-74.006",
                    timezoneId = "America/New_York",
                    localtime = "2019-09-07 08:14",
                    localtimeEpoch = 1587144047703,
                    utcOffset = "-4.0"
                ),
                WeatherRequest(
                    type = "City",
                    query = "New York, United States of America",
                    language = "en",
                    unit = "m"
                )
            )
        }
    }
}

/*
{
    "request": {
        "type": "City",
        "query": "New York, United States of America",
        "language": "en",
        "unit": "m"
    },
    "location": {
        "name": "New York",
        "country": "United States of America",
        "region": "New York",
        "lat": "40.714",
        "lon": "-74.006",
        "timezone_id": "America/New_York",
        "localtime": "2019-09-07 08:14",
        "localtime_epoch": 1567844040,
        "utc_offset": "-4.0"
    },
    "current": {
        "observation_time": "12:14 PM",
        "temperature": 13,
        "weather_code": 113,
        "weather_icons": [
            "https://assets.weatherstack.com/images/wsymbols01_png_64/wsymbol_0001_sunny.png"
        ],
        "weather_descriptions": [
            "Sunny"
        ],
        "wind_speed": 0,
        "wind_degree": 349,
        "wind_dir": "N",
        "pressure": 1010,
        "precip": 0,
        "humidity": 90,
        "cloudcover": 0,
        "feelslike": 13,
        "uv_index": 4,
        "visibility": 16
    }
}

*/