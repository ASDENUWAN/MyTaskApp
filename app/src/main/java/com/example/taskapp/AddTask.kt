package com.example.taskapp

import android.os.Bundle
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import android.app.AlertDialog
import android.app.DatePickerDialog
import android.widget.ArrayAdapter
import android.widget.Button
import android.widget.Spinner
import android.widget.Toast
import com.example.taskapp.database.Task
import com.example.taskapp.database.TaskDB
import com.example.taskapp.database.TaskRepo
import com.example.taskapp.databinding.ActivityAddTaskBinding
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import java.util.Calendar

class AddTask : AppCompatActivity() {
    private lateinit var dateButton:Button
    private lateinit var datePickerDialog:DatePickerDialog
    private lateinit var binding:ActivityAddTaskBinding
    //private lateinit var taskView : TaskView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityAddTaskBinding.inflate(layoutInflater)
        setContentView(binding.root)
        enableEdgeToEdge()
        initDatePicker()
        dateButton = findViewById(R.id.datePickerButton)

        dateButton.text = getTodaysDate()

        val repo = TaskRepo(TaskDB.getDatabase(this))
        val spinner = findViewById<Spinner>(R.id.pnum_spinner)
        val priorities = resources.getStringArray(R.array.priority_options)

        val adapter = ArrayAdapter(this, android.R.layout.simple_spinner_item, priorities)
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
        spinner.adapter = adapter


        binding.saveButton.setOnClickListener{
            val name = binding.nameEditText.text.toString()
            val date = binding.datePickerButton.text.toString()
            val selectedPriorityno = spinner.selectedItem.toString()
            val dec = binding.desEditText.text.toString()

            val task = Task(0,name,date,Integer.parseInt(selectedPriorityno),dec)
            CoroutineScope(Dispatchers.IO).launch {
                repo.insert(task)
            }
            finish()
            Toast.makeText(this, "Added", Toast.LENGTH_SHORT).show()
        }

    }
    private fun getTodaysDate(): String {
        val cal = Calendar.getInstance()
        val year = cal.get(Calendar.YEAR)
        val month = cal.get(Calendar.MONTH) + 1
        val day = cal.get(Calendar.DAY_OF_MONTH)
        return makeDateString(day, month, year)
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
        //datePickerDialog.datePicker.maxDate = System.currentTimeMillis()
    }

    private fun makeDateString(day: Int, month: Int, year: Int): String {
        return "$day/$month/$year"
    }


    fun openDatePicker(view: android.view.View) {
        datePickerDialog.show()
    }
}