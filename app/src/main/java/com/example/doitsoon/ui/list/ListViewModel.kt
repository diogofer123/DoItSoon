package com.example.doitsoon.ui.list

import androidx.lifecycle.ViewModel
import com.example.doitsoon.data.TaskDao
import javax.inject.Inject

class ListViewModel @Inject constructor(private val taskDao: TaskDao) : ViewModel() {

    val tasks = taskDao.getTasks()
    
}