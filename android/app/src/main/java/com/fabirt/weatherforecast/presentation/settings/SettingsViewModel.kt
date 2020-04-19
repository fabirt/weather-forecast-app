package com.fabirt.weatherforecast.presentation.settings

import android.Manifest
import android.content.Context
import android.content.pm.PackageManager
import android.location.LocationManager
import androidx.core.content.ContextCompat
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel

class SettingsViewModel(private val applicationContext: Context) : ViewModel() {

    private val _locationEnabled = MutableLiveData<Boolean>()
    val locationEnabled: LiveData<Boolean>
        get() = _locationEnabled

    private val _locationPermissionsGranted = MutableLiveData<Boolean>()
    val locationPermissionsGranted: LiveData<Boolean>
        get() = _locationPermissionsGranted

    fun checkSettings() {
        _locationEnabled.value = isGpsProviderEnabled()
        _locationPermissionsGranted.value = hasLocationPermission()
    }

    private fun hasLocationPermission(): Boolean {
        return ContextCompat.checkSelfPermission(
            applicationContext,
            Manifest.permission.ACCESS_COARSE_LOCATION
        ) == PackageManager.PERMISSION_GRANTED
    }

    private fun isGpsProviderEnabled(): Boolean {
        val locationManager =
            applicationContext.getSystemService(Context.LOCATION_SERVICE) as LocationManager
        return locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER)
    }
}
