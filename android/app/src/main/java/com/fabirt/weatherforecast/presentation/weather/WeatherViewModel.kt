package com.fabirt.weatherforecast.presentation.weather

import androidx.hilt.lifecycle.ViewModelInject
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.fabirt.weatherforecast.core.error.Failure
import com.fabirt.weatherforecast.core.other.Either
import com.fabirt.weatherforecast.domain.repository.WeatherRepository
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.launch

class WeatherViewModel @ViewModelInject constructor(
    private val repository: WeatherRepository
) : ViewModel() {
    private val viewModelJob = Job()
    private val viewModelScope = CoroutineScope(viewModelJob + Dispatchers.Main)

    val currentWeather = repository.currentWeather
    val currentLocation = repository.currentLocation

    private val _failure = MutableLiveData<Failure?>()
    val failure: LiveData<Failure?>
        get() = _failure

    init {
        // requestCurrenWeather()
    }

    fun requestCurrenWeather() {
        viewModelScope.launch {
            val result = repository.fetchCurrentWeatherRacionale()
            result.fold(
                { failure ->
                    _failure.value = failure
                },
                {}
            )
        }
    }

    fun clearFailure() {
        _failure.value = null
    }

    override fun onCleared() {
        super.onCleared()
        viewModelJob.cancel()
    }
}
