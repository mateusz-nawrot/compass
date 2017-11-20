package com.nawrot.mateusz.compass.data.directions

import android.hardware.Sensor
import android.hardware.SensorEvent
import android.hardware.SensorEventListener
import android.hardware.SensorManager
import android.util.Log
import com.nawrot.mateusz.compass.domain.directions.Direction
import com.nawrot.mateusz.compass.domain.directions.DirectionsRepository
import io.reactivex.Observable
import io.reactivex.subjects.PublishSubject
import javax.inject.Inject


class MNDirectionsRepository @Inject constructor(private val sensorManager: SensorManager) : DirectionsRepository {

    private val subject: PublishSubject<Direction> = PublishSubject.create()

    private var accelerometerReading = FloatArray(3)
    private var magnetometerReading = FloatArray(3)

    private val rotationMatrix = FloatArray(9)
    private val orientationAngles = FloatArray(3)

    private val sensorAccelerometer = sensorManager.getDefaultSensor(Sensor.TYPE_ACCELEROMETER)
    private val sensorMagnetic = sensorManager.getDefaultSensor(Sensor.TYPE_MAGNETIC_FIELD)

    private var accelerometerListener: SensorEventListener = object : SensorEventListener {
        override fun onSensorChanged(event: SensorEvent?) {
            event?.values?.let {
                accelerometerReading = it.copyOf()
                updateOrientationAngles()
            }
        }

        override fun onAccuracyChanged(p0: Sensor?, p1: Int) {
        }

    }

    private var magnetometerListener: SensorEventListener = object : SensorEventListener {
        override fun onSensorChanged(event: SensorEvent?) {
            event?.values?.let {
                magnetometerReading = it.copyOf()
                updateOrientationAngles()
            }
        }

        override fun onAccuracyChanged(p0: Sensor?, p1: Int) {
        }
    }

    override fun getDirectionTo(latitude: Double, longitude: Double): Observable<Direction> {
        sensorManager.registerListener(accelerometerListener, sensorAccelerometer, SensorManager.SENSOR_DELAY_UI)
        sensorManager.registerListener(magnetometerListener, sensorMagnetic, SensorManager.SENSOR_DELAY_UI)
        return subject.doOnTerminate({
            sensorManager.unregisterListener(accelerometerListener)
            sensorManager.unregisterListener(magnetometerListener)
        })
    }

    private fun updateOrientationAngles() {
        SensorManager.getRotationMatrix(rotationMatrix, null, accelerometerReading, magnetometerReading)
        SensorManager.getOrientation(rotationMatrix, orientationAngles)

        val orientationAngle = Math.toDegrees(orientationAngles[0].toDouble())
        Log.d("ORIENTATION_ANGLE", "$orientationAngle")
        subject.onNext(Direction(orientationAngle))
    }
}