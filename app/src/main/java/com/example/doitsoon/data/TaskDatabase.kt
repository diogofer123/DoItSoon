package com.example.doitsoon.data

import androidx.room.Database
import androidx.room.RoomDatabase
import androidx.sqlite.db.SupportSQLiteDatabase
import com.example.doitsoon.di.modules.ApplicationScope
import com.example.doitsoon.ui.list.adapter.listitem.TaskItem
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.launch
import javax.inject.Inject
import javax.inject.Provider

@Database(entities = [TaskItem::class], version = 2)
abstract class TaskDatabase : RoomDatabase() {

    abstract fun taskDao() : TaskDao

    class Callback @Inject constructor(private val database: Provider<TaskDatabase>,@ApplicationScope private val applicationScope: CoroutineScope) : RoomDatabase.Callback(){

        override fun onDestructiveMigration(db: SupportSQLiteDatabase) {
            super.onDestructiveMigration(db)

            val dao = database.get().taskDao()

            applicationScope.launch {
                dao.insertTask(TaskItem("Tarefa", isPriority = false, isCompleted = false))
                dao.insertTask(TaskItem("Levar as compras", isPriority = false, isCompleted = false))
                dao.insertTask(TaskItem("Trabalhar na app", isPriority = true, isCompleted = false))
                dao.insertTask(TaskItem("Comprar coisas", isPriority = false, isCompleted = true))

            }
        }
    }


}