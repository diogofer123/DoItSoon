package com.example.doitsoon.ui.list.adapter.listitem

import android.os.Parcelable
import androidx.room.Entity
import androidx.room.PrimaryKey
import kotlinx.android.parcel.Parcelize
import java.text.DateFormat

@Entity(tableName = "task_table")
@Parcelize
data class TaskItem (
    val taskName: String,
    val isPriority: Boolean = false,
    val isCompleted: Boolean = false,
    val creationTime: Long = System.currentTimeMillis(),
    @PrimaryKey(autoGenerate = true) val Id : Int = 0
) : Parcelable {
    val formatDate :String
        get() = DateFormat.getDateTimeInstance().format(creationTime)
}