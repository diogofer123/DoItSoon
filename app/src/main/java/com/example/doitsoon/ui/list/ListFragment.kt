package com.example.doitsoon.ui.list

import android.os.Bundle
import android.view.*
import androidx.appcompat.widget.SearchView
import androidx.fragment.app.Fragment
import androidx.fragment.app.setFragmentResultListener
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.ItemTouchHelper
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.doitsoon.R
import com.example.doitsoon.data.SortOrder
import com.example.doitsoon.databinding.ListFragmentBinding
import com.example.doitsoon.ui.list.adapter.TaskAdapter
import com.example.doitsoon.ui.list.adapter.listitem.TaskItem
import com.example.doitsoon.util.onQueryTextChanged
import com.google.android.material.snackbar.Snackbar
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.launch

@AndroidEntryPoint
class ListFragment : Fragment(R.layout.list_fragment),TaskAdapter.OnTaskClickedListener {

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
        setObservers()
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

        val taskAdapter = TaskAdapter(this)
        with(binding.taskRecyclerView) {
            layoutManager = LinearLayoutManager(context)
            adapter = taskAdapter
            setHasFixedSize(true)
        }

        viewModel.tasks.observe(viewLifecycleOwner){
            taskAdapter.submitList(it)
        }

        ItemTouchHelper(object : ItemTouchHelper.SimpleCallback(0,ItemTouchHelper.RIGHT or ItemTouchHelper.LEFT){
            override fun onMove(
                recyclerView: RecyclerView,
                viewHolder: RecyclerView.ViewHolder,
                target: RecyclerView.ViewHolder
            ): Boolean {
                return true
            }

            override fun onSwiped(viewHolder: RecyclerView.ViewHolder, direction: Int) {
                val task = taskAdapter.currentList[viewHolder.adapterPosition]
                viewModel.onTaskSwipe(task)
            }

        }).attachToRecyclerView(binding.taskRecyclerView)

    }

    private fun setListeners(){
        with(binding){
            addButton.setOnClickListener {
                viewModel.onAddNewTaskEvent()
            }
        }

        setFragmentResultListener("add_edit_result"){
            _, bundleOfData ->
            val resultMessage = bundleOfData.getString("add_edit_result_message")
            Snackbar.make(requireView(),resultMessage.toString(),Snackbar.LENGTH_LONG).show()
        }
    }


    private fun setObservers(){
        viewLifecycleOwner.lifecycleScope.launchWhenStarted {
            viewModel.taskEvents.collect { event ->
                when(event){
                    is ListViewModel.TaskEvents.ShowUndoDeleteTaskEvent -> {
                        Snackbar.make(requireView(),"Task Deleted",Snackbar.LENGTH_LONG)
                            .setAction("UNDO"){
                                viewModel.undoDeletedTask(event.task)
                            }
                            .show()
                    }
                    is ListViewModel.TaskEvents.AddNewTaskEvent -> {
                        val action = ListFragmentDirections.actionListFragment2ToAddEditTaskDialogFragment(null,"Add Task")
                        findNavController().navigate(action)
                    }

                    is ListViewModel.TaskEvents.UpdateTaskEvent -> {
                        val action = ListFragmentDirections.actionListFragment2ToAddEditTaskDialogFragment(event.task,"Edit Task")
                        findNavController().navigate(action)
                    }
                    ListViewModel.TaskEvents.NoEvents -> {}
                }
            }
        }
    }

    override fun onItemClick(taskItem: TaskItem) {
        viewModel.onEditNewTaskEvent(taskItem)
    }

    override fun onItemCheckBoxClicked(taskItem: TaskItem, isChecked: Boolean) {
        viewModel.onTaskCheckedChanged(taskItem,isChecked)
    }

}