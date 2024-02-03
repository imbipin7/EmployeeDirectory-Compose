package com.example.employeedirectory.screens.allEmployee

import android.annotation.SuppressLint
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.Add
import androidx.compose.material.icons.rounded.Person
import androidx.compose.material.icons.rounded.Refresh
import androidx.compose.material.icons.rounded.Search
import androidx.compose.material3.Card
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.ColorFilter
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel

@Composable
fun AllEmployee(
    navigateToEdit: (String, String, String, String) -> Unit,
    onAddClick: () -> Unit
) {

    EmployeeListContent(navigateToEdit, onAddClick = onAddClick)
}


@SuppressLint("UnusedMaterial3ScaffoldPaddingParameter")
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun EmployeeListContent(
    navigateToEdit: (String, String, String, String) -> Unit,
    onAddClick: () -> Unit,
    viewModel: AllEmployeeVM = hiltViewModel()
) {
    var employeeList = viewModel.employeeList.observeAsState(initial = emptyList()).value

    val isLoading = viewModel.loading.observeAsState(initial = false)
    var error = viewModel.error.observeAsState().value
    val searchText = remember {
        mutableStateOf("")
    }
    LaunchedEffect(key1 = Unit) {
        viewModel.fetchEmployees()
        error = ""

    }

    Scaffold(
        topBar = {
            TopAppBar(
                title = {
                    Text(
                        text = "All Employees",
                        style = MaterialTheme.typography.titleLarge,
                        fontFamily = FontFamily.SansSerif
                    )
                },
                actions = {
                    IconButton(onClick = {
                        viewModel.fetchEmployees()
                    }) {
                        Icon(
                            imageVector = Icons.Rounded.Refresh,
                            contentDescription = null,
                            tint = MaterialTheme.colorScheme.surfaceTint
                        )
                    }
                    IconButton(onClick = {
                        onAddClick.invoke()
                    }) {
                        Icon(imageVector = Icons.Rounded.Add, contentDescription = null)
                    }
                }
            )
        },
        content = {
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(horizontal = 3.dp),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Spacer(modifier = Modifier.height(60.dp))
                OutlinedTextField(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 9.dp),
                    shape = RoundedCornerShape(10.dp),
                    colors = TextFieldDefaults.colors(
                        unfocusedContainerColor = Color.White,
                        focusedContainerColor = Color.White
                    ),
                    value = searchText.value,
                    onValueChange = {
                        searchText.value = it
                        employeeList = employeeList.filter {
                            it.employee_name.lowercase().contains(
                                searchText.value.lowercase()
                            )
                        }

                    },
                    label = { Text(text = "Search") },
                    leadingIcon = {
                        Image(
                            imageVector = Icons.Rounded.Search,
                            contentDescription = "",
                            colorFilter = ColorFilter.tint(MaterialTheme.colorScheme.primary)
                        )
                    }

                )
                if (isLoading.value) {
                    Box(modifier = Modifier.padding(top = 10.dp)) {
                        Text(text = "Please wait....")
                    }

                }
                EmployeeListItemScreen(employees = employeeList, onItemClick = {
                    navigateToEdit(it.id, it.employee_name, it.employee_salary, it.employee_age)
                })


            }
        }
    )

}

@Composable
fun EmployeeListItemScreen(
    employees: List<Employee>,
    onItemClick: (Employee) -> Unit
) {
    LazyColumn {
        items(employees.size) { i ->
            EmployeeItem(employee = employees[i], onItemClick = onItemClick)
        }
    }
}

@Composable
fun EmployeeItem(employee: Employee, onItemClick: (Employee) -> Unit) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(8.dp)
            .clickable { onItemClick(employee) }
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp)
        ) {
            // Employee Picture
            Image(
                imageVector = Icons.Rounded.Person,
                contentDescription = null,
                modifier = Modifier
                    .size(60.dp)
                    .clip(CircleShape)
                    .background(MaterialTheme.colorScheme.inversePrimary)
            )

            Spacer(modifier = Modifier.width(16.dp))

            // Employee Details
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .align(Alignment.CenterVertically)
            ) {
                Text(
                    text = employee.employee_name,
                    style = MaterialTheme.typography.labelLarge,
                    fontFamily = FontFamily.SansSerif
                )
                Spacer(modifier = Modifier.height(4.dp))
                Text(
                    text = "$${employee.employee_salary}",
                    style = MaterialTheme.typography.labelMedium,
                    fontFamily = FontFamily.SansSerif
                )
                Spacer(modifier = Modifier.height(4.dp))
                Text(
                    text = "Age: ${employee.employee_age}",
                    style = MaterialTheme.typography.labelMedium,
                    fontFamily = FontFamily.SansSerif
                )
            }
        }
    }
}

@Preview(showSystemUi = true, showBackground = true)
@Composable
fun AllEmployeePreview() {
    val list = mutableListOf<Employee>()
    list.add(Employee("1", "Bipin", "5000", "23", "Image"))

}
