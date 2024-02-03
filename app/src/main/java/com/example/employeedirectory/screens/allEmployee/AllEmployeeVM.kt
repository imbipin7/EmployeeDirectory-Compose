package com.example.employeedirectory.screens.allEmployee

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.employeedirectory.api.ApiService
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class AllEmployeeVM @Inject constructor(private val apiService: ApiService) : ViewModel() {
    val loading by lazy { MutableLiveData<Boolean>() }
    val employeeList by lazy { MutableLiveData<List<Employee>>() }
    val error by lazy { MutableLiveData<String>() }

    fun fetchEmployees() {
        loading.value = true
        viewModelScope.launch {
            try {
                val response = apiService.getEmployees()
                loading.value = false
                if (response.isSuccessful) {
                    employeeList.value = response.body()?.data
                } else {
                    if (response.code() == 429) {
                        error.value = "Too Many Requests!"
                    } else {
                        error.value = response.message()
                    }
                }
            } catch (e: Exception) {
                loading.value = false
                error.value = e.message
            }
        }
    }


}

