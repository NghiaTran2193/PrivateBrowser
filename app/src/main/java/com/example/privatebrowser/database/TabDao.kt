package com.example.privatebrowser.database

import androidx.lifecycle.LiveData
import androidx.paging.PagingSource
import androidx.room.*
import com.example.privatebrowser.model.Tabs

@Dao
interface TabDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insertTab(tabs: Tabs)
    @Query ("select * from Tabs")
    fun selectAllTab() : PagingSource<Int,Tabs>
    @Update
    fun updateTab(tabs: Tabs)
    @Query ("delete from Tabs")
    fun deleteAllTab()
    @Query ("delete from tabs where id = :id")
    fun deleteTab(id: Long)
    @Query("select count(*) from Tabs ")
    fun getCount(): LiveData<Int>

}