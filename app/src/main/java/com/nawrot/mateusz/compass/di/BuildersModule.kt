package com.nawrot.mateusz.compass.di

import com.nawrot.mateusz.compass.home.CompassActivity
import com.nawrot.mateusz.compass.home.CompassActivityModule
import dagger.Module
import dagger.android.ContributesAndroidInjector


@Module
abstract class BuildersModule {

    @ActivityScope
    @ContributesAndroidInjector(modules = arrayOf(CompassActivityModule::class))
    abstract fun bindHomeActivity(): CompassActivity

}