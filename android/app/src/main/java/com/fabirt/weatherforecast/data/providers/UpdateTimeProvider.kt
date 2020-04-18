package com.fabirt.weatherforecast.data.providers

import android.content.Context
import android.util.Log

private const val LAST_UPDATED_TIME_KEY = "LAST_UPDATED_TIME_KEY"

class UpdateTimeProvider(context: Context) : PreferencesProvider(context) {
    /**
    * Updates the latest time, in milliseconds, where the weather was updated.
    */
    fun setLatestUpdateTime(time: Long) {
        val editor = preferences.edit()
        editor.putLong(LAST_UPDATED_TIME_KEY, time)
        editor.apply()
    }


    private fun getLatestUpdateTime(): Long {
        return preferences.getLong(LAST_UPDATED_TIME_KEY, 0)
    }

    /**
     * Returns true if the latest stored time was at least 6 hours ago.
     */
    fun isCurrentWeatherUpdateNeeded(): Boolean {
        Log.i("UpdateTimeProvider", "isCurrentWeatherUpdateNeeded called")
        val latestTime = getLatestUpdateTime()
        Log.i("UpdateTimeProvider", "latestTime $latestTime")
        val currentTime = System.currentTimeMillis()
        Log.i("UpdateTimeProvider", "currentTime $currentTime")
        val requiredDiff = 2.16e7
        Log.i("UpdateTimeProvider", "diff ${currentTime - latestTime}")
        Log.i("UpdateTimeProvider", "return exp ${currentTime - latestTime > requiredDiff}")
        return currentTime - latestTime > requiredDiff
    }
}