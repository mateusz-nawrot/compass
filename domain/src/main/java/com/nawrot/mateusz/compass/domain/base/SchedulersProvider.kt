package com.nawrot.mateusz.compass.domain.base

import io.reactivex.CompletableTransformer
import io.reactivex.ObservableTransformer


interface SchedulersProvider {

    fun completableTransformer(): CompletableTransformer

    fun <T> observableTransformer(): ObservableTransformer<T, T>

}