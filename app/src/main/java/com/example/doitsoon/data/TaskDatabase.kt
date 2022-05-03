package com.example.doitsoon.data

import androidx.room.Database
import androidx.room.RoomDatabase
import com.example.doitsoon.ui.list.adapter.listitem.TaskItem

@Database(entities = [TaskItem::class], version = 1)
abstract class TaskDatabase : RoomDatabase() {

    abstract fun taskDao() : TaskDao
}