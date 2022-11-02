package com.example.privatebrowser.repository

import android.app.Application
import androidx.lifecycle.LiveData
import androidx.paging.PagingData
import androidx.paging.PagingSource
import com.example.privatebrowser.database.RoomDB
import com.example.privatebrowser.model.App
import com.example.privatebrowser.model.Tabs

class TabRepository (application: Application) {
    companion object{
        private var instance : TabRepository? = null
        fun  newInstance(application: Application) : TabRepository{
            if (instance == null){
                instance = TabRepository(application)
            }
            return instance!!
        }
    }
    private val tabDao = RoomDB.getAppDatabase(application).tabDao()

    fun getAllTab(): PagingSource<Int, Tabs>{
       return tabDao.selectAllTab()
    }
    fun deleteAllTab() {
        tabDao.deleteAllTab()
    }
    fun deleteTab(id : Long){
        tabDao.deleteTab(id)
    }
    fun getCount():LiveData<Int>{
        return  tabDao.getCount()
    }

    fun insertTab(tabs: Tabs){
        tabDao.insertTab(tabs)
    }
    fun updateTab(tabs: Tabs){
        tabDao.updateTab(tabs)
    }
}