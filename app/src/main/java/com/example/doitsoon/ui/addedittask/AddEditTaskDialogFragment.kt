package com.example.doitsoon.ui.addedittask

import android.app.AlertDialog
import android.app.Dialog
import android.os.Bundle
import android.view.Gravity
import android.view.LayoutInflater
import androidx.core.os.bundleOf
import androidx.core.view.isVisible
import androidx.core.widget.addTextChangedListener
import androidx.fragment.app.DialogFragment
import androidx.fragment.app.setFragmentResult
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import com.example.doitsoon.databinding.AddEditTaskDialogFragmentBinding
import com.google.android.material.snackbar.Snackbar
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.flow.collect

@AndroidEntryPoint
class AddEditTaskDialogFragment() : DialogFragment() {

    private val viewModel: AddEditTaskDialogViewModel by viewModels()

    private lateinit var binding : AddEditTaskDialogFragmentBinding

    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {

        binding = AddEditTaskDialogFragmentBinding.inflate(LayoutInflater.from(context))

        val builder = AlertDialog.Builder(requireActivity())
        builder.setView(binding.root)

        with(binding){
            editTextTask.setText(viewModel.taskName)
            priorityCheckBox.isChecked = viewModel.taskImportance
            priorityCheckBox.jumpDrawablesToCurrentState()
            createdTextView.isVisible = viewModel.task != null
            createdTextView.text = viewModel.task?.formatDate


            editTextTask.addTextChangedListener {
                viewModel.taskName = it.toString()
            }

            priorityCheckBox.setOnCheckedChangeListener{ _, isChecked ->
                viewModel.taskImportance = isChecked
            }

            saveButton.setOnClickListener {
                viewModel.onSaveClick()
            }

            parentFragment?.viewLifecycleOwner?.lifecycleScope?.launchWhenStarted {
                viewModel.taskAddEditEvents.collect { event ->
                    when(event){
                        is AddEditTaskDialogViewModel.AddEditTaskEvents.ShowInvalidMessage -> {
                            Snackbar.make(parentFragment!!.requireView(),event.invalidMessage,Snackbar.LENGTH_LONG).show()
                        }
                        is AddEditTaskDialogViewModel.AddEditTaskEvents.Success -> {
                        setFragmentResult(
                            "add_edit_result", bundleOf("add_edit_result_message" to event.successMessage)
                        )
                            findNavController().popBackStack()
                        }
                    }
                }
            }
        }


        val dialog = builder.create()
        dialog.window!!.setGravity(Gravity.CENTER)
        return dialog
    }
}