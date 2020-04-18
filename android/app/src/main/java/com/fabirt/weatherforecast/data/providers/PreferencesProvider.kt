package com.fabirt.weatherforecast.data.providers

import android.content.Context
import android.content.SharedPreferences
import androidx.preference.PreferenceManager

abstract class PreferencesProvider(private val context: Context) {
    protected val preferences: SharedPreferences
        get() = PreferenceManager.getDefaultSharedPreferences(context.applicationContext)
}