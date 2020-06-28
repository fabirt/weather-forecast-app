package com.fabirt.weatherforecast.core.utils

import android.content.Context
import android.content.Intent
import android.net.Uri
import android.provider.Settings
import androidx.core.content.ContextCompat.startActivity

object SettingsManager {
    /**
     * Show screen of details about the application.
     * */
    fun openAppSettings(context: Context) {
        val uri: Uri = Uri.fromParts("package", context.packageName, null)
        val intent = Intent(Settings.ACTION_APPLICATION_DETAILS_SETTINGS).applyFlags {
            data = uri
        }
        startActivity(context, intent, null)
    }

    /**
     * Show settings to allow configuration of current location sources.
     * */
    fun openLocationSettings(context: Context) {
        val intent = Intent(Settings.ACTION_LOCATION_SOURCE_SETTINGS).applyFlags()
        startActivity(context, intent, null)
    }

    /**
     * Show settings to allow configuration of Wi-Fi.
     * */
    fun openWifiSettings(context: Context) {
        val intent = Intent(Settings.ACTION_WIFI_SETTINGS).applyFlags()
        startActivity(context, intent, null)
    }

    // Apply flags to the given [Intent].
    private inline fun Intent.applyFlags(block: Intent.() -> Unit = {}): Intent {
        flags = Intent.FLAG_ACTIVITY_NEW_TASK or
                Intent.FLAG_ACTIVITY_NO_HISTORY or
                Intent.FLAG_ACTIVITY_EXCLUDE_FROM_RECENTS

        block()
        return this
    }
}