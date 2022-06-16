package com.example.doitsoon.ui.addedittask

import android.util.Log
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.doitsoon.data.TaskDao
import com.example.doitsoon.ui.list.ListViewModel
import com.example.doitsoon.ui.list.adapter.listitem.TaskItem
import dagger.assisted.Assisted
import dagger.assisted.AssistedInject
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class AddEditTaskDialogViewModel @Inject constructor(
       private val state: SavedStateHandle,
        private val taskDao: TaskDao) : ViewModel() {

    private val taskAddEditEventChannel = Channel<AddEditTaskEvents>()
    val taskAddEditEvents = taskAddEditEventChannel.receiveAsFlow()

    //this must have the same name as name given to the argument on the nav_graph
    val task = state.get<TaskItem>("taskItem")

    //when we retrieve the task if taskName is null use the task Name, but that can also be null, so use empty string instead
    var taskName = state.get<String>("taskName") ?: task?.taskName ?: ""
    set(value) {
        field = value
        state.set("taskName",value)//we saved our value on savedInstance state if we get out of the app it has something to read when we come back
    }

    var taskImportance = state.get<Boolean>("isPriority") ?: task?.isPriority ?: false
        set(value) {
            field = value
            state.set("isPriority",value)//we saved our value on savedInstance state if we get out of the app it has something to read when we come back
        }

    fun onSaveClick() {
        if(taskName.isBlank()){
            showInvalidMessage()
            return
        }

        if(task != null){
            updateTask(task.copy(taskName = taskName, isPriority = taskImportance))
        }else{
            val newTask = TaskItem(taskName,taskImportance)
            addTask(newTask)
        }
    }

    private fun updateTask(task: TaskItem) =
        viewModelScope.launch {
            taskDao.updateTask(task)
            taskAddEditEventChannel.send(AddEditTaskEvents.Success("Updated with success"))
        }

    private fun addTask(task: TaskItem){
        viewModelScope.launch {
            taskDao.insertTask(task)
            taskAddEditEventChannel.send(AddEditTaskEvents.Success("Added with success"))
        }
    }


    private fun showInvalidMessage()  =
        viewModelScope.launch {
            taskAddEditEventChannel.send(AddEditTaskEvents.ShowInvalidMessage("Task name cannot be empty"))
        }


    sealed class AddEditTaskEvents{
        data class Success(val successMessage: String) : AddEditTaskEvents()
        data class ShowInvalidMessage(val invalidMessage: String) : AddEditTaskEvents()
    }
}