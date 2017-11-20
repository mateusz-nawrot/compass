package com.nawrot.mateusz.compass.data.base

import android.util.Log
import com.nawrot.mateusz.compass.domain.base.SchedulersProvider
import io.reactivex.*
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers
import javax.inject.Inject
import javax.inject.Singleton


@Singleton
class AndroidSchedulersProvider @Inject constructor() : SchedulersProvider {

    override fun completableTransformer(): CompletableTransformer {
        Log.d("AndroidSchedulersProv", "CREATING COMPLETABLE TRANSFORMER");
        return CompletableTransformer { it ->
            it.subscribeOn(Schedulers.io()).observeOn(AndroidSchedulers.mainThread())
        }
    }

    override fun <T> singleTransformer(): SingleTransformer<T, T> {
        Log.d("AndroidSchedulersProv", "CREATING SINGLE TRANSFORMER");
        return SingleTransformer { it ->
            it.subscribeOn(Schedulers.io()).observeOn(AndroidSchedulers.mainThread())
        }
    }

    override fun <T> observableTransformer(): ObservableTransformer<T, T> {
        Log.d("AndroidSchedulersProv", "CREATING OBSERVABLE TRANSFORMER");
        return ObservableTransformer { it ->
            it.subscribeOn(Schedulers.io()).observeOn(AndroidSchedulers.mainThread())
        }
    }

}