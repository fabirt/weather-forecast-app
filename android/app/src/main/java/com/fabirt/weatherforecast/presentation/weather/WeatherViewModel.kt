package com.fabirt.weatherforecast.presentation.weather

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.fabirt.weatherforecast.data.models.WeatherData
import com.fabirt.weatherforecast.data.repository.WeatherRepository
import com.fabirt.weatherforecast.data.repository.WeatherRepositoryImpl
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.launch
import org.threeten.bp.LocalDate
import org.threeten.bp.format.DateTimeFormatter

class WeatherViewModel : ViewModel() {
    private val repository: WeatherRepository = WeatherRepositoryImpl()
    private val viewModelJob = Job()
    private val viewModelScope = CoroutineScope(viewModelJob + Dispatchers.Main)

    private val _currentWeather = MutableLiveData<WeatherData>()
    val currentWeather: LiveData<WeatherData>
        get() = _currentWeather

    init {
        getCurrenWeather()
    }

    private fun getCurrenWeather() {
        viewModelScope.launch {
            val weather = repository.getCurrentWeather()
            val formatter = DateTimeFormatter.ofPattern("EEE dd MMMM yyyy")
            val date = LocalDate.ofEpochDay(weather.location.localtimeEpoch / 86400000L)

            _currentWeather.value = weather.copy(location = weather.location.copy(localtime = date.format(formatter)))
        }
    }

    override fun onCleared() {
        super.onCleared()
        viewModelJob.cancel()
    }
}
