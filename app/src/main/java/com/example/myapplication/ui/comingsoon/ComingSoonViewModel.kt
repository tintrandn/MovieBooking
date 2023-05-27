package com.example.myapplication.ui.comingsoon

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel

class ComingSoonViewModel : ViewModel() {

    private val _text = MutableLiveData<String>().apply {
        value = "Hiện tại đang chưa khả dụng. \n Xin quý khách vui lòng quay lại sau."
    }
    val text: LiveData<String> = _text
}