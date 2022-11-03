package com.example.privatebrowser.model

import androidx.room.Entity
import androidx.room.Index
import androidx.room.PrimaryKey
import com.example.privatebrowser.R

@Entity(tableName = "Bookmark",
    indices = [Index(value = ["url"], unique = true)]
)
data class App(
    @PrimaryKey(autoGenerate = true) val id: Long=0,
    val name: String="",
    val url: String="",
    val logo : Int = 0,
    val isCheck : Boolean = false
    )
