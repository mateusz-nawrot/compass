package com.nawrot.mateusz.compass.home

import com.nawrot.mateusz.compass.base.BasePresenter
import com.nawrot.mateusz.compass.domain.directions.DirectionsParam
import com.nawrot.mateusz.compass.domain.directions.GetDirectionsUseCase
import javax.inject.Inject


class CompassActivityPresenter @Inject constructor(private val directionsUseCase: GetDirectionsUseCase) : BasePresenter<CompassView>() {

    fun getDirection(askForPermission: Boolean = true) {
        val locationPermission: Boolean = view?.locationPermissionGranted() ?: false

        if (!locationPermission && askForPermission) {
            view?.requestLocationPermission()
        }

        compositeDisposable.add(directionsUseCase.execute(getDirectionsParam(locationPermission)).subscribe({ direction ->
            view?.rotateCompass(direction.angle)
        }))
    }

    fun locationPermissionDenied() {
        view?.showLocationPermissionRationale()
    }

    fun onLocationButtonClick() {
        view?.showDestinationDialog()
    }

    private fun getDirectionsParam(locationEnabled: Boolean): DirectionsParam {
        return DirectionsParam(view?.getDestinationLatitude(), view?.getDestinationLongitude(), locationEnabled)
    }

}