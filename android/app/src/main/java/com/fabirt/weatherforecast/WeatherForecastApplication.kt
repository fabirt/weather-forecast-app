package com.fabirt.weatherforecast

import android.app.Application
import android.app.NotificationChannel
import android.app.NotificationManager
import android.os.Build
import androidx.core.app.NotificationManagerCompat
import androidx.hilt.work.HiltWorkerFactory
import androidx.work.*
import com.fabirt.weatherforecast.core.constants.NOTIFICATION_CHANNEL_ID
import com.fabirt.weatherforecast.core.work.DailyWorker
import dagger.hilt.android.HiltAndroidApp
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import java.util.concurrent.TimeUnit
import javax.inject.Inject

@HiltAndroidApp
class WeatherForecastApplication : Application(), Configuration.Provider {
    @Inject
    lateinit var workerFactory: HiltWorkerFactory

    private val applicationScope = CoroutineScope(Dispatchers.Default)

    override fun onCreate() {
        super.onCreate()
        createNotificationChannel()
        delayedInit()
    }

    override fun getWorkManagerConfiguration(): Configuration {
        return Configuration.Builder()
            .setWorkerFactory(workerFactory)
            .build()
    }

    private fun delayedInit() = applicationScope.launch {
        setupRecurringWork()
    }

    private fun setupRecurringWork() {
        val constrains = Constraints.Builder()
            .setRequiredNetworkType(NetworkType.UNMETERED)
            .build()

        val repeatingRequest = PeriodicWorkRequestBuilder<DailyWorker>(1, TimeUnit.DAYS)
            .setConstraints(constrains)
            .setInitialDelay(10, TimeUnit.SECONDS)
            .build()

        WorkManager.getInstance(applicationContext).enqueueUniquePeriodicWork(
            DailyWorker.WORK_NAME,
            ExistingPeriodicWorkPolicy.KEEP,
            repeatingRequest
        )
    }

    private fun createNotificationChannel() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            val name = "Daily weather"
            val descriptionText = "Daily weather notifications"
            val importance = NotificationManager.IMPORTANCE_HIGH
            val channel = NotificationChannel(NOTIFICATION_CHANNEL_ID, name, importance).apply {
                description = descriptionText
            }
            NotificationManagerCompat.from(applicationContext).createNotificationChannel(channel)
        }
    }
}