package com.example.privatebrowser.viewmodel

import android.app.Application
import androidx.lifecycle.LiveData
import androidx.lifecycle.SavedStateHandle
import com.example.privatebrowser.utils.SingleLiveEvent

class PrivateBrowserViewModel(application: Application, savedStateHandle: SavedStateHandle) : BaseViewModel(application, savedStateHandle) {
    val listenEventOnce = SingleLiveEvent<Any>()
    val eventOnce: LiveData<Any> by lazy {
        listenEventOnce
    }
}