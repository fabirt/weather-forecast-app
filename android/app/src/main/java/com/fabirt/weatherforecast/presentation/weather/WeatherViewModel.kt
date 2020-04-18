package com.fabirt.weatherforecast.presentation.weather

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.fabirt.weatherforecast.data.database.getDatabase
import com.fabirt.weatherforecast.data.models.WeatherData
import com.fabirt.weatherforecast.data.repository.WeatherRepository
import com.fabirt.weatherforecast.data.repository.WeatherRepositoryImpl
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.launch
import org.threeten.bp.LocalDate
import org.threeten.bp.format.DateTimeFormatter

class WeatherViewModel(private val repository: WeatherRepository) : ViewModel() {
    private val viewModelJob = Job()
    private val viewModelScope = CoroutineScope(viewModelJob + Dispatchers.Main)

    val currentWeather = repository.currentWeather
    val currentLocation = repository.currentLocation

    init {
        getCurrenWeather()
    }

    fun getCurrenWeather() {
        viewModelScope.launch {
            repository.fetchCurrentWeather()
        }
    }

    override fun onCleared() {
        super.onCleared()
        viewModelJob.cancel()
    }
}
