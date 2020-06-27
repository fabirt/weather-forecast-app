package com.fabirt.weatherforecast.presentation.settings

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import com.fabirt.weatherforecast.core.utils.SettingsManager
import com.fabirt.weatherforecast.databinding.FragmentSettingsBinding

class SettingsFragment : Fragment() {

    private lateinit var binding: FragmentSettingsBinding
    private lateinit var viewModel: SettingsViewModel

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentSettingsBinding.inflate(inflater, container, false)

        binding.locationPermissionSwitch.setOnClickListener {
            locationPermissionsCheckedChangeListener()
        }

        binding.locationEnabledSwitch.setOnClickListener {
            locationEnabledCheckedChangeListener()
        }
        return binding.root
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        val viewModelFactory = SettingsViewModelFactory(requireContext())
        viewModel = ViewModelProvider(this, viewModelFactory).get(SettingsViewModel::class.java)
        observeViewModel()
    }

    override fun onResume() {
        super.onResume()
        viewModel.checkSettings()
    }

    private fun locationPermissionsCheckedChangeListener() {
        SettingsManager.openAppSettings(requireContext())
    }

    private fun locationEnabledCheckedChangeListener() {
        SettingsManager.openLocationSettings(requireContext())
    }

    private fun observeViewModel() {
        viewModel.locationEnabled.observe(viewLifecycleOwner, Observer { isEnabled ->
            binding.locationEnabledSwitch.isChecked = isEnabled
        })

        viewModel.locationPermissionsGranted.observe(viewLifecycleOwner, Observer { isEnabled ->
            binding.locationPermissionSwitch.isChecked = isEnabled
        })
    }
}
