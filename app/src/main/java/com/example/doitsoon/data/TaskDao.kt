package com.example.doitsoon.data

import androidx.lifecycle.LiveData
import androidx.room.*
import com.example.doitsoon.ui.list.adapter.listitem.TaskItem

@Dao
interface TaskDao {

    @Query("SELECT * FROM task_table WHERE taskName LIKE '%' || :searchQuery || '%' ORDER BY isPriority")
    fun getTasks(searchQuery: String): LiveData<List<TaskItem>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertTask(task: TaskItem)

    @Update
    suspend fun updateTask(task: TaskItem)

    @Delete
    suspend fun deleteTask(task: TaskItem)
}