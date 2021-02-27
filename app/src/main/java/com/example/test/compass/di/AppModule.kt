package com.example.test.compass.di

import android.app.Application
import android.content.Context
import com.example.test.compass.azimuth.AzimuthResolver
import com.example.test.compass.location.DistanceEvaluator
import com.example.test.compass.location.GpsLocationProvider
import com.example.test.compass.sensor.PhoneRotationSensor
import dagger.Module
import dagger.Provides
import javax.inject.Singleton

@Module
class ApplicationModule {

    @Provides
    fun provideApplicationContext(application: Application): Context =
        application.applicationContext

    @Singleton
    @Provides
    fun provideRotationSensor(context: Context): PhoneRotationSensor =
        PhoneRotationSensor(context)

    @Singleton
    @Provides
    fun provideLocationProvider(context: Context): GpsLocationProvider = GpsLocationProvider(context)

    @Singleton
    @Provides
    fun provideAzimuthResolver(): AzimuthResolver = AzimuthResolver()

    @Singleton
    @Provides
    fun provideDistanceEvaluator(): DistanceEvaluator = DistanceEvaluator()
}