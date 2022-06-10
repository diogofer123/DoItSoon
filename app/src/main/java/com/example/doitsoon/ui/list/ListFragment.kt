package com.example.doitsoon.ui.list

import android.os.Bundle
import android.view.*
import androidx.appcompat.widget.SearchView
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.doitsoon.R
import com.example.doitsoon.data.SortOrder
import com.example.doitsoon.databinding.ListFragmentBinding
import com.example.doitsoon.ui.list.adapter.TaskAdapter
import com.example.doitsoon.util.onQueryTextChanged
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.launch

@AndroidEntryPoint
class ListFragment : Fragment(R.layout.list_fragment) {

    companion object {
        fun newInstance() = ListFragment()
    }

    private lateinit var binding: ListFragmentBinding

    private val viewModel: ListViewModel by viewModels()


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
        setHasOptionsMenu(true)
    }

    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
       inflater.inflate(R.menu.menu_fragment_tasks,menu)

        val searchItem = menu.findItem(R.id.action_search)
        val searchView = searchItem.actionView as SearchView


        searchView.onQueryTextChanged {
            viewModel.searchQuery.value = it
        }

        viewLifecycleOwner.lifecycleScope.launch{
            menu.findItem(R.id.action_hide_completed).isChecked =
            viewModel.preferencesFlow.first().hideCompleted
        }
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
       return when(item.itemId){
            R.id.action_sort_by_name -> {
                context?.let { viewModel.onSortOrderSelected(SortOrder.BY_NAME_DESC, it) }
                true
            }
            R.id.action_sort_by_date -> {
                context?.let { viewModel.onSortOrderSelected(SortOrder.BY_DATE, it) }
                true
            }
            R.id.action_hide_completed -> {
                item.isChecked = !item.isChecked
                context?.let { viewModel.onHideCompletedClicked(item.isChecked, it) }
                true
            }
            R.id.action_delete_completed ->{

                true
            }
           else -> super.onOptionsItemSelected(item)
       }
    }

    private fun initializeList() {

        val taskAdapter = TaskAdapter()
        with(binding.taskRecyclerView) {
            layoutManager = LinearLayoutManager(context)
            adapter = taskAdapter
            setHasFixedSize(true)
        }

        viewModel.tasks.observe(viewLifecycleOwner){
            taskAdapter.submitList(it)
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