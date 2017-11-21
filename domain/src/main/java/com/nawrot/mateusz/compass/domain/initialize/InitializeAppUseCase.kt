package com.nawrot.mateusz.compass.domain.initialize

import com.nawrot.mateusz.compass.domain.base.SchedulersProvider
import com.nawrot.mateusz.compass.domain.base.usecase.CompletableUseCase
import io.reactivex.Completable
import javax.inject.Inject


class InitializeAppUseCase @Inject constructor(schedulersProvider: SchedulersProvider,
                           private val initializeRepository: InitializeRepository) : CompletableUseCase<Unit>(schedulersProvider) {

    override fun createUseCaseCompletable(param: Unit): Completable {
        return initializeRepository.initializeApp()
    }
}