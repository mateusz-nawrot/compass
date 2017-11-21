package com.nawrot.mateusz.compass.splash

import android.os.Bundle
import com.nawrot.mateusz.compass.R
import com.nawrot.mateusz.compass.base.BaseActivity
import com.nawrot.mateusz.compass.base.getColorCompat
import com.nawrot.mateusz.compass.base.getDimenValue
import com.nawrot.mateusz.compass.base.setColor
import com.nawrot.mateusz.compass.home.CompassActivity
import dagger.android.AndroidInjection
import kotlinx.android.synthetic.main.activity_splash_screen.*
import javax.inject.Inject


class SplashScreenActivity : BaseActivity(), SplashScreenView {

    @Inject
    lateinit var presenter: SplashScreenPresenter

    override fun onCreate(savedInstanceState: Bundle?) {
        AndroidInjection.inject(this)
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_splash_screen)

        splashScreenProgress.setColor(getColorCompat(R.color.white))
    }

    override fun onStart() {
        super.onStart()
        presenter.attachView(this)
    }

    override fun onStop() {
        super.onStop()
        presenter.detachView()
    }

    override fun onResume() {
        super.onResume()
        animateSplash()
        presenter.initializeApp()
    }

    override fun navigateToHomeScreen() {
        start(CompassActivity::class)
    }

    private fun animateSplash() {
        val animationTime = resources.getInteger(android.R.integer.config_longAnimTime).toLong()
        val titleTranslation = 3 * getDimenValue(R.dimen.activity_horizontal_margin)

        splashScreenTitle.alpha = 0f
        splashScreenTitle.translationY = titleTranslation

        splashScreenProgress.alpha = 0f

        splashScreenTitle.animate()
                .alpha(1f)
                .translationY(0f)
                .setDuration(animationTime)

        splashScreenProgress.animate().alpha(1f)
                .setStartDelay(animationTime)
                .setDuration(animationTime)
    }
}
