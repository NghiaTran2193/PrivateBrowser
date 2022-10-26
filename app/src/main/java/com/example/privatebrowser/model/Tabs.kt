package com.example.privatebrowser.model

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "Tabs")
data class Tabs(
    @PrimaryKey (autoGenerate = true) val id: Long=0,
    val imageWeb: String="",
    var urlNew: String="",
    var title : String=""
    ): java.io.Serializable