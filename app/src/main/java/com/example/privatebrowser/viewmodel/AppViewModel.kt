package com.example.privatebrowser.viewmodel

import android.app.Application
import androidx.lifecycle.*

import androidx.paging.Pager
import androidx.paging.PagingConfig
import androidx.paging.PagingData
import androidx.paging.cachedIn
import com.example.privatebrowser.R
import com.example.privatebrowser.model.App
import com.example.privatebrowser.repository.AppRepository
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.launch


class AppViewModel(application: Application) : AndroidViewModel(application) {
 val appRepository: AppRepository by lazy {
    AppRepository.newInstance(application)
}

    val listApp: Flow<PagingData<App>> by lazy {
        Pager(PagingConfig(10, enablePlaceholders = false, initialLoadSize = 20)){
            appRepository.getAll()
        }.flow.cachedIn(viewModelScope)
    }
     fun insertApp(app: App){
         viewModelScope.launch(Dispatchers.IO){
             appRepository.insertApp(app)
         }
     }
    fun deleteApp(id : Long){
        viewModelScope.launch(Dispatchers.IO){
            appRepository.deleteApp(id)
        }
    }

    val getCount : LiveData<Int> by lazy {
        appRepository.getCount()
    }

    fun updateApp(id: Long){
        viewModelScope.launch(Dispatchers.IO) {
            appRepository.appDao.updateApp(id)
        }
    }
    fun updateAppAll(){
        viewModelScope.launch(Dispatchers.IO) {
            appRepository.appDao.updateAppAll()
        }
    }

    fun load(){
        insertApp(App(name = "Add more", logo = R.drawable.ic_add_more))
    }

}