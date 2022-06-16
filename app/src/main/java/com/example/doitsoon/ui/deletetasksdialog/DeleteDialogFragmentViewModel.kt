package com.example.doitsoon.ui.deletetasksdialog

import androidx.lifecycle.ViewModel
import com.example.doitsoon.data.TaskDao
import com.example.doitsoon.di.modules.ApplicationScope
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class DeleteDialogFragmentViewModel @Inject constructor(
    private val taskDao: TaskDao,
    @ApplicationScope private val applicationScope: CoroutineScope
) : ViewModel() {

     fun deleteAllTasks() = applicationScope.launch{
        taskDao.deleteAllCompletedTAsks()

    }

}