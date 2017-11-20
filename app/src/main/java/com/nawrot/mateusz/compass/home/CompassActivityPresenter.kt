package com.nawrot.mateusz.compass.home

import com.nawrot.mateusz.compass.base.BasePresenter
import com.nawrot.mateusz.compass.domain.directions.GetDirectionsUseCase
import javax.inject.Inject


class CompassActivityPresenter @Inject constructor(private val directionsUseCase: GetDirectionsUseCase) : BasePresenter<CompassView>() {

    fun getDirection() {

    }

}