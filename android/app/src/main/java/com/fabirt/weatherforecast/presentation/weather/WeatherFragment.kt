package com.fabirt.weatherforecast.presentation.weather

import android.Manifest
import android.content.pm.PackageManager
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.app.ActivityCompat
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider

import com.fabirt.weatherforecast.R
import com.fabirt.weatherforecast.core.constants.LOCATION_PERMISSION_REQUEST_CODE
import com.fabirt.weatherforecast.data.database.getDatabase
import com.fabirt.weatherforecast.data.network.WeatherApiService
import com.fabirt.weatherforecast.data.providers.LocationProviderImpl
import com.fabirt.weatherforecast.data.providers.UpdateTimeProvider
import com.fabirt.weatherforecast.data.providers.UpdateTimeProviderImpl
import com.fabirt.weatherforecast.data.repository.WeatherRepositoryImpl
import com.fabirt.weatherforecast.databinding.FragmentWeatherBinding
import com.google.android.gms.location.FusedLocationProviderClient

class WeatherFragment : Fragment() {

    private lateinit var viewModel: WeatherViewModel
    private lateinit var binding: FragmentWeatherBinding

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentWeatherBinding.inflate(inflater, container, false)

        // Dependency injection
        val weatherApiService = WeatherApiService()
        val weatherDatabase = getDatabase(requireContext())
        val updateTimeProvider = UpdateTimeProviderImpl(requireContext())
        val locationProvider = LocationProviderImpl(
            requireActivity(),
            FusedLocationProviderClient(requireActivity())
        )
        val weatherRepository = WeatherRepositoryImpl(
            weatherService = weatherApiService,
            updateTimeProvider = updateTimeProvider,
            weatherDao = weatherDatabase.weatherDao,
            locationProvider = locationProvider
        )
        val viewModelFactory = WeatherViewModelFactory(weatherRepository)

        viewModel = ViewModelProvider(this, viewModelFactory).get(WeatherViewModel::class.java)

        viewModel.currentWeather.observe(viewLifecycleOwner, Observer { weather ->
            weather?.let {
                binding.weather = it
                binding.humidityText.text = "${it.humidity}%"
                binding.pressureText.text = "${it.pressure} mb"
                binding.windSpeedText.text = "${it.windSpeed} km/h"
                binding.temperatureText.text = "${it.temperature}º"
                binding.realFeelText.text = "${it.feelslike}º"
                binding.windDirectionText.text = it.windDir
                binding.visibilityText.text = "${it.visibility} km"
                binding.weatherDescriptionText.text = it.weatherDescription
            }
        })

        viewModel.currentLocation.observe(viewLifecycleOwner, Observer { location ->
            location?.let {
                binding.location = location
                binding.locationText.text = location.name
            }
        })

        return binding.root
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        requestPermissions(
            arrayOf(Manifest.permission.ACCESS_COARSE_LOCATION),
            LOCATION_PERMISSION_REQUEST_CODE
        )
    }

    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<out String>,
        grantResults: IntArray
    ) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        if (requestCode == LOCATION_PERMISSION_REQUEST_CODE) {
            if (grantResults.isNotEmpty() && grantResults.first() == PackageManager.PERMISSION_GRANTED) {
                viewModel.getCurrenWeather()
            }
        }
    }

}
