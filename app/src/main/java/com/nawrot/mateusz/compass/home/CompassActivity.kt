package com.nawrot.mateusz.compass.home

import android.Manifest
import android.content.pm.PackageManager
import android.location.Location
import android.os.Bundle
import com.nawrot.mateusz.compass.R
import com.nawrot.mateusz.compass.base.*
import dagger.android.AndroidInjection
import kotlinx.android.synthetic.main.activity_compass.*
import javax.inject.Inject

class CompassActivity : BaseActivity(), CompassView {

    private val LOCATION_PERMISSION_CODE: Int = 1

    private var currentAngle: Float = 0f

    private var destinationLocation: Location? = null

    @Inject
    lateinit var presenter: CompassActivityPresenter

    override fun onCreate(savedInstanceState: Bundle?) {
        AndroidInjection.inject(this)
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_compass)
        setSupportActionBar(toolbar)

        locationFab.setOnClickListener {
            presenter.onLocationButtonClick()
        }
    }

    override fun onStart() {
        super.onStart()
        presenter.attachView(this)
        requestForLocationPermission()
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

    override fun getDestinationLatitude(): Double? {
        return latitudeInput.text.toString().toOptionalDouble()
    }

    override fun getDestinationLongitude(): Double? {
        return longitudeInput.text.toString().toOptionalDouble()
    }

    override fun showLocationPermissionRationale() {
        compassActivityCoordinator.showSnackbar(getString(R.string.location_rationale_message), action = getString(R.string.grant_location_permission), actionListener = {
            requestForLocationPermission()
        })
    }

    override fun onRequestPermissionsResult(requestCode: Int, permissions: Array<out String>, grantResults: IntArray) {
        when (requestCode) {
            LOCATION_PERMISSION_CODE -> {
                if (grantResults.isNotEmpty() && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    //location permission granted
                    presenter.getDirectionWithLocation()
                } else {
                    //permission denied
                    presenter.getDirection()
                }
                return
            }
        }
    }

    private fun requestForLocationPermission() {
        if (!isPermissionGranted(Manifest.permission.ACCESS_FINE_LOCATION)) {
            requestPermissionCompat(arrayOf(Manifest.permission.ACCESS_FINE_LOCATION), LOCATION_PERMISSION_CODE)
        } else {
            presenter.getDirectionWithLocation()
        }
    }

}
