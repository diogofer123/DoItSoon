package com.example.doitsoon.util

import androidx.appcompat.widget.SearchView

//extension for searchView to be used on fragment
inline fun SearchView.onQueryTextChanged(crossinline listener: (String) -> Unit) {
    this.setOnQueryTextListener(object : SearchView.OnQueryTextListener {
        override fun onQueryTextSubmit(query: String?): Boolean {
            //triggered when something is submited on the text field(no need for this app)
            return true
        }

        override fun onQueryTextChange(newText: String?): Boolean {
            //triggered when something is texted on the text field
            listener(newText.orEmpty())
            return true
        }

    })
}