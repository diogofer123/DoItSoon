package com.example.doitsoon.ui.list.adapter.listitem

import java.text.DateFormat


data class TaskItem (
    val taskName: String,
    val isPriority: Boolean,
    val creationTime: Long = System.currentTimeMillis()
){
    val formatDate :String
        get() = DateFormat.getDateTimeInstance().format(creationTime)
}