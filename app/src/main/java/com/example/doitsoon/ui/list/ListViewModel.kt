package com.example.doitsoon.ui.list

import android.content.Context
import androidx.lifecycle.ViewModel
import androidx.lifecycle.asFlow
import androidx.lifecycle.asLiveData
import androidx.lifecycle.viewModelScope
import com.example.doitsoon.data.PreferencesManager
import com.example.doitsoon.data.SortOrder
import com.example.doitsoon.data.TaskDao
import com.example.doitsoon.ui.list.adapter.listitem.TaskItem
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class ListViewModel @Inject constructor(
    private val taskDao: TaskDao,
    private val preferencesManager: PreferencesManager, ) : ViewModel() {

    val searchQuery = MutableStateFlow("")

    val preferencesFlow = preferencesManager.preferencesFlow

    private val taskEventChannel = Channel<TaskEvents>()
    val taskEvents = taskEventChannel.receiveAsFlow()

    //flatMapLatest is a flow operator
    //detects when the searchQuery changes and pass it to the function in order to do the search
    //flatMapLatest is triggered,taskFlow is updated
    private val tasksFlow = combine(
        searchQuery,
        preferencesFlow
    ){ query,filterPreferences ->
        Pair(query,filterPreferences)
    }.flatMapLatest { (query,filterPreferences) ->
        taskDao.getTasks(query,filterPreferences.sortOrder,filterPreferences.hideCompleted).asFlow()
    }

    val tasks = tasksFlow.asLiveData()


    fun onSortOrderSelected(sortOrder: SortOrder,context: Context) = viewModelScope.launch {
        preferencesManager.updateSortOrder(sortOrder,context)
    }

    fun onHideCompletedClicked(hideCompleted: Boolean, context: Context) = viewModelScope.launch {
        preferencesManager.updateHideCompleted(hideCompleted,context)
    }

    fun onTaskCheckedChanged(taskItem: TaskItem, checked: Boolean) {
        viewModelScope.launch {
            taskDao.updateTask(taskItem.copy(isCompleted = checked))
        }
    }

    fun onTaskSwipe(task: TaskItem) {
        viewModelScope.launch {
            taskDao.deleteTask(task)
            taskEventChannel.send(TaskEvents.ShowUndoDeleteTaskEvent(task))
        }
    }

    fun undoDeletedTask(task: TaskItem) {
        viewModelScope.launch {
            taskDao.insertTask(task)
        }
    }

    fun onAddNewTaskEvent() = viewModelScope.launch {
        taskEventChannel.send(TaskEvents.AddNewTaskEvent)
    }

    fun onEditNewTaskEvent(task : TaskItem) = viewModelScope.launch {
        taskEventChannel.send(TaskEvents.UpdateTaskEvent(task))
    }

    fun onDeleteAllCompletedTasks() = viewModelScope.launch{
        taskEventChannel.send(TaskEvents.DeleteAllCompletedTaskEvent)
    }


    sealed class TaskEvents{
        data class ShowUndoDeleteTaskEvent(val task: TaskItem) : TaskEvents()
        object AddNewTaskEvent : TaskEvents()
        data class UpdateTaskEvent(val task: TaskItem) : TaskEvents()
        object DeleteAllCompletedTaskEvent : TaskEvents()

    }

}