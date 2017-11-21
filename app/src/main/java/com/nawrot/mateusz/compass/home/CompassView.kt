package com.nawrot.mateusz.compass.home

import com.nawrot.mateusz.compass.base.BaseView


interface CompassView : BaseView {

    fun rotateCompass(angle: Float)

    fun getDestinationLatitude(): Double?

    fun getDestinationLongitude(): Double?

    fun showLocationPermissionRationale()

}