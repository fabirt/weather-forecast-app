package com.fabirt.weatherforecast.data.providers

import android.Manifest
import android.annotation.SuppressLint
import android.content.Context
import android.content.pm.PackageManager
import android.location.Location
import androidx.core.content.ContextCompat
import com.fabirt.weatherforecast.core.error.LatestLocationNotFoundException
import com.fabirt.weatherforecast.core.error.LocationPermissionNotGrantedException
import com.fabirt.weatherforecast.core.extensions.asDeferredAsync
import com.google.android.gms.location.FusedLocationProviderClient
import dagger.hilt.android.qualifiers.ApplicationContext
import kotlinx.coroutines.Deferred
import javax.inject.Inject

class LocationProviderImpl @Inject constructor(
    @ApplicationContext context: Context
) : PreferencesProvider(context), LocationProvider {

    private val fusedLocationProviderClient = FusedLocationProviderClient(context)

    override suspend fun getPreferredLocationString(): String {
        val location =
            getLastDeviceLocationAsync().await() ?: throw LatestLocationNotFoundException()
        return "${location.latitude},${location.longitude}"
    }

    @SuppressLint("MissingPermission")
    private fun getLastDeviceLocationAsync(): Deferred<Location?> {
        if (hasLocationPermission()) {
            return fusedLocationProviderClient.lastLocation.asDeferredAsync()
        } else {
            throw LocationPermissionNotGrantedException()
        }
    }

    private fun hasLocationPermission(): Boolean {
        return ContextCompat.checkSelfPermission(
            context.applicationContext,
            Manifest.permission.ACCESS_COARSE_LOCATION
        ) == PackageManager.PERMISSION_GRANTED
    }
}