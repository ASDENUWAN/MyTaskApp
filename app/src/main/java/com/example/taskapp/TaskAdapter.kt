package com.example.taskapp


import android.content.Intent
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.taskapp.database.Task
import com.example.taskapp.database.TaskRepo
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class TaskAdapter(var tasks: List<Task>, private var taskViewModel: TaskViewModel, private val repo: TaskRepo) :
    RecyclerView.Adapter<TaskAdapter.TaskViewHolder>() {

    class TaskViewHolder(itemView: View):RecyclerView.ViewHolder(itemView){
        val nametext:TextView = itemView.findViewById(R.id.nameTextView)
        val date:TextView = itemView.findViewById(R.id.dateTextView)
        val pnumber:TextView = itemView.findViewById(R.id.pnumTextView)
        val destext:TextView = itemView.findViewById(R.id.desTextView)
        val editBtn: ImageView = itemView.findViewById(R.id.updateButton)
        val deleteBtn: ImageView = itemView.findViewById(R.id.deleteButton)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): TaskViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.task_item,parent,false)
        return TaskViewHolder(view)
    }

    override fun getItemCount(): Int = tasks.size

    override fun onBindViewHolder(holder: TaskViewHolder, position: Int) {
        val task = tasks[position]
        val fullName = task.name
        val words = fullName.split(" ")
        val firstTwoWords = words.take(2).joinToString(" ")
        holder.nametext.text = firstTwoWords
        holder.date.text = task.date
        holder.pnumber.text = task.no.toString()
        holder.destext.text = task.description

        holder.editBtn.setOnClickListener{
            val intent = Intent(holder.itemView.context,EditTask::class.java).apply {
                putExtra("taskid",task.id)
            }
            holder.itemView.context.startActivity(intent)
        }

        holder.deleteBtn.setOnClickListener{
            CoroutineScope(Dispatchers.IO).launch {
                repo.delete(task)
                val data = repo.getAllTasks()
                withContext(Dispatchers.Main){
                    taskViewModel.setData(data)
                }

            }
        }
    }

}

