package com.example.doitsoon.ui.list

import android.content.Context
import androidx.lifecycle.ViewModel
import androidx.lifecycle.asFlow
import androidx.lifecycle.asLiveData
import androidx.lifecycle.viewModelScope
import com.example.doitsoon.data.PreferencesManager
import com.example.doitsoon.data.SortOrder
import com.example.doitsoon.data.TaskDao
import dagger.hilt.android.lifecycle.HiltViewModel
import dagger.hilt.android.qualifiers.ApplicationContext
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.flatMapLatest
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class ListViewModel @Inject constructor(private val taskDao: TaskDao,private val preferencesManager: PreferencesManager) : ViewModel() {

    val searchQuery = MutableStateFlow("")


    private val preferencesFlow = preferencesManager.preferencesFlow

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

}