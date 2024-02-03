package com.example.employeedirectory.screens.addUpdateEmployee

import android.annotation.SuppressLint
import android.util.Log
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.rounded.KeyboardArrowLeft
import androidx.compose.material.icons.rounded.DateRange
import androidx.compose.material.icons.rounded.Person
import androidx.compose.material.icons.rounded.Star
import androidx.compose.material3.Button
import androidx.compose.material3.CenterAlignedTopAppBar
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.ColorFilter
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import com.example.employeedirectory.api.EmployeeRequest


@SuppressLint("UnusedMaterial3ScaffoldPaddingParameter")
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AddUpdateEmployee(
    onBack: () -> Unit,
    employeeId: String?,
    viewModel: AddUpdateEmployeeVM = hiltViewModel(),
    name: String?,
    salary: String?,
    age: String?
) {
    var nameText by remember {
        mutableStateOf("")
    }
    var ageText by remember {
        mutableStateOf("")
    }
    var salaryText by remember {
        mutableStateOf("")
    }
    var buttonText by remember {
        mutableStateOf("Save")
    }

    LaunchedEffect(key1  = Unit){
        if (!employeeId.isNullOrEmpty()){
            Log.d("oks", "AddUpdateEmployee: $employeeId")
            if (name != null) {
                nameText = name
            }
            if (age != null) {
                ageText =age
            }
            if (salary != null) {
                salaryText = salary
            }
            buttonText = "Update"


        }
    }


    Scaffold(
        modifier = Modifier.fillMaxSize(),
        topBar = {
            CenterAlignedTopAppBar(
                title = {
                    Text(text = "Employee Details", style =
                    MaterialTheme.typography.titleLarge,
                        fontFamily = FontFamily.SansSerif) },
                navigationIcon = { Image(imageVector = Icons.AutoMirrored.Rounded.KeyboardArrowLeft, contentDescription ="" , modifier = Modifier.clickable {
                    onBack.invoke()
                }) }

            )
        }
    ) {

        AddEditEmployeeContent(modifier = Modifier.padding(it),
            ageText = ageText,
            nameText = nameText,
            salaryText = salaryText,
            onNameChange = {
                nameText = it

        },
            onSalaryChange = {
                             salaryText = it

            },
            onAgeChange = {
                ageText = it

            },
            onSubmitClick = {
                if (nameText.isNotEmpty() && salaryText.isNotEmpty() && ageText.isNotEmpty()){
                    if (!employeeId.isNullOrEmpty()){
                        viewModel.updateEmployee(EmployeeRequest(age = ageText, name = nameText, salary = salaryText),employeeId = employeeId)


                    }else{
                        viewModel.addEmployee(
                            EmployeeRequest(age = ageText, name = nameText, salary = salaryText)
                        )
                    }



                }
            },
            buttonText = buttonText.toString()
        )
    }

}


@Composable
fun AddEditEmployeeContent(
    modifier: Modifier,
    nameText:String,
    onNameChange:(String)->Unit,

    salaryText:String,
    onSalaryChange:(String)->Unit,

    ageText:String,
    onAgeChange:(String)->Unit,

    onSubmitClick:()->Unit,

    buttonText:String,
) {
    Column(
        modifier = modifier
    ) {
        Spacer(modifier = Modifier.height(15.dp))
        Box(modifier = Modifier.fillMaxWidth()) {
            Column(
                modifier = Modifier.fillMaxWidth(),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Box(
                    modifier = Modifier
                        .size(120.dp)
                        .clip(CircleShape)
                        .background(MaterialTheme.colorScheme.primaryContainer),
                    contentAlignment = Alignment.Center
                ) {
                    Image(
                        imageVector = Icons.Rounded.Person,
                        contentDescription = "", modifier = Modifier.size(100.dp)
                    )
                }
                Spacer(modifier = Modifier.height(9.dp))

                Spacer(modifier = Modifier.height(18.dp))
                CustomTextField(
                    imageVector = Icons.Rounded.Person,
                    text = "Name",
                    onValueChange = {
                      onNameChange(it)
                    },
                    textValue = nameText,

                )

                CustomTextField(
                    imageVector = Icons.Rounded.Star,
                    text = "Salary",

                    onValueChange = {
                        onSalaryChange(it)
                    },
                    textValue = salaryText,
                )

                CustomTextField(
                    imageVector = Icons.Rounded.DateRange,
                    text = "Age",
                    onValueChange = {
                        onAgeChange(it)
                    },
                    textValue = ageText,
                )


                Button(
                    onClick = {
                              onSubmitClick()
                              }, modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 35.dp, vertical = 50.dp)
                ) {
                    Text(
                        text = buttonText,
                        style = MaterialTheme.typography.titleMedium,
                        fontFamily = FontFamily.SansSerif
                    )

                }


            }
        }

    }

}

@Composable
private fun CustomTextField(
    imageVector: ImageVector,
    text: String,
    textValue:String,
    onValueChange:(String)->Unit
) {
    OutlinedTextField(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 9.dp),
        shape = RoundedCornerShape(10.dp),
        colors = TextFieldDefaults.colors(unfocusedContainerColor = Color.White, focusedContainerColor = Color.White),
        value = textValue,
        onValueChange = {
            onValueChange(it)
        },
        label = { Text(text = text) },
        leadingIcon = {
            Image(
                imageVector = imageVector,
                contentDescription = "",
                colorFilter = ColorFilter.tint(MaterialTheme.colorScheme.primary)
            )
        }

    )

}

@Preview(showSystemUi = true, showBackground = true)
@Composable
fun AllEmployeePreview() {


}