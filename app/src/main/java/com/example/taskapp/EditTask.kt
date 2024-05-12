package com.example.taskapp

import android.app.AlertDialog
import android.app.DatePickerDialog
import android.os.Bundle
import android.widget.ArrayAdapter
import android.widget.Button
import android.widget.Spinner
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import com.example.taskapp.database.Task
import com.example.taskapp.database.TaskDB
import com.example.taskapp.database.TaskRepo
import com.example.taskapp.databinding.ActivityEditTaskBinding
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import java.util.Calendar

class EditTask : AppCompatActivity() {
    private lateinit var dateButton: Button
    private lateinit var datePickerDialog: DatePickerDialog
    private lateinit var binding: ActivityEditTaskBinding
    private var taskId:Int=-1
    private lateinit var task:Task

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityEditTaskBinding.inflate(layoutInflater)
        setContentView(binding.root)
        enableEdgeToEdge()
        initDatePicker()

        dateButton = findViewById(R.id.editdatePickerButton)

        val repo = TaskRepo(TaskDB.getDatabase(this))
        val spinner = findViewById<Spinner>(R.id.editpnum_spinner)
        val priorities = resources.getStringArray(R.array.priority_options)
        val adapter = ArrayAdapter(this, android.R.layout.simple_spinner_item, priorities)
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
        spinner.adapter = adapter
        taskId = intent.getIntExtra("taskid",-1)
        if(taskId==-1){
            finish()
            return
        }

        CoroutineScope(Dispatchers.IO).launch {
            val task = repo.getTask(taskId)
            dateButton.text = task.date
            binding.editnameEditText.setText(task.name)
            binding.editdesEditText.setText(task.description)
            val valueToSet = task.no
            spinner.setSelection(valueToSet-1)
        }

        binding.editsaveButton.setOnClickListener{
            val name = binding.editnameEditText.text.toString()
            val date = binding.editdatePickerButton.text.toString()
            val selectedPriorityno = spinner.selectedItem.toString()
            val dec = binding.editdesEditText.text.toString()
            task = Task(taskId,name,date,Integer.parseInt(selectedPriorityno),dec)
            CoroutineScope(Dispatchers.IO).launch {
                repo.update(task)
                finish()
            }
        }
    }

    private fun initDatePicker() {
        val dateSetListener = DatePickerDialog.OnDateSetListener { _, year, month, day ->
            val formattedDate = makeDateString(day, month + 1, year)
            dateButton.text = formattedDate
        }

        val cal = Calendar.getInstance()
        val year = cal.get(Calendar.YEAR)
        val month = cal.get(Calendar.MONTH)
        val day = cal.get(Calendar.DAY_OF_MONTH)

        val style = AlertDialog.THEME_HOLO_LIGHT

        datePickerDialog = DatePickerDialog(this, style, dateSetListener, year, month, day)

    }

    private fun makeDateString(day: Int, month: Int, year: Int): String {
        return "$day/$month/$year"
    }

    fun editopenDatePicker(view: android.view.View) {
        datePickerDialog.show()
    }


}