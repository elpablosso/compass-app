package com.example.test.compass.di

import android.app.Application
import com.example.test.compass.root.App
import dagger.BindsInstance
import dagger.Component
import dagger.android.AndroidInjectionModule
import dagger.android.AndroidInjector
import javax.inject.Singleton

@Singleton
@Component(
    modules = [
        AndroidInjectionModule::class,
        ActivityModule::class,
        ApplicationModule::class,
        ViewModelFactoryModule::class
    ])

interface AppComponent : AndroidInjector<App> {
    @Component.Factory
    interface Factory {
        fun create(@BindsInstance application: Application) : AppComponent
    }
}