package com.nawrot.mateusz.compass.domain.directions

import java.util.*


data class AccelerometerEvent(val readings: FloatArray) {

    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (javaClass != other?.javaClass) return false

        other as MagnetometerEvent

        if (!Arrays.equals(readings, other.readings)) return false

        return true
    }

    override fun hashCode(): Int {
        return Arrays.hashCode(readings)
    }
}