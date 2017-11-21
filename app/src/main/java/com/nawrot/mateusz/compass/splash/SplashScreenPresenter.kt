package com.nawrot.mateusz.compass.splash

import com.nawrot.mateusz.compass.base.BasePresenter
import com.nawrot.mateusz.compass.domain.initialize.InitializeAppUseCase
import javax.inject.Inject


class SplashScreenPresenter @Inject constructor(private val initializeAppUseCase: InitializeAppUseCase) : BasePresenter<SplashScreenView>() {

    fun initializeApp() {
        compositeDisposable.add(initializeAppUseCase.execute(Unit).subscribe { view?.navigateToHomeScreen() })
    }

}