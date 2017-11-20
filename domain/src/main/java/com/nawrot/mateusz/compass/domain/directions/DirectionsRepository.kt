package com.nawrot.mateusz.compass.domain.directions

import io.reactivex.Observable


interface DirectionsRepository {

    fun getDirectionTo(latitude: Double, longitude: Double): Observable<Direction>

}