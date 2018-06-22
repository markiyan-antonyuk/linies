package com.markantoni.linies.watchface

import io.reactivex.Observable
import io.reactivex.disposables.Disposable
import io.reactivex.schedulers.Schedulers
import java.util.concurrent.TimeUnit

class DrawingThread(frame: () -> Unit) {
    private val observable = Observable.fromCallable { frame() }
            .subscribeOn(Schedulers.newThread())
            .delay(16, TimeUnit.MILLISECONDS)
            .repeat()

    private var disposable: Disposable? = null

    init {
        disposable = observable.subscribe()
    }

    fun stop() {
        disposable?.dispose()
    }
}