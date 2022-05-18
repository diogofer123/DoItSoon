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

@Database(entities = [TaskItem::class], version = 1)
abstract class TaskDatabase : RoomDatabase() {

    abstract fun taskDao() : TaskDao


    class Callback @Inject constructor(private val database: Provider<TaskDatabase>,@ApplicationScope private val applicationScope: CoroutineScope) : RoomDatabase.Callback(){

        override fun onCreate(db: SupportSQLiteDatabase) {
            super.onCreate(db)

            //db.operations
            val dao = database.get().taskDao()

            applicationScope.launch {
                dao.insertTask(TaskItem("Levar o lixo"))
                dao.insertTask(TaskItem("Lavar a roupa"))
                dao.insertTask(TaskItem("Comprar bilhetes",isPriority = true))
                dao.insertTask(TaskItem("Limpar quarto"))
                dao.insertTask(TaskItem("Tirar a carta internacional"))

            }

        }
    }
}