package com.nawrot.mateusz.compass.home

import com.nawrot.mateusz.compass.base.BaseView


interface CompassView : BaseView {

    fun rotateCompass(angle: Double)

    fun getDestinationLatitude(): Double

    fun getDestinationLongitude(): Double

    fun showError(errorMessage: String)

}