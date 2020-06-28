package com.fabirt.weatherforecast.presentation.weather

import android.Manifest
import android.content.pm.PackageManager
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.content.ContextCompat
import androidx.fragment.app.viewModels
import androidx.lifecycle.Observer
import com.fabirt.weatherforecast.core.constants.LOCATION_PERMISSION_REQUEST_CODE
import com.fabirt.weatherforecast.core.utils.DialogManager
import com.fabirt.weatherforecast.core.error.Failure
import com.fabirt.weatherforecast.core.utils.SettingsManager
import com.fabirt.weatherforecast.core.utils.TransitionsHelper
import com.fabirt.weatherforecast.data.models.WeatherLocation
import com.fabirt.weatherforecast.databinding.FragmentWeatherBinding
import com.fabirt.weatherforecast.domain.entities.CurrentWeather
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class WeatherFragment : Fragment() {

    private val locationPermission = Manifest.permission.ACCESS_FINE_LOCATION
    private val viewModel: WeatherViewModel by viewModels()
    private lateinit var binding: FragmentWeatherBinding

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentWeatherBinding.inflate(inflater, container, false)
        setupObservers()
        return binding.root
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        TransitionsHelper.setupTransitions(this)
        checkPermissions()
    }

    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<out String>,
        grantResults: IntArray
    ) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        if (requestCode == LOCATION_PERMISSION_REQUEST_CODE && grantResults.isNotEmpty()) {
            val result = grantResults.first()
            if (result == PackageManager.PERMISSION_GRANTED) {
                viewModel.requestCurrenWeather()
            } else if (result == PackageManager.PERMISSION_DENIED) {
                DialogManager.show(
                    requireContext(),
                    title = Failure.LocationPermissionNotGrantedFailure.title,
                    body = Failure.LocationPermissionNotGrantedFailure.message,
                    positiveText = "Settings",
                    onConfirm = {
                        SettingsManager.openAppSettings(requireContext())
                    }
                )
            }
        }
    }

    private fun checkPermissions() {
        if (ContextCompat.checkSelfPermission(
                requireContext(),
                locationPermission
            ) == PackageManager.PERMISSION_GRANTED
        ) {
            viewModel.requestCurrenWeather()
        } else {
            requestLocationPermissions()
        }
    }

    private fun requestLocationPermissions() {
        if (shouldShowRequestPermissionRationale(locationPermission)) {
            DialogManager.show(
                requireContext(),
                title = Failure.LocationPermissionNotGrantedFailure.title,
                body = Failure.LocationPermissionNotGrantedFailure.message,
                positiveText = "Allow",
                onConfirm = {
                    requestPermissions(
                        arrayOf(locationPermission),
                        LOCATION_PERMISSION_REQUEST_CODE
                    )
                }
            )
        } else {
            requestPermissions(
                arrayOf(locationPermission),
                LOCATION_PERMISSION_REQUEST_CODE
            )
        }
    }

    private fun setupObservers() {
        viewModel.currentWeather.observe(viewLifecycleOwner, Observer { weather ->
            weather?.let(this::updateWeatherView)
        })

        viewModel.currentLocation.observe(viewLifecycleOwner, Observer { location ->
            location?.let(this::updateLocationView)
        })

        viewModel.failure.observe(viewLifecycleOwner, Observer { failure ->
            failure?.let(::handleFailure)
        })
    }

    private fun updateWeatherView(weather: CurrentWeather) {
        binding.weather = weather
        binding.humidityText.text = "${weather.humidity}%"
        binding.pressureText.text = "${weather.pressure} mb"
        binding.windSpeedText.text = "${weather.windSpeed} km/h"
        binding.temperatureText.text = "${weather.temperature}º"
        binding.realFeelText.text = "${weather.feelslike}º"
        binding.windDirectionText.text = weather.windDir
        binding.visibilityText.text = "${weather.visibility} km"
        binding.weatherDescriptionText.text = weather.weatherDescription
    }

    private fun updateLocationView(location: WeatherLocation) {
        binding.location = location
        binding.locationText.text = location.name
    }

    private fun handleFailure(failure: Failure) {
        val title = failure.title
        val body = failure.message
        var actionText = "OK"
        var onConfirm = {}
        when (failure) {
            Failure.LocationPermissionNotGrantedFailure -> {
                actionText = "Settings"
                onConfirm = {
                    SettingsManager.openAppSettings(requireContext())
                }
            }
            Failure.LatestLocationNotFoundFailure -> {
                actionText = "Settings"
                onConfirm = {
                    SettingsManager.openLocationSettings(requireContext())
                }
            }
            Failure.NetworkFailure -> {
                actionText = "Settings"
                onConfirm = {
                    SettingsManager.openWifiSettings(requireContext())
                }
            }
            Failure.UnexpectedFailure -> {
                actionText = "Retry"
                onConfirm = {
                    viewModel.requestCurrenWeather()
                }
            }
        }
        DialogManager.show(
            requireContext(),
            title = title,
            body = body,
            positiveText = actionText,
            onConfirm = onConfirm
        )
        viewModel.clearFailure()
    }

}