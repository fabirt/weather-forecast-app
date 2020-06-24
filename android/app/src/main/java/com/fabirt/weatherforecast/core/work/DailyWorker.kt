package com.fabirt.weatherforecast.core.work

import android.app.Notification
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import android.graphics.BitmapFactory
import androidx.core.app.NotificationCompat
import androidx.core.app.NotificationManagerCompat
import androidx.hilt.Assisted
import androidx.hilt.work.WorkerInject
import androidx.work.CoroutineWorker
import androidx.work.WorkerParameters
import com.fabirt.weatherforecast.R
import com.fabirt.weatherforecast.core.constants.NOTIFICATION_CHANNEL_ID
import com.fabirt.weatherforecast.domain.repository.WeatherRepository
import com.fabirt.weatherforecast.presentation.MainActivity

class DailyWorker @WorkerInject constructor(
    @Assisted appContext: Context,
    @Assisted params: WorkerParameters,
    private val weatherRepository: WeatherRepository
) :
    CoroutineWorker(appContext, params) {

    companion object {
        const val WORK_NAME = "DailyWeatherWorker"
    }

    override suspend fun doWork(): Result {
        val (weather, location) = weatherRepository.fetchCurrentWeatherMandatory()
        var content: String =
            "Have a nice day! Remember to wash your hands, stay safe and check today's weather 🌥"
        if (weather != null && location != null) {
            val temperature = weather.temperature
            content = if (temperature >= 30) {
                "It is a little hot today. We have $temperature ºC. Take a shower and drink water"
            } else {
                "Everyday is a nice day! Today's temperature is $temperature ºC. Remember to wash your hands"
            }
        }
        val notification = buildNotification(content)
        NotificationManagerCompat.from(applicationContext).notify(2000, notification)
        return Result.success()
    }

    private fun buildNotification(content: String): Notification {
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
            .setContentText(content)
            .setStyle(NotificationCompat.BigTextStyle())
            .setPriority(NotificationCompat.PRIORITY_HIGH)
            .setAutoCancel(true)
            .setContentIntent(pendingIntent)

        return notification.build()
    }
}