package com.fabirt.weatherforecast.data.providers

import android.Manifest
import android.content.Context
import android.content.pm.PackageManager
import android.location.Location
import android.util.Log
import androidx.core.content.ContextCompat
import com.fabirt.weatherforecast.core.asDeferredAsync
import com.google.android.gms.location.FusedLocationProviderClient
import kotlinx.coroutines.Deferred

class LocationProviderImpl(
    context: Context,
    private val fusedLocationProviderClient: FusedLocationProviderClient
) : PreferencesProvider(context), LocationProvider {

    override suspend fun getPreferredLocationString(): String {
        return try {
            val location = getLastDeviceLocation().await() ?: throw Exception()
            Log.i("LocationProviderImpl", "${location.latitude},${location.longitude}")
            "${location.latitude},${location.longitude}"
        } catch (e: Exception) {
            Log.i("LocationProviderImpl", e.toString())
            // "Barranquilla"
            throw e
        }
    }

    private fun getLastDeviceLocation(): Deferred<Location?> {
        return if (hasLocationPermission())
            fusedLocationProviderClient.lastLocation.asDeferredAsync()
        else
            throw Exception()
    }

    private fun hasLocationPermission(): Boolean {
        return ContextCompat.checkSelfPermission(
            context.applicationContext,
            Manifest.permission.ACCESS_COARSE_LOCATION
        ) == PackageManager.PERMISSION_GRANTED
    }
}