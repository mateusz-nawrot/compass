package com.nawrot.mateusz.compass.data.directions

import android.annotation.SuppressLint
import android.hardware.*
import android.location.Location
import android.location.LocationManager
import com.nawrot.mateusz.compass.domain.base.ifNotNull
import com.nawrot.mateusz.compass.domain.directions.AccelerometerEvent
import com.nawrot.mateusz.compass.domain.directions.Direction
import com.nawrot.mateusz.compass.domain.directions.DirectionsRepository
import com.nawrot.mateusz.compass.domain.directions.MagnetometerEvent
import io.reactivex.Observable
import io.reactivex.ObservableOnSubscribe
import io.reactivex.disposables.Disposables
import io.reactivex.functions.BiFunction
import javax.inject.Inject


class MNDirectionsRepository @Inject constructor(private val sensorManager: SensorManager,
                                                 private val locationManager: LocationManager) : DirectionsRepository {

    private val rotationMatrix = FloatArray(9)
    private val orientationAngles = FloatArray(3)

    private val sensorAccelerometer = sensorManager.getDefaultSensor(Sensor.TYPE_ACCELEROMETER)
    private val sensorMagnetic = sensorManager.getDefaultSensor(Sensor.TYPE_MAGNETIC_FIELD)


    @SuppressLint("MissingPermission")
    override fun getDirectionTo(latitude: Double?, longitude: Double?, locationEnabled: Boolean): Observable<Direction> {

        val accelerometerObservable = Observable.create(ObservableOnSubscribe<AccelerometerEvent> { emitter ->
            val accelerometerListener = object : SensorEventListener {
                override fun onSensorChanged(event: SensorEvent?) {
                    event?.values?.let {
                        emitter.onNext(AccelerometerEvent(it.copyOf()))
                    }
                }

                override fun onAccuracyChanged(p0: Sensor?, p1: Int) {}
            }

            emitter.setDisposable(Disposables.fromAction { sensorManager.unregisterListener(accelerometerListener) })

            sensorManager.registerListener(accelerometerListener, sensorAccelerometer, SensorManager.SENSOR_DELAY_UI)
        })

        val magnetometerObservable = Observable.create(ObservableOnSubscribe<MagnetometerEvent> { emitter ->
            val magnetometerListener: SensorEventListener = object : SensorEventListener {
                override fun onSensorChanged(event: SensorEvent?) {
                    event?.values?.let {
                        emitter.onNext(MagnetometerEvent(it.copyOf()))
                    }
                }

                override fun onAccuracyChanged(p0: Sensor?, p1: Int) {}
            }

            emitter.setDisposable(Disposables.fromAction { sensorManager.unregisterListener(magnetometerListener) })

            sensorManager.registerListener(magnetometerListener, sensorMagnetic, SensorManager.SENSOR_DELAY_UI)
        })

        return Observable.zip(accelerometerObservable, magnetometerObservable, BiFunction<AccelerometerEvent, MagnetometerEvent, Direction> { accelerometerEvent, magnetometerEvent ->
            SensorManager.getRotationMatrix(rotationMatrix, null, accelerometerEvent.readings, magnetometerEvent.readings)
            SensorManager.getOrientation(rotationMatrix, orientationAngles)

            var azimuth = Math.toDegrees(orientationAngles[0].toDouble()).toFloat()

            if (locationEnabled) {
                val currentLocation: Location? = locationManager.getLastKnownLocation(LocationManager.NETWORK_PROVIDER)
                currentLocation?.let {
                    val geoField = GeomagneticField(it.latitude.toFloat(), it.longitude.toFloat(), it.altitude.toFloat(), System.currentTimeMillis())
                    azimuth -= geoField.declination

                    ifNotNull(latitude, longitude, { lat, lng ->
                        val destinationLocation = Location("")
                        destinationLocation.latitude = lat
                        destinationLocation.longitude = lng

                        var bearingTo = currentLocation.bearingTo(destinationLocation)

                        if (bearingTo < 0) {
                            bearingTo += 360
                        }

                        azimuth += bearingTo

                        if (azimuth < 0) azimuth += 360
                    })
                }
            }

            Direction(azimuth)
        })
    }
}