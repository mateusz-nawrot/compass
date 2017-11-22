package com.nawrot.mateusz.compass.home

import android.Manifest
import android.content.pm.PackageManager
import android.os.Bundle
import android.view.View
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

        RxView.clicks(locationFab).subscribe { presenter.destinationButtonClicked() }
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
        formatAngleText(angle.toInt())
        currentAngle = -angle
        compassArrow.rotateImage(currentAngle)
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
        DestinationDialog.getInstance(destinationLatitude, destinationLongitude).show(supportFragmentManager, DestinationDialog.TAG)
    }

    override fun destinationCoordinatesEntered(latitude: Double?, longitude: Double?) {
        presenter.destinationCoordinatesEntered(latitude, longitude)
    }

    override fun hideDestinationIndicator() {
        destinationIndicator.visibility = View.GONE
        locationIcon.setDrawable(R.drawable.ic_location_off)
        locationFab.setDrawable(R.drawable.ic_add_location)
    }

    override fun showDestinationIndicator(latitude: Double, longitude: Double) {
        locationIcon.setDrawable(R.drawable.ic_location_on)
        locationFab.setDrawable(R.drawable.ic_edit_location)
        destinationIndicator.visibility = View.VISIBLE
        destinationIndicator.text = getString(R.string.lat_lng_placeholder, latitude, longitude)
    }

    override fun getDestinationLatitude(): Double? {
        return destinationLatitude
    }

    override fun getDestinationLongitude(): Double? {
        return destinationLongitude
    }

    private fun formatAngleText(angle: Int) {
        var formattedAngle = angle

        formattedAngle = if (formattedAngle < 0) {
            Math.abs(formattedAngle)
        } else {
            360 - formattedAngle
        }

        angleIndicator.text = formattedAngle.toString()
    }

}
