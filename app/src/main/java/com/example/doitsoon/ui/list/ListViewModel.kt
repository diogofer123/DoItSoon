package com.example.doitsoon.ui.list

import androidx.lifecycle.ViewModel
import com.example.doitsoon.data.TaskDao
import dagger.hilt.android.lifecycle.HiltViewModel
import dagger.hilt.android.scopes.ViewModelScoped
import javax.inject.Inject

@HiltViewModel
class ListViewModel @Inject constructor(private val taskDao: TaskDao) : ViewModel() {

    val tasks = taskDao.getTasks()

}