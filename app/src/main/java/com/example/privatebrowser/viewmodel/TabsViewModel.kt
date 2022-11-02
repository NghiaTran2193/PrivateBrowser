package com.example.privatebrowser.viewmodel

import android.app.Application
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.viewModelScope
import androidx.paging.Pager
import androidx.paging.PagingConfig
import androidx.paging.PagingData
import androidx.paging.cachedIn
import com.example.privatebrowser.model.Tabs
import com.example.privatebrowser.repository.TabRepository
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.launch

class TabsViewModel(application: Application, savedStateHandle: SavedStateHandle) :
    BaseViewModel(application, savedStateHandle) {
    val tabRepository: TabRepository by lazy {
        TabRepository.newInstance(application)
    }

    val listApp: Flow<PagingData<Tabs>> by lazy {
        Pager(PagingConfig(20, enablePlaceholders = false, initialLoadSize = 20)){
            tabRepository.getAllTab()
        }.flow.cachedIn(viewModelScope)
    }


    fun insertTab(tabs: Tabs){
        viewModelScope.launch(Dispatchers.IO) {
            tabRepository.insertTab(tabs)
        }
    }
    fun updateTab(tabs: Tabs){
        viewModelScope.launch(Dispatchers.IO) {
            tabRepository.updateTab(tabs)
        }
    }
    fun deleteTab(id : Long) {
        viewModelScope.launch(Dispatchers.IO) {
            tabRepository.deleteTab(id)
        }
    }


}