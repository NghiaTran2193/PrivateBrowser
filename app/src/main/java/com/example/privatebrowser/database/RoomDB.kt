package com.example.privatebrowser.database

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import com.example.privatebrowser.model.App
import com.example.privatebrowser.model.Tabs

@Database(
    entities = [App::class, Tabs::class],
    version = 2,
    exportSchema = false
)
abstract class RoomDB: RoomDatabase() {
    abstract fun appDao() : AppDao
    abstract fun tabDao() : TabDao
    companion object{
        private var INSTATE: RoomDB? = null
        fun getAppDatabase(context: Context): RoomDB{
            if (INSTATE == null){
                INSTATE = Room.databaseBuilder(context, RoomDB::class.java, "private_browser")
                    .fallbackToDestructiveMigration()
                    .build()
            }
            return INSTATE!!
        }
//        fun getInstance(context: Context): RoomDB?{
//            return Room.databaseBuilder(context,RoomDB::class.java,"web.db").allowMainThreadQueries().build()
//        }
    }
}