package com.example.doitsoon.ui.list.adapter.listitem

import androidx.room.Entity
import androidx.room.PrimaryKey
import java.text.DateFormat

@Entity(tableName = "task_table")
data class TaskItem (
    val taskName: String,
    val isPriority: Boolean = false,
    val creationTime: Long = System.currentTimeMillis(),
    @PrimaryKey(autoGenerate = true) val Id : Int = 0
){
    val formatDate :String
        get() = DateFormat.getDateTimeInstance().format(creationTime)
}