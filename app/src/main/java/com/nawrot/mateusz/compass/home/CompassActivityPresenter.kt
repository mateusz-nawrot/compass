package com.nawrot.mateusz.compass.home

import com.nawrot.mateusz.compass.base.BasePresenter
import com.nawrot.mateusz.compass.domain.directions.DirectionsParam
import com.nawrot.mateusz.compass.domain.directions.GetDirectionsUseCase
import javax.inject.Inject


class CompassActivityPresenter @Inject constructor(private val directionsUseCase: GetDirectionsUseCase) : BasePresenter<CompassView>() {

    fun onLocationButtonClick() {

    }

    fun getDirection() {
        view?.showLocationPermissionRationale()

        compositeDisposable.add(directionsUseCase.execute(getDirectionsParam(false)).subscribe({ direction ->
            view?.rotateCompass(direction.angle)
        }))
    }

    fun getDirectionWithLocation() {
        compositeDisposable.add(directionsUseCase.execute(getDirectionsParam(true)).subscribe({ direction ->
            view?.rotateCompass(direction.angle)
        }))
    }

    private fun getDirectionsParam(locationEnabled: Boolean): DirectionsParam {
        return DirectionsParam(view?.getDestinationLatitude(), view?.getDestinationLongitude(), locationEnabled)
    }

}