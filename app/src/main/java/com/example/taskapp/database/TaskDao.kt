package com.example.taskapp.database


import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Update


@Dao
interface TaskDao {
    @Insert(onConflict = OnConflictStrategy.IGNORE)
    suspend fun addTask(task: Task)

    @Update
    suspend fun updateTask(task: Task)

    @Query("SELECT * FROM mytask ORDER By no")
    fun getAllTask():List<Task>

    @Query("SELECT * FROM mytask WHERE id = :taskId")
    suspend fun getTaskById(taskId: Int): Task

    @Delete
    suspend fun removeTask(task: Task)
}