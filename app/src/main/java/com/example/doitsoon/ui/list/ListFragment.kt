package com.example.doitsoon.ui.list

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.doitsoon.R
import com.example.doitsoon.databinding.ListFragmentBinding
import com.example.doitsoon.ui.list.adapter.TaskAdapter
import com.example.doitsoon.ui.list.adapter.listitem.TaskItem
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class ListFragment : Fragment(R.layout.list_fragment) {

    companion object {
        fun newInstance() = ListFragment()
    }

    private lateinit var binding: ListFragmentBinding

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = ListFragmentBinding.inflate(inflater, container, false)
        return binding.root
    }


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initializeList()
        setListeners()
    }

    private fun initializeList() {
        val tasks = listOf(
            TaskItem("Ir ao médico", false),
            TaskItem("Ir ás comrpas", false), TaskItem("Arrumar a casa", true)
        )

        with(binding.taskRecyclerView) {
            layoutManager = LinearLayoutManager(context)
            adapter = TaskAdapter(tasks)
        }
    }

    private fun setListeners(){

        with(binding){
            addButton.setOnClickListener {
                //Add task to the list

            }
        }
    }

}