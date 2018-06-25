package com.markantoni.linies.app

import androidx.annotation.LayoutRes
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.constraintlayout.widget.ConstraintSet
import androidx.fragment.app.FragmentActivity
import androidx.lifecycle.*
import androidx.transition.TransitionManager

inline fun <reified T : ViewModel> FragmentActivity.viewModel(): Lazy<T> = lazy { ViewModelProviders.of(this).get(T::class.java) }

fun <T> LifecycleOwner.observe(liveData: LiveData<T>, observer: (T) -> Unit) = liveData.observe(this, Observer { observer(it as T) })

fun ConstraintLayout.animateToSet(@LayoutRes id: Int) {
    ConstraintSet().apply {
        clone(context, id)
        applyTo(this@animateToSet)
    }
    TransitionManager.beginDelayedTransition(this)
}