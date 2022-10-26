package com.example.privatebrowser.database

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.paging.PagingSource
import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.Query
import com.example.privatebrowser.model.App
import com.example.privatebrowser.model.Tabs

@Dao
interface AppDao {
    @Query("select * from Bookmark order by id desc")
    fun getAll() : PagingSource<Int, App>
    @Insert
    fun insertApp(app: App)
//    @Delete
//    fun deleteApp(app: App)
    @Query("select count(*) from Bookmark")
    fun getCount() : LiveData<Int>
    @Query("delete from Bookmark where id =:id")
    fun deleteApp(id : Long)
}