package com.example.doitsoon.ui.addedittask

import android.app.AlertDialog
import android.app.Dialog
import android.os.Bundle
import android.view.Gravity
import android.view.LayoutInflater
import androidx.core.view.isVisible
import androidx.fragment.app.DialogFragment
import androidx.fragment.app.viewModels
import com.example.doitsoon.databinding.AddEditTaskDialogFragmentBinding
import dagger.hilt.android.AndroidEntryPoint

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
            createdTextView.text = "Created - ${viewModel.task?.creationTime.toString()}"
        }



        val dialog = builder.create()
        dialog.window!!.setGravity(Gravity.CENTER)
        return dialog
    }
}