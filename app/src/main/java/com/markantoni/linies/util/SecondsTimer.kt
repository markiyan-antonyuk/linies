package com.markantoni.linies.util

import io.reactivex.Observable
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.Disposable
import io.reactivex.schedulers.Schedulers
import java.util.concurrent.TimeUnit

class SecondsTimer(private val action: () -> Unit) {
    private val observable = Observable.fromCallable { action() }
            .observeOn(AndroidSchedulers.mainThread())
            .subscribeOn(Schedulers.newThread())
            .delay(1, TimeUnit.SECONDS)
            .repeat()

    private var disposable: Disposable? = null

    fun start() {
        disposable?.let { if (!it.isDisposed) return }
        disposable = observable.subscribe()
    }

    fun stop() {
        disposable?.dispose()
    }
}