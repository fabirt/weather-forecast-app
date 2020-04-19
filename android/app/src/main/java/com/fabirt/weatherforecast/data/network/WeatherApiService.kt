package com.fabirt.weatherforecast.data.network

import android.util.Log
import com.fabirt.weatherforecast.BuildConfig
import com.fabirt.weatherforecast.data.models.WeatherResponse
import com.jakewharton.retrofit2.adapter.kotlin.coroutines.CoroutineCallAdapterFactory
import kotlinx.coroutines.Deferred
import okhttp3.Interceptor
import okhttp3.OkHttpClient
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.http.GET
import retrofit2.http.Query

private const val BASE_URL = BuildConfig.BASE_URL
private const val API_KEY = BuildConfig.ACCESS_KEY

interface WeatherApiService {
    /**
     * Request the current weather information for the given [location].
     * The response is retrieved in the given [languageCode].
     */
    @GET("current")
    fun getCurrentWeatherAsync(
        @Query("query") location: String,
        @Query("language") languageCode: String
    ): Deferred<WeatherResponse>

    companion object {
        operator fun invoke(): WeatherApiService {
            val requestInterceptor = Interceptor { chain ->
                val url = chain.request()
                    .url()
                    .newBuilder()
                    .addQueryParameter("access_key", API_KEY)
                    .build()

                val request = chain.request()
                    .newBuilder()
                    .url(url)
                    .build()

                return@Interceptor chain.proceed(request)
            }

            val httpClient = OkHttpClient.Builder()
                .addInterceptor(requestInterceptor)
                .build()

            return Retrofit.Builder()
                .client(httpClient)
                .baseUrl(BASE_URL)
                .addCallAdapterFactory(CoroutineCallAdapterFactory())
                .addConverterFactory(GsonConverterFactory.create())
                .build()
                .create(WeatherApiService::class.java)

        }
    }
}