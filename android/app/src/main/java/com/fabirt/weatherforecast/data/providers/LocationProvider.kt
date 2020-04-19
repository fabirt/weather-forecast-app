package com.fabirt.weatherforecast.data.providers

interface LocationProvider {
    /**
     * @return the user's location latitude and longitude in [String] format.
     * @throws Exception if location permissions aren't granted or if there is no location
     * stored in the device.
     */
    suspend fun getPreferredLocationString(): String
}