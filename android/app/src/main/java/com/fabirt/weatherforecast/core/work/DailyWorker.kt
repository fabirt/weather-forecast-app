package com.fabirt.weatherforecast.core.work

import android.app.Notification
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import android.graphics.BitmapFactory
import androidx.core.app.NotificationCompat
import androidx.core.app.NotificationManagerCompat
import androidx.work.CoroutineWorker
import androidx.work.WorkerParameters
import com.fabirt.weatherforecast.R
import com.fabirt.weatherforecast.core.constants.NOTIFICATION_CHANNEL_ID
import com.fabirt.weatherforecast.data.database.getDatabase
import com.fabirt.weatherforecast.data.network.WeatherApiService
import com.fabirt.weatherforecast.data.providers.LocationProviderImpl
import com.fabirt.weatherforecast.data.providers.UpdateTimeProviderImpl
import com.fabirt.weatherforecast.data.repository.WeatherRepositoryImpl
import com.fabirt.weatherforecast.domain.repository.WeatherRepository
import com.fabirt.weatherforecast.presentation.MainActivity
import com.google.android.gms.location.FusedLocationProviderClient

class DailyWorker(appContext: Context, params: WorkerParameters) :
    CoroutineWorker(appContext, params) {

    companion object {
        const val WORK_NAME = "DailyWeatherWorker"
    }

    /**
     * A suspending method to do your work.  This function runs on the coroutine context specified
     * by [coroutineContext].
     * <p>
     * A CoroutineWorker is given a maximum of ten minutes to finish its execution and return a
     * [ListenableWorker.Result].  After this time has expired, the worker will be signalled to
     * stop.
     *
     * @return The [ListenableWorker.Result] of the result of the background work; note that
     * dependent work will not execute if you return [ListenableWorker.Result.failure]
     */
    override suspend fun doWork(): Result {
        /*
        val repository: WeatherRepository = WeatherRepositoryImpl(
            weatherService = WeatherApiService(),
            updateTimeProvider = UpdateTimeProviderImpl(applicationContext),
            weatherDao = getDatabase(applicationContext).weatherDao,
            locationProvider = LocationProviderImpl(
                applicationContext,
                FusedLocationProviderClient(applicationContext)
            )
        )
        */

        val notification = buildNotification()
        NotificationManagerCompat.from(applicationContext).notify(2000, notification)
        return Result.success()
    }

    private fun buildNotification(): Notification {
        // Create an explicit intent for an Activity in your app
        val intent = Intent(applicationContext, MainActivity::class.java).apply {
            flags = Intent.FLAG_ACTIVITY_NEW_TASK
            action = Intent.ACTION_MAIN
            addCategory(Intent.CATEGORY_LAUNCHER)
        }
        val pendingIntent: PendingIntent =
            PendingIntent.getActivity(applicationContext, 0, intent, 0)

        val notification = NotificationCompat.Builder(applicationContext, NOTIFICATION_CHANNEL_ID)
            .setSmallIcon(R.drawable.ic_notification_large)
            //.setColor(R.color.colorAccent)
            .setLargeIcon(
                BitmapFactory.decodeResource(
                    applicationContext.resources,
                    R.drawable.ic_notification_large
                )
            )
            .setContentTitle("My weather forecast")
            .setContentText("Remember to wash your hands, stay safe and check today's weather 🌥")
            .setStyle(NotificationCompat.BigTextStyle())
            .setPriority(NotificationCompat.PRIORITY_HIGH)
            .setAutoCancel(true)
            .setContentIntent(pendingIntent)
            .build()

        return notification
    }

}