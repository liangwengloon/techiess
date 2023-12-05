package com.example.techiess.ui.compare

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel

class CompareViewModel : ViewModel() {

    private val _text = MutableLiveData<String>().apply {
        value = "This is Compare Fragment"
    }
    val text: LiveData<String> = _text
}