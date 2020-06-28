package com.fabirt.weatherforecast.presentation.futureweather

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.ViewModelProvider
import com.fabirt.weatherforecast.R
import com.fabirt.weatherforecast.core.utils.TransitionsHelper

class FutureWeatherFragment : Fragment() {

    private lateinit var viewModel: FutureWeatherViewModel

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_future_weather, container, false)
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        TransitionsHelper.setupTransitions(this)
        viewModel = ViewModelProvider(this).get(FutureWeatherViewModel::class.java)
        // TODO: Use the ViewModel
    }
}