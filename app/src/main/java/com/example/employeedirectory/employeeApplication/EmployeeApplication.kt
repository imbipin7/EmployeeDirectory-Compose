package com.example.employeedirectory.employeeApplication

import android.app.Application
import dagger.hilt.android.HiltAndroidApp

@HiltAndroidApp
class EmployeeApplication:Application() {
    override fun onCreate() {
        super.onCreate()
    }
}