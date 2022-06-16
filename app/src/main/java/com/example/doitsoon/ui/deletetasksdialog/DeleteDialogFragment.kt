package com.example.doitsoon.ui.deletetasksdialog

import android.app.AlertDialog
import android.app.Dialog
import android.os.Bundle
import androidx.fragment.app.DialogFragment
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class DeleteDialogFragment : DialogFragment() {

    private val viewModel: DeleteDialogFragmentViewModel by viewModels()

    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
        return AlertDialog.Builder(requireContext())
            .setTitle("Delete completed tasks")
            .setMessage("Do you really want to erase all completed tasks")
            .setPositiveButton("Yes") {_ , _ ->
                viewModel.deleteAllTasks()
            }
            .setNegativeButton("No"){_,_ ->
                findNavController().popBackStack()
            }
            .create()
    }
}