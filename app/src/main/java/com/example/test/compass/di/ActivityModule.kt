package com.example.test.compass.di

import androidx.lifecycle.ViewModel
import com.example.test.compass.MainActivity
import com.example.test.compass.ui.CompassViewModel
import dagger.Binds
import dagger.Module
import dagger.android.ContributesAndroidInjector
import dagger.multibindings.IntoMap

@Module
interface ActivityModule {

    @ActivityScope
    @ContributesAndroidInjector(modules = [CompassViewModelModule::class])
    fun mainActivity(): MainActivity
}

@Module
interface CompassViewModelModule {
    @Binds
    @IntoMap
    @ViewModelKey(CompassViewModel::class)
    fun compassViewModel(viewModel: CompassViewModel): ViewModel
}