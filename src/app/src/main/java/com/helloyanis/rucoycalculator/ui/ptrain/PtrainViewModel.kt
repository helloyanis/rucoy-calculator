package com.helloyanis.rucoycalculator.ui.ptrain

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel

class PtrainViewModel : ViewModel() {

    private val _text = MutableLiveData<String>().apply {
        value = "Power training calculation coming soon!"
    }
    val text: LiveData<String> = _text
}