package com.fabirt.weatherforecast.presentation.weather

import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider

import com.fabirt.weatherforecast.R
import com.fabirt.weatherforecast.data.database.getDatabase
import com.fabirt.weatherforecast.data.network.WeatherApiService
import com.fabirt.weatherforecast.data.providers.UpdateTimeProvider
import com.fabirt.weatherforecast.data.repository.WeatherRepositoryImpl
import com.fabirt.weatherforecast.databinding.FragmentWeatherBinding

class WeatherFragment : Fragment() {

    private lateinit var viewModel: WeatherViewModel

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val binding = FragmentWeatherBinding.inflate(inflater, container, false)

        val weatherApiService = WeatherApiService()

        val updateTimeProvider = UpdateTimeProvider(requireContext())

        val weatherDatabase = getDatabase(requireContext())

        val weatherRepository = WeatherRepositoryImpl(
            weatherService = weatherApiService,
            updateTimeProvider = updateTimeProvider,
            weatherDao = weatherDatabase.weatherDao
        )

        val viewModelFactory = WeatherViewModelFactory(weatherRepository)

        viewModel = ViewModelProvider(this, viewModelFactory).get(WeatherViewModel::class.java)

        viewModel.currentWeather.observe(viewLifecycleOwner, Observer { weather ->
            weather?.let {
                binding.weather = weather
                binding.humidityText.text = "${weather.humidity}%"
                binding.pressureText.text = "${weather.pressure} Pa"
                binding.windSpeedText.text = "${weather.windSpeed} mph"
                binding.weatherText.text = "${weather.temperature}º"
            }
        })

        viewModel.currentLocation.observe(viewLifecycleOwner, Observer { location ->
            location?.let {
                binding.location = location
            }
        })

        return binding.root
    }

}
