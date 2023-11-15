package com.example.a683project

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.Button
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.material.TextField
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp


@Composable
fun BMI_Calculator() {
    val height = remember { mutableStateOf("") }
    val weight = remember { mutableStateOf("") }
    val bmi = remember { mutableStateOf<Double?>(null) }
    val showInvalidInput = remember { mutableStateOf(false) }
    val invalidInput = remember { mutableStateOf(false) }

    Column {
        Text(text = "BMI Calculator", style = MaterialTheme.typography.h6)
        TextField(
            value = height.value,
            onValueChange = {
                height.value = it
                showInvalidInput.value = false // Reset invalid input message when user is typing
            },
            label = { Text("Enter your height (cm)") },
            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Decimal)
        )
        Spacer(modifier = Modifier.height(8.dp))
        TextField(
            value = weight.value,
            onValueChange = {
                weight.value = it
                showInvalidInput.value = false // Reset invalid input message when user is typing
            },
            label = { Text("Enter your weight (kg)") },
            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Decimal)
        )
        Spacer(modifier = Modifier.height(16.dp))
        Button(onClick = {
            val h = height.value.toDoubleOrNull()
            val w = weight.value.toDoubleOrNull()
            if (h != null && h in 100.0..250.0 && w != null && w in 30.0..300.0) {
                bmi.value = w / ((h / 100) * (h / 100))
                invalidInput.value = false
            } else {
                bmi.value = null
                invalidInput.value = true
            }
            showInvalidInput.value = invalidInput.value // Show invalid input message only after calculation attempt
        }) {
            Text("Calculate BMI")
        }
        if (showInvalidInput.value) {
            Text("Invalid input entered", color = Color.Red)
        }
        bmi.value?.let {
            Text("Your BMI is: ${String.format("%.2f", it)}", style = MaterialTheme.typography.body1)
            Text("You are " + bmiCategory(it), style = MaterialTheme.typography.body2)
        }
    }
}

fun bmiCategory(bmi: Double): String {
    return when {
        bmi < 18.5 -> "Underweight"
        bmi < 25 -> "Normal weight"
        bmi < 30 -> "Overweight"
        else -> "Obesity"
    }
}
