package com.example.taskapp

import android.content.Intent
import android.os.Bundle
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.taskapp.database.TaskDB
import com.example.taskapp.database.TaskRepo
import com.example.taskapp.databinding.ActivityMainBinding
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch


class MainActivity : AppCompatActivity() {
    private lateinit var binding: ActivityMainBinding
    private lateinit var taskAdapter: TaskAdapter
    private lateinit var repo: TaskRepo
    private lateinit var taskViewModel: TaskViewModel


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)
        enableEdgeToEdge()

        taskViewModel = ViewModelProvider(this).get(TaskViewModel::class.java)

        repo = TaskRepo(TaskDB.getDatabase(this))

        taskViewModel.data.observe(this){
            taskAdapter = TaskAdapter(it,taskViewModel, repo)
            binding.taskRecyclerView.layoutManager = LinearLayoutManager(this)
            binding.taskRecyclerView.adapter = taskAdapter
        }

        CoroutineScope(Dispatchers.IO).launch {
            val data = repo.getAllTasks()
            runOnUiThread {
                taskViewModel.setData(data)
            }
        }

        binding.addBtn.setOnClickListener{
            val intent = Intent(this, AddTask::class.java)
            startActivity(intent)
        }

    }

    override fun onResume() {
        super.onResume()
        CoroutineScope(Dispatchers.IO).launch {
            val data = repo.getAllTasks()
            runOnUiThread {
                taskViewModel.setData(data)
            }
        }
    }
}
