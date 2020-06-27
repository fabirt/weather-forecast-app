package com.fabirt.weatherforecast.data.providers

import android.content.Context
import android.util.Log
import com.fabirt.weatherforecast.core.constants.LAST_UPDATED_TIME_KEY
import dagger.hilt.android.qualifiers.ApplicationContext
import javax.inject.Inject

class UpdateTimeProviderImpl @Inject constructor(
    @ApplicationContext context: Context
) : PreferencesProvider(context), UpdateTimeProvider {

    private fun requestLastUpdateTime(): Long {
        return preferences.getLong(LAST_UPDATED_TIME_KEY, 0)
    }

    override fun saveLastUpdateTime(time: Long) {
        val editor = preferences.edit()
        editor.putLong(LAST_UPDATED_TIME_KEY, time)
        editor.apply()
    }

    override fun isCurrentWeatherUpdateNeeded(): Boolean {
        val latestTime = requestLastUpdateTime()
        val currentTime = System.currentTimeMillis()
        val requiredDiff = 2.16e7
        return currentTime - latestTime > requiredDiff
    }
}