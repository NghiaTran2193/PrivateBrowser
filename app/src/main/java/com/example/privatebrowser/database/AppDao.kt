package com.example.privatebrowser.database

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.paging.PagingSource
import androidx.room.*
import com.example.privatebrowser.model.App
import com.example.privatebrowser.model.Tabs

@Dao
interface AppDao {
    @Query("select * from Bookmark order by id desc")
    fun getAll() : PagingSource<Int, App>
    @Insert(onConflict = OnConflictStrategy.IGNORE)
    fun insertApp(app: App)
    @Query("select count(*) from Bookmark")
    fun getCount() : LiveData<Int>
    @Query("delete from Bookmark where id =:id")
    fun deleteApp(id : Long)
    @Query("update Bookmark set isCheck = 1 where id=:id")
    fun updateApp(id: Long)
    @Query("update Bookmark set isCheck = 0")
    fun updateAppAll()
}