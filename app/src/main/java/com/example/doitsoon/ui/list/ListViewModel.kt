package com.example.doitsoon.ui.list

import androidx.lifecycle.ViewModel
import androidx.lifecycle.asFlow
import androidx.lifecycle.asLiveData
import com.example.doitsoon.data.TaskDao
import dagger.hilt.android.lifecycle.HiltViewModel
import dagger.hilt.android.scopes.ViewModelScoped
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.flatMapLatest
import javax.inject.Inject

@HiltViewModel
class ListViewModel @Inject constructor(private val taskDao: TaskDao) : ViewModel() {

    val searchQuery = MutableStateFlow("")

    //flatMapLatest is a flow operator
    //detects when the searchQuery changes and pass it to the function in order to do the search
    //flatMapLatest is triggered,taskFlow is updated
    private val taskFlow = searchQuery.flatMapLatest {
        taskDao.getTasks(it).asFlow()
    }

    val tasks = taskFlow.asLiveData()

}