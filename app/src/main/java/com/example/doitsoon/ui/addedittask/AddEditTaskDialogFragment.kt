package com.example.doitsoon.ui.addedittask

import android.app.AlertDialog
import android.app.Dialog
import android.os.Bundle
import android.view.Gravity
import android.view.LayoutInflater
import androidx.fragment.app.DialogFragment
import com.example.doitsoon.databinding.AddEditTaskDialogFragmentBinding
import com.example.doitsoon.ui.list.adapter.listitem.TaskItem

class AddEditTaskDialogFragment(private val onSaveClickListener : (TaskItem) -> Unit) : DialogFragment() {

    private lateinit var binding : AddEditTaskDialogFragmentBinding

    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {

        binding = AddEditTaskDialogFragmentBinding.inflate(LayoutInflater.from(context))

        val builder = AlertDialog.Builder(requireActivity())
        builder.setView(binding.root)

        binding.saveButton.setOnClickListener {
            onSaveClickListener.invoke(TaskItem(binding.editTextTask.text.toString(),binding.priorityCheckBox.isChecked))
            dismiss()
        }

        val dialog = builder.create()
        dialog.window!!.setGravity(Gravity.CENTER)
        return dialog
    }
}