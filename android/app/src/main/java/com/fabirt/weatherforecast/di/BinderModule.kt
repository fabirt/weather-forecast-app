package com.fabirt.weatherforecast.di

import com.fabirt.weatherforecast.data.providers.LocationProvider
import com.fabirt.weatherforecast.data.providers.LocationProviderImpl
import com.fabirt.weatherforecast.data.providers.UpdateTimeProvider
import com.fabirt.weatherforecast.data.providers.UpdateTimeProviderImpl
import com.fabirt.weatherforecast.data.repository.WeatherRepositoryImpl
import com.fabirt.weatherforecast.domain.repository.WeatherRepository
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.android.components.ApplicationComponent

@Module
@InstallIn(ApplicationComponent::class)
abstract class BinderModule {
    @Binds
    abstract fun bindUpdateTimeProvider(
        updateTimeProviderImpl: UpdateTimeProviderImpl
    ): UpdateTimeProvider

    @Binds
    abstract fun bindLocationProvider(
        locationProviderImpl: LocationProviderImpl
    ): LocationProvider

    @Binds
    abstract fun bindWeatherRepository(
        weatherRepositoryImpl: WeatherRepositoryImpl
    ): WeatherRepository
}