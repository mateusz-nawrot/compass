package com.nawrot.mateusz.compass.di

import android.content.Context
import android.hardware.SensorManager
import android.location.LocationManager
import com.nawrot.mateusz.compass.App
import com.nawrot.mateusz.compass.data.base.AndroidSchedulersProvider
import com.nawrot.mateusz.compass.data.directions.MNDirectionsRepository
import com.nawrot.mateusz.compass.data.initialize.MNInitializeRepository
import com.nawrot.mateusz.compass.domain.base.SchedulersProvider
import com.nawrot.mateusz.compass.domain.directions.DirectionsRepository
import com.nawrot.mateusz.compass.domain.initialize.InitializeRepository
import dagger.Binds
import dagger.Module
import dagger.Provides


@Module
abstract class AppModule {

    @Binds
    abstract fun bindContext(app: App): Context

    @Binds
    abstract fun bindSchedulersProvider(androidSchedulersProvider: AndroidSchedulersProvider): SchedulersProvider

    @Binds
    abstract fun bindDirectionsRepository(recipeRepository: MNDirectionsRepository): DirectionsRepository

    @Binds
    abstract fun bindInitializeAppRepository(initializeAppRepository: MNInitializeRepository): InitializeRepository

    @Module
    companion object {

        @JvmStatic
        @Provides
        fun provideSensorManager(context: Context): SensorManager {
            return context.getSystemService(Context.SENSOR_SERVICE) as SensorManager
        }

        @JvmStatic
        @Provides
        fun provideLocationManager(context: Context): LocationManager {
            return context.getSystemService(Context.LOCATION_SERVICE) as LocationManager
        }
    }

}