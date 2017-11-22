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
import org.mockito.Mockito.never
import org.mockito.Mockito.verify
import org.mockito.MockitoAnnotations


class CompassPresenterTest {

    private val validLatLng = 12.0

    private val emptyLatLng: Double? = null

    private val validDirectionsParam = DirectionsParam(validLatLng, validLatLng, false)

    private val emptyDirectionsParam = DirectionsParam(emptyLatLng, emptyLatLng, false)

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
    fun `Request Location permission when it is not granted while calling getDirection`() {
        Mockito.`when`(view.getDestinationLatitude()).thenReturn(validLatLng)
        Mockito.`when`(view.getDestinationLongitude()).thenReturn(validLatLng)
        Mockito.`when`(view.locationPermissionGranted()).thenReturn(false)
        Mockito.`when`(getDirectionUseCase.execute(validDirectionsParam)).thenReturn(Observable.just(testDirection))

        presenter.getDirection()

        verify(view).requestLocationPermission()
    }

    @Test
    fun `Do not request Location permission when askForPermission is set to false while calling getDirection`() {
        Mockito.`when`(view.getDestinationLatitude()).thenReturn(validLatLng)
        Mockito.`when`(view.getDestinationLongitude()).thenReturn(validLatLng)
        Mockito.`when`(view.locationPermissionGranted()).thenReturn(false)
        Mockito.`when`(getDirectionUseCase.execute(validDirectionsParam)).thenReturn(Observable.just(testDirection))

        presenter.getDirection(false)

        verify(view, never()).requestLocationPermission()
    }

    @Test
    fun `Show permission rationale when user denied location permission`() {
        presenter.locationPermissionDenied()

        verify(view).showLocationPermissionRationale()
    }

    @Test
    fun `Rotate the arrow after getDirection() succeed`() {
        Mockito.`when`(view.getDestinationLatitude()).thenReturn(validLatLng)
        Mockito.`when`(view.getDestinationLongitude()).thenReturn(validLatLng)
        Mockito.`when`(getDirectionUseCase.execute(validDirectionsParam)).thenReturn(Observable.just(testDirection))

        presenter.getDirection()

        verify(view).rotateCompass(testDirection.angle)
    }

    @Test
    fun `Show destination dialog when user clicked on destination button`() {
        presenter.destinationButtonClicked()

        verify(view).showDestinationDialog()
    }

    @Test
    fun `Should show destination indicator when entered coordinates are not empty`() {
        Mockito.`when`(view.getDestinationLatitude()).thenReturn(validLatLng)
        Mockito.`when`(view.getDestinationLongitude()).thenReturn(validLatLng)
        Mockito.`when`(getDirectionUseCase.execute(validDirectionsParam)).thenReturn(Observable.just(testDirection))

        presenter.destinationCoordinatesEntered(validLatLng, validLatLng)

        verify(view).showDestinationIndicator(validLatLng, validLatLng)
    }

    @Test
    fun `Should hide destination indicator when entered coordinates are empty`() {
        Mockito.`when`(view.getDestinationLatitude()).thenReturn(emptyLatLng)
        Mockito.`when`(view.getDestinationLongitude()).thenReturn(emptyLatLng)
        Mockito.`when`(getDirectionUseCase.execute(emptyDirectionsParam)).thenReturn(Observable.just(testDirection))

        presenter.destinationCoordinatesEntered(emptyLatLng, emptyLatLng)

        verify(view).hideDestinationIndicator()
    }

}