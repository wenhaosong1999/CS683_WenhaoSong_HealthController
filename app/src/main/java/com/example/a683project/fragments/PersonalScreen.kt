package com.example.a683project.fragments

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.padding
import androidx.compose.material.Scaffold
import androidx.compose.material.Text
import androidx.compose.material.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.navigation.NavHostController
import com.example.a683project.BMIAndBMRCalculator

@Composable
fun PersonalScreen(navController: NavHostController,paddingValues: PaddingValues) {
    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("BMI and BMR Calculator",
                    fontWeight = FontWeight.Normal) },
                actions = {
                }
            )
        }
    ) { paddingValues ->
        PersonalContent(paddingValues)
    }
}

@Composable
fun PersonalContent(paddingValues: PaddingValues) {
    Column(modifier = Modifier.padding(paddingValues)) {
        BMIAndBMRCalculator()
    }
}




