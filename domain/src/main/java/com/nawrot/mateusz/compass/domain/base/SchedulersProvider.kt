package com.nawrot.mateusz.compass.domain.base

import io.reactivex.ObservableTransformer


interface SchedulersProvider {

    fun <T> observableTransformer(): ObservableTransformer<T, T>

}