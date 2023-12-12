package com.example.a683project

import android.widget.ScrollView
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.Button
import androidx.compose.material.MaterialTheme
import androidx.compose.material.OutlinedTextField
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.*
import androidx.compose.runtime.getValue
import androidx.compose.runtime.setValue
import androidx.compose.ui.res.painterResource

@Composable
fun BMIAndBMRCalculator() {
    val heightState = remember { mutableStateOf("") }
    val weightState = remember { mutableStateOf("") }
    val ageState = remember { mutableStateOf("") }
    val genderState = remember { mutableStateOf("Male") }
    val bmiState = remember { mutableStateOf<Double?>(null) }
    val bmrState = remember { mutableStateOf<Double?>(null) }
    val invalidInputState = remember { mutableStateOf(false) }
    val activityLevels = listOf("Sedentary", "Lightly Active", "Moderately Active", "Very Active", "Extra Active")
    var selectedActivityLevel by remember { mutableStateOf(activityLevels[0]) }
    val invalidHeightState = remember { mutableStateOf(false) }
    val invalidWeightState = remember { mutableStateOf(false) }
    val invalidAgeState = remember { mutableStateOf(false) }
    val buttonModifier = Modifier
        .padding(8.dp)
        .size(width = 80.dp, height = 60.dp)
    val scrollState = rememberScrollState()
    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp)
            .verticalScroll(scrollState)
    ) {
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceEvenly
        ) {
            Button(
                onClick = { genderState.value = "Male" },
                colors = ButtonDefaults.buttonColors(
                    backgroundColor = if (genderState.value == "Male") MaterialTheme.colors.primary else Color(
                        0xFFFAF9F9
                    ),

                ),
                modifier = buttonModifier
            ) {
                Column(
                    horizontalAlignment = Alignment.CenterHorizontally,
                    verticalArrangement = Arrangement.Center
                ) {
                    Image(
                        painter = painterResource(id = R.drawable.male),
                        contentDescription = "Male",
                        modifier = Modifier
                            .size(120.dp)
                            .fillMaxWidth()
                    )
                    if (genderState.value == "Male") Text(
                        "Male",
                        color = Color.White
                    ) else Text("Male", color = Color.Black)
                }
            }
            Button(
                onClick = { genderState.value = "Female" },
                colors = ButtonDefaults.buttonColors(
                    backgroundColor = if (genderState.value == "Female") MaterialTheme.colors.primary else Color(
                        0xFFFAF9F9
                    )
                ),
                modifier = buttonModifier
            ) {
                Column(
                    horizontalAlignment = Alignment.CenterHorizontally,
                    verticalArrangement = Arrangement.Center
                ) {
                    Image(
                        painter = painterResource(id = R.drawable.female),
                        contentDescription = "Female",
                        modifier = Modifier
                            .size(120.dp)
                            .fillMaxWidth()
                    )
                    if (genderState.value == "Female") Text(
                        "Female",
                        color = Color.White
                    ) else Text("Female", color = Color.Black)
                }
            }
        }
        OutlinedTextField(
            value = ageState.value,
            onValueChange = { ageState.value = it },
            label = { Text("Enter your age (10-100 years)") },
            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
            singleLine = true,
            modifier = Modifier.fillMaxWidth()
        )
        OutlinedTextField(
            value = heightState.value,
            onValueChange = { heightState.value = it },
            label = { Text("Enter your height (100-250 cm)") },
            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Decimal),
            singleLine = true,
            modifier = Modifier.fillMaxWidth()
        )
        OutlinedTextField(
            value = weightState.value,
            onValueChange = { weightState.value = it },
            label = { Text("Enter your weight (30-300 kg)") },
            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Decimal),
            singleLine = true,
            modifier = Modifier.fillMaxWidth()
        )
        Column {
            activityLevels.forEach { level ->
                Row(verticalAlignment = Alignment.CenterVertically) {
                    RadioButton(
                        selected = selectedActivityLevel == level,
                        onClick = { selectedActivityLevel = level }
                    )
                    Text(level)
                }
            }
        }
        Button(onClick = {
            val height = heightState.value.toDoubleOrNull()
            val weight = weightState.value.toDoubleOrNull()
            val age = ageState.value.toIntOrNull()

            invalidHeightState.value = height == null || height !in 100.0..250.0
            invalidWeightState.value = weight == null || weight !in 30.0..300.0
            invalidAgeState.value = age == null || age !in 10..100

            if (!invalidHeightState.value && !invalidWeightState.value && !invalidAgeState.value) {
                bmiState.value = calculateBMI(weight!!, height!!)
                bmrState.value = calculateBMR(
                    weight,
                    height,
                    age!!,
                    genderState.value
                ) * getActivityLevelFactor(selectedActivityLevel)
            }
        }, modifier = Modifier.align(Alignment.CenterHorizontally)) {
            Text("Calculate BMI and BMR")
        }
        Spacer(modifier = Modifier.height(8.dp))
        if (invalidHeightState.value) {
            Text(
                "Invalid height! Please enter a value between 100-250 cm.",
                color = Color.Red,
                modifier = Modifier
                    .align(Alignment.CenterHorizontally)
                    .padding(vertical = 8.dp)
            )
        }
        if (invalidWeightState.value) {
            Text(
                "Invalid weight! Please enter a value between 30-300 kg.",
                color = Color.Red,
                modifier = Modifier
                    .align(Alignment.CenterHorizontally)
                    .padding(vertical = 8.dp)
            )
        }
        if (invalidAgeState.value) {
            Text(
                "Invalid age! Please enter a value between 10-100 years.",
                color = Color.Red,
                modifier = Modifier
                    .align(Alignment.CenterHorizontally)
                    .padding(vertical = 8.dp) // 调整垂直间距
            )
        }
        if (invalidInputState.value) {
            Text(
                "Please enter valid height (100-250 cm), weight (30-300 kg), and age (10-100 years).",
                color = Color.Red,
                modifier = Modifier
                    .align(Alignment.CenterHorizontally)
                    .padding(vertical = 8.dp)
            )
        }
        if (!invalidInputState.value && !invalidAgeState.value && !invalidHeightState.value && !invalidWeightState.value) {
            bmiState.value?.let { bmi ->
                val category = bmiCategory(bmi)
                Text(
                    "Your BMI is: ${bmi.format(2)} ($category)",
                    modifier = Modifier
                        .align(Alignment.CenterHorizontally)
                        .padding(vertical = 8.dp)
                )
            }
            bmrState.value?.let { bmr ->
                Text(
                    "Your BMR is: ${bmr.format(2)} kcal/day",
                    modifier = Modifier
                        .align(Alignment.CenterHorizontally)
                )
                Text(
                    "(Daily minimum caloric consumption)",
                    modifier = Modifier
                        .align(Alignment.CenterHorizontally)
                )
            }
        }
    }
}

fun calculateBMI(weight: Double, height: Double): Double {
    return weight / ((height / 100) * (height / 100))
}

fun calculateBMR(weight: Double, height: Double, age: Int, gender: String): Double {
    return if (gender == "Male") {
        88.362 + (13.397 * weight) + (4.799 * height) - (5.677 * age)
    } else {
        447.593 + (9.247 * weight) + (3.098 * height) - (4.330 * age)
    }
}

fun Double.format(digits: Int) = "%.${digits}f".format(this)

fun bmiCategory(bmi: Double): String = when {
    bmi < 18.5 -> "Underweight"
    bmi in 18.5..24.9 -> "Normal weight"
    bmi in 25.0..29.9 -> "Overweight"
    else -> "Obese"
}

fun getActivityLevelFactor(activityLevel: String): Double {
    return when (activityLevel) {
        "Sedentary" -> 1.2
        "Lightly Active" -> 1.375
        "Moderately Active" -> 1.55
        "Very Active" -> 1.725
        "Extra Active" -> 1.9
        else -> 1.0
    }
}