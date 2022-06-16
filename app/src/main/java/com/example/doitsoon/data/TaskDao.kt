package com.example.doitsoon.data

import android.os.FileObserver.DELETE
import androidx.lifecycle.LiveData
import androidx.room.*
import com.example.doitsoon.ui.list.ListViewModel
import com.example.doitsoon.ui.list.adapter.listitem.TaskItem
import java.util.concurrent.Flow

@Dao
interface TaskDao {

    fun getTasks(query: String,sortOrder: SortOrder, hideComplete: Boolean) : LiveData<List<TaskItem>> =
        when(sortOrder){
            SortOrder.BY_DATE -> getTasksSortedByDate(searchQuery = query, hideComplete = hideComplete)
            SortOrder.BY_NAME_DESC -> getTasksSortedByName(searchQuery = query, hideComplete = hideComplete)
        }

    @Query("SELECT * FROM task_table WHERE (isCompleted != :hideComplete OR isCompleted = 0) AND taskName LIKE '%' || :searchQuery || '%' ORDER BY isPriority DESC,(taskName)")
    fun getTasksSortedByName(searchQuery: String,hideComplete: Boolean): LiveData<List<TaskItem>>

    @Query("SELECT * FROM task_table WHERE (isCompleted != :hideComplete OR isCompleted = 0) AND taskName LIKE '%' || :searchQuery || '%' ORDER BY isPriority DESC,(creationTime)")
    fun getTasksSortedByDate(searchQuery: String, hideComplete: Boolean): LiveData<List<TaskItem>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertTask(task: TaskItem)

    @Update
    suspend fun updateTask(task: TaskItem)

    @Delete
    suspend fun deleteTask(task: TaskItem)

    @Query("DELETE FROM task_table WHERE isCompleted = 1")
    fun deleteAllCompletedTAsks()
}