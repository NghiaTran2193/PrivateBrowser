package com.example.privatebrowser.viewmodel

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.SavedStateHandle
import io.reactivex.disposables.CompositeDisposable

abstract class BaseViewModel(application: Application, private val handle : SavedStateHandle) : AndroidViewModel(application) {
    val compositeDisposable: CompositeDisposable by lazy {
        CompositeDisposable()
    }

    /*Save data*/
    fun saveDataStore() {

    }

    fun restoreData() {

    }
    /**/

    override fun onCleared() {
        super.onCleared()
        compositeDisposable.clear()
    }
}