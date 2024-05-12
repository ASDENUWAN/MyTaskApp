package com.example.taskapp.database

class TaskRepo (private val db: TaskDB){

    suspend fun insert(task: Task) = db.taskDao().addTask(task)
    suspend fun update(task: Task) = db.taskDao().updateTask(task)
    suspend fun delete(task: Task) = db.taskDao().removeTask(task)
    suspend fun getTask(taskId: Int):Task = db.taskDao().getTaskById(taskId)

    fun getAllTasks():List<Task> = db.taskDao().getAllTask()
}