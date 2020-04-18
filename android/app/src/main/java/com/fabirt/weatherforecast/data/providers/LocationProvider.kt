package com.fabirt.weatherforecast.data.providers

interface LocationProvider {
    suspend fun getPreferredLocationString(): String
}