package com.example.taskapp.database

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase


@Database(entities = [Task::class], version = 1, exportSchema = false)
abstract class TaskDB : RoomDatabase(){

    abstract fun taskDao(): TaskDao

    companion object{
        @Volatile
        private var INSTANCE : TaskDB?=null

        fun getDatabase(context: Context): TaskDB {
            val tempI = INSTANCE
            if(tempI != null){
                return tempI
            }
            synchronized(this){
                val instance = Room.databaseBuilder(
                    context.applicationContext,
                    TaskDB::class.java,
                    "taskdb"
                ).build()
                INSTANCE = instance
                return instance
            }
        }
    }
}