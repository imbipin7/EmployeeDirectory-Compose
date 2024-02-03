package com.example.employeedirectory.screens.addUpdateEmployee

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.employeedirectory.api.ApiService
import com.example.employeedirectory.api.EmployeeRequest
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class AddUpdateEmployeeVM @Inject constructor(private val apiService: ApiService) : ViewModel() {
    val loading by lazy { MutableLiveData<Boolean>() }
    val addEmployee by lazy { MutableLiveData<EmployeeRequest>() }
    val error by lazy { MutableLiveData<String>() }

    fun addEmployee(data: EmployeeRequest) {
        loading.value = true
        viewModelScope.launch {
            try {
                val response = apiService.addEmployee(data)
                loading.value = false
                if (response.isSuccessful) {
                    addEmployee.value = response.body()?.data
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

    fun updateEmployee(data: EmployeeRequest, employeeId: String?) {
        loading.value = true
        viewModelScope.launch {
            try {
                val response = apiService.updateEmployee(employeeId ?: "", data)
                loading.value = false
                if (response.isSuccessful) {
                    addEmployee.value = response.body()?.data
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