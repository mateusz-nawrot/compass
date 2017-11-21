package com.nawrot.mateusz.compass.base

import android.support.annotation.CallSuper
import io.reactivex.disposables.CompositeDisposable


open class BasePresenter<V : BaseView> {

    protected var compositeDisposable = CompositeDisposable()

    protected var view: V? = null

    fun attachView(view: V) {
        this.view = view
    }

    @CallSuper
    open fun detachView() {
        compositeDisposable.clear()
    }
}
