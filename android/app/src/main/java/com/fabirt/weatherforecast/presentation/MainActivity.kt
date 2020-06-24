package com.fabirt.weatherforecast.presentation

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.MenuItem
import androidx.appcompat.widget.TooltipCompat
import androidx.core.view.forEach
import androidx.navigation.findNavController
import androidx.navigation.ui.setupWithNavController
import com.fabirt.weatherforecast.R
import com.fabirt.weatherforecast.presentation.futureweather.FutureWeatherFragmentDirections
import com.fabirt.weatherforecast.presentation.settings.SettingsFragmentDirections
import com.fabirt.weatherforecast.presentation.weather.WeatherFragmentDirections
import com.google.android.material.bottomnavigation.BottomNavigationView
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.android.synthetic.main.activity_main.*

@AndroidEntryPoint
class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        initializeUI()
    }

    private fun initializeUI() {
        val bottomNav = findViewById<BottomNavigationView>(R.id.bottom_nav)
        val navController = findNavController(R.id.nav_host_fragment)
        bottomNav.setupWithNavController(navController)
        bottomNav.setOnNavigationItemSelectedListener {
            selectedNavigationItemListener(it, bottomNav.selectedItemId)
            true
        }
    }

    private fun selectedNavigationItemListener(item: MenuItem, selectedId: Int) {
        val navController = findNavController(R.id.nav_host_fragment)
        when(item.itemId) {
            R.id.weatherFragment -> {
                if (selectedId == R.id.futureWeatherFragment) {
                    navController.navigate(FutureWeatherFragmentDirections.actionFutureWeatherFragmentToWeatherFragment())
                } else if (selectedId == R.id.settingsFragment) {
                    navController.navigate(SettingsFragmentDirections.actionSettingsFragmentToWeatherFragment())
                }
            }
            R.id.futureWeatherFragment -> {
                if (selectedId == R.id.weatherFragment) {
                    navController.navigate(WeatherFragmentDirections.actionWeatherFragmentToFutureWeatherFragment())
                } else if (selectedId == R.id.settingsFragment) {
                    navController.navigate(SettingsFragmentDirections.actionSettingsFragmentToFutureWeatherFragment())
                }
            }
            R.id.settingsFragment -> {
                if (selectedId == R.id.weatherFragment) {
                    navController.navigate(WeatherFragmentDirections.actionWeatherFragmentToSettingsFragment2())
                } else if (selectedId == R.id.futureWeatherFragment) {
                    navController.navigate(FutureWeatherFragmentDirections.actionFutureWeatherFragmentToSettingsFragment())
                }
            }
        }
    }
}
