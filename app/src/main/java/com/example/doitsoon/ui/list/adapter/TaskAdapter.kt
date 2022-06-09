package com.example.doitsoon.ui.list.adapter

import android.graphics.Paint
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.core.view.isVisible
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.example.doitsoon.databinding.TaskModelBinding
import com.example.doitsoon.ui.list.adapter.listitem.TaskItem

class TaskAdapter() : ListAdapter<TaskItem,TaskAdapter.TaskViewHolder>(DiffCallBack()) {



    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): TaskViewHolder {
        val binding = TaskModelBinding.inflate(LayoutInflater.from(parent.context),parent,false)
        return TaskViewHolder(binding)
    }

    override fun onBindViewHolder(holder: TaskViewHolder, position: Int) {
        val taskItem = getItem(position)
        holder.bind(taskItem)
    }

    class TaskViewHolder(private val binding: TaskModelBinding) : RecyclerView.ViewHolder(binding.root) {

        fun bind(taskItem: TaskItem) {
            with(binding){
                taskText.text = taskItem.taskName
                priorityBell.isVisible = taskItem.isPriority
                isCompleted.isChecked = taskItem.isCompleted
            }

            if(taskItem.isCompleted){
                with(binding){
                    taskText.apply {
                        paintFlags = Paint.STRIKE_THRU_TEXT_FLAG
                    }
                }
            }
        }

    }

    class DiffCallBack: DiffUtil.ItemCallback<TaskItem>(){

        override fun areItemsTheSame(oldItem: TaskItem, newItem: TaskItem): Boolean = oldItem.Id == newItem.Id

        override fun areContentsTheSame(oldItem: TaskItem, newItem: TaskItem): Boolean = oldItem == newItem

    }
}

