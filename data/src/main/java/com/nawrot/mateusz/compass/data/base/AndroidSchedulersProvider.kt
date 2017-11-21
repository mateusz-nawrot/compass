package com.nawrot.mateusz.compass.data.base

import com.nawrot.mateusz.compass.domain.base.SchedulersProvider
import io.reactivex.CompletableTransformer
import io.reactivex.ObservableTransformer
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers
import javax.inject.Inject
import javax.inject.Singleton


@Singleton
class AndroidSchedulersProvider @Inject constructor() : SchedulersProvider {

    override fun completableTransformer(): CompletableTransformer {
        return CompletableTransformer { it ->
            it.subscribeOn(Schedulers.io()).observeOn(AndroidSchedulers.mainThread())
        }
    }

    override fun <T> observableTransformer(): ObservableTransformer<T, T> {
        return ObservableTransformer { it ->
            it.subscribeOn(Schedulers.io()).observeOn(AndroidSchedulers.mainThread())
        }
    }

}