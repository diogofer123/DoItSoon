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

class TaskAdapter(private val clickActions: onTaskClickedListener) : ListAdapter<TaskItem,TaskAdapter.TaskViewHolder>(DiffCallBack()) {


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): TaskViewHolder {
        val binding = TaskModelBinding.inflate(LayoutInflater.from(parent.context),parent,false)
        return TaskViewHolder(binding,clickActions)
    }

    override fun onBindViewHolder(holder: TaskViewHolder, position: Int) {
        val taskItem = getItem(position)
        holder.bind(taskItem)
    }

    inner class TaskViewHolder(private val binding: TaskModelBinding, private val clickActions: onTaskClickedListener) : RecyclerView.ViewHolder(binding.root) {


        init {
            binding.apply {
                root.setOnClickListener{
                    val position = adapterPosition
                    if(position != RecyclerView.NO_POSITION){
                        val task = getItem(position)
                        clickActions.onItemClick(task)
                    }

                }
                isCompleted.setOnClickListener {
                    val position = adapterPosition
                    //if the position is different of -1
                    if(position != RecyclerView.NO_POSITION){
                        val task = getItem(position)
                        clickActions.onItemCheckBoxClicked(task,isCompleted.isChecked)
                    }
                }
            }
        }


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

            clickActions.onItemClick(taskItem)
            clickActions.onItemCheckBoxClicked(taskItem,taskItem.isCompleted)
        }

    }

    class DiffCallBack: DiffUtil.ItemCallback<TaskItem>(){

        override fun areItemsTheSame(oldItem: TaskItem, newItem: TaskItem): Boolean = oldItem.Id == newItem.Id

        override fun areContentsTheSame(oldItem: TaskItem, newItem: TaskItem): Boolean = oldItem == newItem

    }

    interface onTaskClickedListener {
        fun onItemClick(taskItem : TaskItem)
        fun onItemCheckBoxClicked(taskItem: TaskItem,isChecked: Boolean)
    }
}

