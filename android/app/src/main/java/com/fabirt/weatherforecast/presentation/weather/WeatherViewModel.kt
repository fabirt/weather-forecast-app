package com.fabirt.weatherforecast.presentation.weather

import androidx.lifecycle.ViewModel
import com.fabirt.weatherforecast.domain.repository.WeatherRepository
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.launch

class WeatherViewModel(private val repository: WeatherRepository) : ViewModel() {
    private val viewModelJob = Job()
    private val viewModelScope = CoroutineScope(viewModelJob + Dispatchers.Main)

    val currentWeather = repository.currentWeather
    val currentLocation = repository.currentLocation

    init {
        // getCurrenWeather()
    }

    fun getCurrenWeather() {
        viewModelScope.launch {
            repository.fetchCurrentWeatherRacionale()
        }
    }

    override fun onCleared() {
        super.onCleared()
        viewModelJob.cancel()
    }
}
