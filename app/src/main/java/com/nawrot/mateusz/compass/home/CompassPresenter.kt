package com.nawrot.mateusz.compass.home

import com.nawrot.mateusz.compass.base.BasePresenter
import com.nawrot.mateusz.compass.domain.directions.DirectionsParam
import com.nawrot.mateusz.compass.domain.directions.GetDirectionsUseCase
import javax.inject.Inject


class CompassPresenter @Inject constructor(private val directionsUseCase: GetDirectionsUseCase) : BasePresenter<CompassView>() {

    fun getDirection(askForPermission: Boolean = true) {
        val locationPermission: Boolean = view?.locationPermissionGranted() ?: false

        if (!locationPermission && askForPermission) {
            view?.requestLocationPermission()
        }

        clearPreviousDisposableIfNecessary()

        compositeDisposable.add(directionsUseCase.execute(getDirectionsParam(locationPermission)).subscribe({ angle ->
            view?.rotateCompass(angle)
        }))
    }

    fun locationPermissionDenied() {
        view?.showLocationPermissionRationale()
    }

    fun destinationButtonClicked() {
        view?.showDestinationDialog()
    }

    fun destinationCoordinatesEntered(latitude: Double?, longitude: Double?) {
        if (latitude != null && longitude != null) {
            view?.showDestinationIndicator(latitude, longitude)
        } else {
            view?.hideDestinationIndicator()
        }
        getDirection()
    }

    private fun getDirectionsParam(locationEnabled: Boolean): DirectionsParam {
        return DirectionsParam(view?.getDestinationLatitude(), view?.getDestinationLongitude(), locationEnabled)
    }

    private fun clearPreviousDisposableIfNecessary() {
        if (compositeDisposable.size() > 0) compositeDisposable.clear()
    }

}