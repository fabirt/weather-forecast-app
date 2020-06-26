package com.fabirt.weatherforecast.presentation.weather

import android.Manifest
import android.content.pm.PackageManager
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.viewModels
import androidx.lifecycle.Observer
import com.fabirt.weatherforecast.core.constants.LOCATION_PERMISSION_REQUEST_CODE
import com.fabirt.weatherforecast.databinding.FragmentWeatherBinding
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class WeatherFragment : Fragment() {

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

    private fun setupObservers() {
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
                binding.location = it
                binding.locationText.text = it.name
            }
        })

        viewModel.failure.observe(viewLifecycleOwner, Observer { failure ->
            failure?.let {
                Toast.makeText(requireContext(), it.message, Toast.LENGTH_LONG).show()
                viewModel.clearFailure()
            }
        })
    }
}
