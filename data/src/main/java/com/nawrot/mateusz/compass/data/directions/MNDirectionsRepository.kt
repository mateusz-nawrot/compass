package com.nawrot.mateusz.compass.data.directions

import android.annotation.SuppressLint
import android.hardware.*
import android.location.Location
import android.location.LocationManager
import com.nawrot.mateusz.compass.domain.directions.Direction
import com.nawrot.mateusz.compass.domain.directions.DirectionsRepository
import io.reactivex.Observable
import io.reactivex.subjects.PublishSubject
import javax.inject.Inject


class MNDirectionsRepository @Inject constructor(private val sensorManager: SensorManager,
                                                 private val locationManager: LocationManager) : DirectionsRepository {

    lateinit var subject: PublishSubject<Direction>

    private var accelerometerReading = FloatArray(3)
    private var magnetometerReading = FloatArray(3)

    private val rotationMatrix = FloatArray(9)
    private val orientationAngles = FloatArray(3)

    private val sensorAccelerometer = sensorManager.getDefaultSensor(Sensor.TYPE_ACCELEROMETER)
    private val sensorMagnetic = sensorManager.getDefaultSensor(Sensor.TYPE_MAGNETIC_FIELD)

    private var locationEnabled: Boolean = false

    override fun getDirectionTo(latitude: Double?, longitude: Double?, locationEnabled: Boolean): Observable<Direction> {
        this.locationEnabled = locationEnabled

        val accelerometerListener = object : SensorEventListener {
            override fun onSensorChanged(event: SensorEvent?) {
                event?.values?.let {
                    accelerometerReading = it.copyOf()
                    updateOrientationAngles()
                }
            }

            override fun onAccuracyChanged(p0: Sensor?, p1: Int) {
            }
        }

        val magnetometerListener: SensorEventListener = object : SensorEventListener {
            override fun onSensorChanged(event: SensorEvent?) {
                event?.values?.let {
                    magnetometerReading = it.copyOf()
                    updateOrientationAngles()
                }
            }

            override fun onAccuracyChanged(p0: Sensor?, p1: Int) {
            }
        }

        sensorManager.registerListener(accelerometerListener, sensorAccelerometer, SensorManager.SENSOR_DELAY_UI)
        sensorManager.registerListener(magnetometerListener, sensorMagnetic, SensorManager.SENSOR_DELAY_UI)

        // unregister before creating new subject
        // it is required to avoid leaks after granting location permission and calling getDirectionTo() again
        // otherwise listeners will be alive till onStop() in activity
        if (isSubjectInitialized()) {
            sensorManager.unregisterListener(accelerometerListener)
            sensorManager.unregisterListener(magnetometerListener)
        }

        subject = PublishSubject.create()

        return subject.doOnDispose({
            sensorManager.unregisterListener(accelerometerListener)
            sensorManager.unregisterListener(magnetometerListener)
        })
    }

    @SuppressLint("MissingPermission")
    private fun updateOrientationAngles() {
        SensorManager.getRotationMatrix(rotationMatrix, null, accelerometerReading, magnetometerReading)
        SensorManager.getOrientation(rotationMatrix, orientationAngles)

        var orientationAngle = Math.toDegrees(orientationAngles[0].toDouble()).toFloat()

        if (locationEnabled) {
            val currentLocation: Location? = locationManager.getLastKnownLocation(LocationManager.NETWORK_PROVIDER)
            currentLocation?.let {
                val geoField = GeomagneticField(it.latitude.toFloat(), it.longitude.toFloat(), it.altitude.toFloat(), System.currentTimeMillis())
                orientationAngle -= geoField.declination
            }
        }
        subject.onNext(Direction(orientationAngle))
    }

    private fun isSubjectInitialized(): Boolean {
        return try {
            subject
            true
        } catch (exception: UninitializedPropertyAccessException) {
            false
        }
    }
}