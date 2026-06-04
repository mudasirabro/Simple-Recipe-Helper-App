package com.example.simplerecipehelper.ui.cooking

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData

class CookingViewModel(application: Application) : AndroidViewModel(application) {

    private val _steps = MutableLiveData<List<String>>(emptyList())
    val steps: LiveData<List<String>> = _steps

    private val _currentIndex = MutableLiveData(0)
    val currentIndex: LiveData<Int> = _currentIndex

    fun setSteps(steps: List<String>) {
        _steps.value = steps
        _currentIndex.value = 0
    }

    fun nextStep() {
        val list = _steps.value ?: return
        val index = _currentIndex.value ?: 0
        if (index < list.size - 1) {
            _currentIndex.value = index + 1
        }
    }

    fun previousStep() {
        val index = _currentIndex.value ?: 0
        if (index > 0) {
            _currentIndex.value = index - 1
        }
    }

    fun markComplete() {
        nextStep()
    }

    fun resetToFirstStep() {
        _currentIndex.value = 0
    }
}


