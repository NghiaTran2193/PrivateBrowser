package com.example.privatebrowser.repository

import android.app.Application
import androidx.lifecycle.LiveData
import androidx.paging.PagingSource
import com.example.privatebrowser.database.RoomDB
import com.example.privatebrowser.model.App

class AppRepository (application: Application) {
    companion object{
        private var instance : AppRepository? = null
        fun  newInstance(application: Application) : AppRepository{
            if (instance == null){
                instance = AppRepository(application)
            }
            return instance!!
        }
    }
    val appDao = RoomDB.getAppDatabase(application).appDao()
    fun getAll(): PagingSource<Int, App>{
        return appDao.getAll()
    }
    fun insertApp(app: App){
        appDao.insertApp(app)
    }
    fun deleteApp(id: Long){
        appDao.deleteApp(id)
    }
    fun getCount(): LiveData<Int>{
        return appDao.getCount()
    }
}