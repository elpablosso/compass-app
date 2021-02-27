package com.example.test.compass.common

import android.view.View
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData

fun <T> LiveData<T>.updateValue(newValue: T?) {
    newValue?.also { (this as MutableLiveData).value = it }
}

fun <T> LiveData<T>.update(update: (T) -> T) {
    value?.let {
        updateValue(update.invoke(it))
    }
}

infix fun View.isVisibleWhen(shouldBeVisible: Boolean) {
    visibility = if(shouldBeVisible) View.VISIBLE else View.INVISIBLE
}