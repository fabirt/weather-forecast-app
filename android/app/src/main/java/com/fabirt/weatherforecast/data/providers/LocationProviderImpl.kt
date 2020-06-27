package com.fabirt.weatherforecast.data.providers

import android.Manifest
import android.annotation.SuppressLint
import android.content.Context
import android.content.pm.PackageManager
import android.location.Location
import androidx.core.content.ContextCompat
import com.fabirt.weatherforecast.core.error.AppException
import com.fabirt.weatherforecast.core.extensions.asDeferredAsync
import com.google.android.gms.location.FusedLocationProviderClient
import dagger.hilt.android.qualifiers.ApplicationContext
import kotlinx.coroutines.Deferred
import javax.inject.Inject

class LocationProviderImpl @Inject constructor(
    @ApplicationContext context: Context
) : PreferencesProvider(context), LocationProvider {

    private val fusedLocationProviderClient = FusedLocationProviderClient(context)

    override suspend fun requestPreferredLocation(): String {
        val location = requestLastDeviceLocationAsync().await()
            ?: throw AppException.LatestLocationNotFoundException()
        return "${location.latitude},${location.longitude}"
    }

    @SuppressLint("MissingPermission")
    private fun requestLastDeviceLocationAsync(): Deferred<Location?> {
        if (hasLocationPermission()) {
            return fusedLocationProviderClient.lastLocation.asDeferredAsync()
        } else {
            throw AppException.LocationPermissionNotGrantedException()
        }
    }

    private fun hasLocationPermission(): Boolean {
        return ContextCompat.checkSelfPermission(
            context.applicationContext,
            Manifest.permission.ACCESS_FINE_LOCATION
        ) == PackageManager.PERMISSION_GRANTED
    }
}