package com.fabirt.weatherforecast.core.work

import android.content.Context
import android.widget.Toast
import androidx.core.app.NotificationCompat
import androidx.core.app.NotificationManagerCompat
import androidx.work.CoroutineWorker
import androidx.work.WorkerParameters
import com.fabirt.weatherforecast.R
import com.fabirt.weatherforecast.data.database.getDatabase
import com.fabirt.weatherforecast.data.network.WeatherApiService
import com.fabirt.weatherforecast.data.providers.LocationProviderImpl
import com.fabirt.weatherforecast.data.providers.UpdateTimeProviderImpl
import com.fabirt.weatherforecast.data.repository.WeatherRepositoryImpl
import com.fabirt.weatherforecast.domain.repository.WeatherRepository
import com.google.android.gms.location.FusedLocationProviderClient

class DailyWorker(appContext: Context, params: WorkerParameters) :
    CoroutineWorker(appContext, params) {

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
        val repository: WeatherRepository = WeatherRepositoryImpl(
            weatherService = WeatherApiService(),
            updateTimeProvider = UpdateTimeProviderImpl(applicationContext),
            weatherDao = getDatabase(applicationContext).weatherDao,
            locationProvider = LocationProviderImpl(
                applicationContext,
                FusedLocationProviderClient(applicationContext)
            )
        )
        
        val builder = NotificationCompat.Builder(applicationContext, "CHANNEL_ID")
            .setSmallIcon(R.drawable.ic_weather)
            .setContentTitle("My Weather Forecast")
            .setContentText("Remember to wash your hands, drink water and check today's weather forecast")
            .setPriority(NotificationCompat.PRIORITY_HIGH)

        NotificationManagerCompat.from(applicationContext).notify(2000, builder.build())

        return Result.success()
    }

    companion object {
        const val WORK_NAME = "DailyWeatherWorker"
    }

}