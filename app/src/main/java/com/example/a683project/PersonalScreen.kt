package com.example.a683project

import android.app.TimePickerDialog
import android.content.Context
import android.media.MediaPlayer
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.Button
import androidx.compose.material.Divider
import androidx.compose.material.IconButton
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Scaffold
import androidx.compose.material.Text
import androidx.compose.material.TextField
import androidx.compose.material.TopAppBar
import androidx.compose.material.rememberScaffoldState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import androidx.navigation.NavHostController

@Composable
fun PersonalScreen(navController: NavHostController, paddingValues: PaddingValues) {
        Column(modifier = Modifier.padding(paddingValues)){
            BMI_Calculator()
    }
}


@Composable
fun BMI_Calculator() {
    val height = remember { mutableStateOf("") }
    val weight = remember { mutableStateOf("") }
    val bmi = remember { mutableStateOf<Double?>(null) }

    Column {
        Text(text = "BMI Calculator", style = MaterialTheme.typography.h6)
        TextField(
            value = height.value,
            onValueChange = {
                if (it.toFloatOrNull() != null && it.toFloat() >= 1 || it.isEmpty()) {
                    height.value = it
                }
            },
            label = { Text("Enter your height (cm)") },
            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Decimal)
        )
        Spacer(modifier = Modifier.height(8.dp))
        TextField(
            value = weight.value,
            onValueChange = {
                if (it.toFloatOrNull() != null && it.toFloat() >= 1 || it.isEmpty()) {
                    weight.value = it
                }
            },
            label = { Text("Enter your weight (kg)") },
            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Decimal)
        )
        Spacer(modifier = Modifier.height(16.dp))
        Button(onClick = {
            val h = height.value.toDoubleOrNull()
            val w = weight.value.toDoubleOrNull()
            if (h != null && h >= 1 && w != null && w >= 1) {
                bmi.value = w / ((h / 100) * (h / 100))
            } else {
                bmi.value = null
            }
        }) {
            Text("Calculate BMI")
        }
        bmi.value?.let {
            Text("Your BMI is: ${String.format("%.2f", it)}", style = MaterialTheme.typography.body1)
        }
    }
}

