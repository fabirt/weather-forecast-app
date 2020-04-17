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
import com.fabirt.weatherforecast.databinding.FragmentWeatherBinding

class WeatherFragment : Fragment() {

    private lateinit var viewModel: WeatherViewModel

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val binding = FragmentWeatherBinding.inflate(inflater, container, false)

        viewModel = ViewModelProvider(this).get(WeatherViewModel::class.java)

        viewModel.currentWeather.observe(viewLifecycleOwner, Observer { data ->
            binding.weather = viewModel.currentWeather.value
        })

        return binding.root
    }

}
