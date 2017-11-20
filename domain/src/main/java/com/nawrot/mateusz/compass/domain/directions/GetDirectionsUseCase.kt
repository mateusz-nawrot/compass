package com.nawrot.mateusz.compass.domain.directions

import com.nawrot.mateusz.compass.domain.base.SchedulersProvider
import com.nawrot.mateusz.compass.domain.base.usecase.ObservableUseCase
import io.reactivex.Observable
import javax.inject.Inject


class GetDirectionsUseCase @Inject constructor(schedulersProvider: SchedulersProvider,
                                               private val directionsRepository: DirectionsRepository) : ObservableUseCase<Unit, Direction>(schedulersProvider) {

    override fun createUseCaseObservable(param: Unit): Observable<Direction> {
        return directionsRepository.getDirectionTo(20.0, 30.0)
    }
}