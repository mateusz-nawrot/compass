package com.nawrot.mateusz.compass.di

import android.content.Context
import android.hardware.SensorManager
import com.nawrot.mateusz.compass.App
import com.nawrot.mateusz.compass.data.base.AndroidSchedulersProvider
import com.nawrot.mateusz.compass.data.directions.MNDirectionsRepository
import com.nawrot.mateusz.compass.domain.base.SchedulersProvider
import com.nawrot.mateusz.compass.domain.directions.DirectionsRepository
import dagger.Binds
import dagger.Module
import dagger.Provides


@Module
abstract class AppModule {

    @Binds
    abstract fun bindContext(app: App): Context

    @Binds
    abstract fun schedulersProvider(androidSchedulersProvider: AndroidSchedulersProvider): SchedulersProvider

    @Binds
    abstract fun recipeRepository(recipeRepository: MNDirectionsRepository): DirectionsRepository


    @Module
    companion object {

        @JvmStatic
        @Provides
        fun sensorManager(context: Context): SensorManager {
            return context.getSystemService(Context.SENSOR_SERVICE) as SensorManager
        }
//
//        @JvmStatic
//        @Provides
//        @Named("applicationId")
//        fun applicationId(context: Context) : String {
//            return context.getString(R.string.application_id)
//        }
//
//        @JvmStatic
//        @Provides
//        @Named("apiKey")
//        fun apiKey(context: Context) : String {
//            return context.getString(R.string.rest_api_key)
//        }
//
//        @JvmStatic
//        @Provides
//        @Singleton
//        fun okHttpClient(): OkHttpClient {
//            val builder = OkHttpClient.Builder()
//            builder.retryOnConnectionFailure(true)
//
//            val logInterceptor = HttpLoggingInterceptor()
//            logInterceptor.level = HttpLoggingInterceptor.Level.BODY
//            builder.addInterceptor(logInterceptor)
//            return builder.build()
//        }
//
//        @JvmStatic
//        @Provides
//        @Singleton
//        fun retrofit(okHttpClient: OkHttpClient, @Named("applicationId") applicationId: String, @Named("apiKey") apiKey: String) : Retrofit {
//            return Retrofit.Builder()
//                    .baseUrl("https://api.backendless.com/$applicationId/$apiKey/")
//                    .addConverterFactory(MoshiConverterFactory.create())
//                    .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
//                    .client(okHttpClient)
//                    .build()
//        }
//
    }

}