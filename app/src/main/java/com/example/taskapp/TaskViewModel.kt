package com.example.taskapp


import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.taskapp.database.Task


class TaskViewModel:ViewModel(){
    private val _data =MutableLiveData<List<Task>>()

    val data:LiveData<List<Task>> = _data

    fun setData(newdata:List<Task>){
        _data.value = newdata
    }
}