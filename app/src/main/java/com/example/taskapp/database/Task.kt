package com.example.taskapp.database

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "mytask")
data class Task(
    @PrimaryKey(autoGenerate = true)
    val id:Int,
    val name:String,
    val date :String,
    val no:Int,
    val description:String
)
