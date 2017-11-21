package com.nawrot.mateusz.compass.home

import android.os.Bundle
import android.support.design.widget.Snackbar
import com.nawrot.mateusz.compass.R
import com.nawrot.mateusz.compass.base.BaseActivity
import dagger.android.AndroidInjection
import kotlinx.android.synthetic.main.activity_compass.*
import javax.inject.Inject

class CompassActivity : BaseActivity(), CompassView {

    @Inject
    lateinit var presenter: CompassActivityPresenter

    override fun onCreate(savedInstanceState: Bundle?) {
        AndroidInjection.inject(this)
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_compass)
        setSupportActionBar(toolbar)

        fab.setOnClickListener { view ->
            Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
                    .setAction("Action", null).show()
        }
    }

    override fun onStart() {
        super.onStart()
        presenter.attachView(this)
    }

    override fun onStop() {
        super.onStop()
        presenter.detachView()
    }

    override fun rotateCompass(angle: Double) {
    }

    override fun getDestinationLatitude(): Double {
        return 0.0
    }

    override fun getDestinationLongitude(): Double {
        return 0.0
    }

    override fun showError(errorMessage: String) {

    }

}
