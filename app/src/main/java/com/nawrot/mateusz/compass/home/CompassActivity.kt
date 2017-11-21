package com.nawrot.mateusz.compass.home

import android.Manifest
import android.content.pm.PackageManager
import android.os.Bundle
import com.jakewharton.rxbinding2.view.RxView
import com.nawrot.mateusz.compass.R
import com.nawrot.mateusz.compass.base.*
import com.nawrot.mateusz.compass.home.destination.DestinationDialog
import com.nawrot.mateusz.compass.home.destination.DestinationDialogActivityInterface
import dagger.android.AndroidInjection
import kotlinx.android.synthetic.main.activity_compass.*
import javax.inject.Inject

class CompassActivity : BaseActivity(), CompassView, DestinationDialogActivityInterface {

    private val LOCATION_PERMISSION_CODE: Int = 1

    private var currentAngle: Float = 0f

    private var destinationLatitude: Double? = null
    private var destinationLongitude: Double? = null

    @Inject
    lateinit var presenter: CompassPresenter

    override fun onCreate(savedInstanceState: Bundle?) {
        AndroidInjection.inject(this)
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_compass)
        setSupportActionBar(toolbar)

        RxView.clicks(locationFab).subscribe { presenter.onLocationButtonClick() }
    }

    override fun onStart() {
        super.onStart()
        presenter.attachView(this)
        presenter.getDirection()
    }

    override fun onStop() {
        super.onStop()
        presenter.detachView()
    }

    override fun rotateCompass(angle: Float) {
        if (angle.difference(-currentAngle) < 1f) {
            return
        }
        angleIndicator.text = angle.toInt().toString()
        currentAngle = -angle
        compass.rotateImage(currentAngle)
    }

    override fun showLocationPermissionRationale() {
        compassActivityCoordinator.showSnackbar(getString(R.string.location_rationale_message), action = getString(R.string.grant_location_permission), actionListener = {
            requestLocationPermission()
        })
    }

    override fun onRequestPermissionsResult(requestCode: Int, permissions: Array<out String>, grantResults: IntArray) {
        when (requestCode) {
            LOCATION_PERMISSION_CODE -> {
                if (grantResults.isEmpty() || grantResults[0] == PackageManager.PERMISSION_DENIED) {
                    //location permission denied
                    presenter.locationPermissionDenied()
                }
                presenter.getDirection(false)
                return
            }
        }
    }

    override fun requestLocationPermission() {
        requestPermissionCompat(arrayOf(Manifest.permission.ACCESS_FINE_LOCATION), LOCATION_PERMISSION_CODE)
    }

    override fun locationPermissionGranted(): Boolean {
        return isPermissionGranted(Manifest.permission.ACCESS_FINE_LOCATION)
    }

    override fun showDestinationDialog() {
        DestinationDialog.getInstance().show(supportFragmentManager, DestinationDialog.TAG)
    }

    override fun destinationCoordinatesEntered(latitude: Double?, longitude: Double?) {
        destinationLatitude = latitude
        destinationLongitude = longitude

        presenter.getDirection()
    }

    override fun getDestinationLatitude(): Double? {
        return destinationLatitude
    }

    override fun getDestinationLongitude(): Double? {
        return destinationLongitude
    }

}
