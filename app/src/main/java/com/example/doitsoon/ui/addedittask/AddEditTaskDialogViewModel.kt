package com.example.doitsoon.ui.addedittask

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import com.example.doitsoon.ui.list.adapter.listitem.TaskItem
import dagger.assisted.Assisted
import dagger.assisted.AssistedInject
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject


class AddEditTaskDialogViewModel @AssistedInject constructor(
     @Assisted private val state: SavedStateHandle) : ViewModel() {

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
}