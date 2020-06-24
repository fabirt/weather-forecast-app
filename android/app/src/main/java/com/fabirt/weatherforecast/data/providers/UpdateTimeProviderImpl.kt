package com.fabirt.weatherforecast.data.providers

import android.content.Context
import android.util.Log
import com.fabirt.weatherforecast.core.constants.LAST_UPDATED_TIME_KEY
import dagger.hilt.android.qualifiers.ApplicationContext
import javax.inject.Inject

class UpdateTimeProviderImpl @Inject constructor(
    @ApplicationContext context: Context
) : PreferencesProvider(context), UpdateTimeProvider {

    private fun getLatestUpdateTime(): Long {
        return preferences.getLong(LAST_UPDATED_TIME_KEY, 0)
    }

    /**
     * Updates the latest time, in milliseconds, where the weather was updated.
     * */
    override fun setLatestUpdateTime(time: Long) {
        val editor = preferences.edit()
        editor.putLong(LAST_UPDATED_TIME_KEY, time)
        editor.apply()
    }

    /**
     * Returns true if the latest stored time was at least 6 hours ago.
     */
    override fun isCurrentWeatherUpdateNeeded(): Boolean {
        val latestTime = getLatestUpdateTime()
        val currentTime = System.currentTimeMillis()
        val requiredDiff = 2.16e7
        return currentTime - latestTime > requiredDiff
    }
}