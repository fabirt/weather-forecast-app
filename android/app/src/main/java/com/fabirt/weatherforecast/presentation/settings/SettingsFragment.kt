package com.fabirt.weatherforecast.presentation.settings

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.provider.Settings
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
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
        val intent = Intent(Settings.ACTION_APPLICATION_DETAILS_SETTINGS)
            .addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
            .addFlags(Intent.FLAG_ACTIVITY_NO_HISTORY)
            .addFlags(Intent.FLAG_ACTIVITY_EXCLUDE_FROM_RECENTS)
        val uri: Uri = Uri.fromParts("package", context?.packageName, null)
        intent.data = uri
        startActivity(intent)
    }

    private fun locationEnabledCheckedChangeListener() {
        val intent = Intent(Settings.ACTION_LOCATION_SOURCE_SETTINGS).apply {
            flags = Intent.FLAG_ACTIVITY_NEW_TASK or
                    Intent.FLAG_ACTIVITY_NO_HISTORY or
                    Intent.FLAG_ACTIVITY_EXCLUDE_FROM_RECENTS
        }
        startActivity(intent)
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
