package com.nawrot.mateusz.compass.domain.initialize

import io.reactivex.Completable


interface InitializeRepository {

    fun initializeApp(): Completable

}