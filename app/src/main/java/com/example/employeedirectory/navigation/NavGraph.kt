package com.example.employeedirectory.navigation

import androidx.compose.runtime.Composable
import androidx.navigation.NavHostController
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.navArgument
import com.example.employeedirectory.screens.addUpdateEmployee.AddUpdateEmployee
import com.example.employeedirectory.screens.allEmployee.AllEmployee

@Composable
fun SetupNavGraph(navHostController: NavHostController) {
    NavHost(
        navHostController,
        startDestination = ScreenRoutes.Home.routes
    ) {
        composable(
            route = ScreenRoutes.Home.routes,

        ){

            AllEmployee(
                navigateToEdit = {id, name, salary, age ->
                    navHostController.navigate(ScreenRoutes.Edit.getEmployeeDetails(
                        id = id,
                        name =name,
                        salary = salary,
                        age = age
                    ))
                },

                onAddClick = {
                    navHostController.navigate(route="add")

                }
            )
        }
        composable(
            route = ScreenRoutes.Edit.routes,
            arguments = listOf(navArgument("id"){
                type = NavType.StringType
            }, navArgument("name"){
                type = NavType.StringType
            },
                navArgument("salary"){
                    type = NavType.StringType
                },
                navArgument("age"){
                    type = NavType.StringType
                },

                )

        ){
            val employeeId = it.arguments?.getString("id")
            val name = it.arguments?.getString("name")
            val salary = it.arguments?.getString("salary")
            val age = it.arguments?.getString("age")
            AddUpdateEmployee(
                employeeId = employeeId,
                name = name?:"",
                salary =salary?:"",
                age = age?:"",
                onBack = {
                navHostController.popBackStack()
            })
        }
        composable("add"){
            AddUpdateEmployee(onBack = {navHostController.popBackStack()}, employeeId =null , name =null , salary =null , age =null )
        }

    }


}

sealed class ScreenRoutes(val routes: String) {

    object Home : ScreenRoutes(routes = "home_screen")
    object Edit : ScreenRoutes(routes = "employee_details/{id}/{name}/{salary}/{age}") {
        fun getEmployeeDetails(id: String, name: String, salary:String, age:String): String {
            return "employee_details/$id/$name/$salary/$age"

        }

    }
}
