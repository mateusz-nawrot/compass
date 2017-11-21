package com.nawrot.mateusz.compass.data.initialize

import com.nawrot.mateusz.compass.domain.initialize.InitializeRepository
import io.reactivex.Completable
import java.util.concurrent.TimeUnit
import javax.inject.Inject


class MNInitializeRepository @Inject constructor() : InitializeRepository {

    //dummy implementation, just to have nice splash screen background with animation at the beginning :)
    override fun initializeApp(): Completable {
        return Completable.fromAction { /* do nothing*/ }.delay(2, TimeUnit.SECONDS)
    }
}