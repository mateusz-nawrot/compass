@file:Suppress("IllegalIdentifier")

package com.nawrot.mateusz.compass

import com.nawrot.mateusz.compass.domain.directions.Direction
import com.nawrot.mateusz.compass.domain.directions.DirectionsParam
import com.nawrot.mateusz.compass.domain.directions.GetDirectionsUseCase
import com.nawrot.mateusz.compass.home.CompassPresenter
import com.nawrot.mateusz.compass.home.CompassView
import io.reactivex.Observable
import org.junit.Before
import org.junit.Test
import org.mockito.Mock
import org.mockito.Mockito
import org.mockito.Mockito.verify
import org.mockito.MockitoAnnotations


class CompassPresenterTest {

    private val testLatLng = 12.0

    private val testDirectionsParam = DirectionsParam(testLatLng, testLatLng, false)

    private val testDirection = Direction(12f)

    @Mock
    private lateinit var view: CompassView

    @Mock
    private lateinit var getDirectionUseCase: GetDirectionsUseCase

    private lateinit var presenter: CompassPresenter

    @Before
    fun setup() {
        MockitoAnnotations.initMocks(this)
        presenter = CompassPresenter(getDirectionUseCase)
        presenter.attachView(view)
    }

    @Test
    fun `Request Location Permission when it is not granted while calling getDirection()`() {
        Mockito.`when`(view.getDestinationLatitude()).thenReturn(testLatLng)
        Mockito.`when`(view.getDestinationLongitude()).thenReturn(testLatLng)

        Mockito.`when`(view.locationPermissionGranted()).thenReturn(false)
        Mockito.`when`(getDirectionUseCase.execute(testDirectionsParam)).thenReturn(Observable.just(testDirection))

        presenter.getDirection()

        verify(view).requestLocationPermission()
    }

}