package com.nawrot.mateusz.compass.domain.directions

import com.nawrot.mateusz.compass.domain.base.SchedulersProvider
import com.nawrot.mateusz.compass.domain.base.usecase.ObservableUseCase
import io.reactivex.Observable
import javax.inject.Inject


class GetDirectionsUseCase @Inject constructor(schedulersProvider: SchedulersProvider,
                                               private val directionsRepository: DirectionsRepository) : ObservableUseCase<DirectionsParam, Float>(schedulersProvider) {

    override fun createUseCaseObservable(directionsParam: DirectionsParam): Observable<Float> {
        return directionsRepository.getDirectionTo(directionsParam.latitude, directionsParam.longitude, directionsParam.locationEnabled)
    }
}