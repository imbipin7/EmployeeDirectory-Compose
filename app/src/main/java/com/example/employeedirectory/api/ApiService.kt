package com.example.employeedirectory.api

import com.example.employeedirectory.screens.allEmployee.Employee
import com.google.gson.annotations.SerializedName
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.DELETE
import retrofit2.http.GET
import retrofit2.http.POST
import retrofit2.http.PUT
import retrofit2.http.Path


interface ApiService {
    companion object{
        const val EMPLOYEES="api/v1/employees"
        const val EMPLOYEE="api/v1/employee"
        const val ADD_EMPLOYEE="api/v1/create"
        const val UPDATE_EMPLOYEE="api/v1/update/{id}"
        const val DELETE_EMPLOYEE="api/v1/delete/{id}"
    }
    @GET(EMPLOYEES)
    suspend fun getEmployees(): Response<ApiResponse<List<Employee>>>

    @GET("$EMPLOYEE/{id}")
    suspend fun getEmployee(@Path("id") id:String): Response<ApiResponse<Employee>>

    @POST(ADD_EMPLOYEE)
    suspend fun addEmployee(@Body data:EmployeeRequest): Response<ApiResponse<EmployeeRequest>>

    @PUT(UPDATE_EMPLOYEE)
    suspend fun updateEmployee(@Path("id")id:String,@Body data:EmployeeRequest): Response<ApiResponse<EmployeeRequest>>

    @DELETE(DELETE_EMPLOYEE)
    suspend fun deleteEmployee(@Path("id")id:String): Response<ApiResponse<String>>

}
class ApiResponse<T> {
    @SerializedName("success",alternate = ["statusCode","status"])
    val status: String? = null
    @SerializedName("message",alternate = ["msg"])
    val msg: String? = null
    val data: T? = null
}
data class EmployeeRequest(
    var id: Int?=null,
    val age: String,
    val name: String,
    val salary: String
)