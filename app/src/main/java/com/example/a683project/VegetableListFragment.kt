package com.example.a683project

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.runtime.Composable
import androidx.navigation.NavHostController

@Composable
fun VegetableListFragment(navController: NavHostController) {
    Column {
        Row(horizontalArrangement = Arrangement.SpaceBetween) {
            ImageButton(imageRes = R.drawable.ic_launcher_foreground, contentDescription = "vegetable") {
                navController.navigate("vegetableDetail/1")
            }
            ImageButton(imageRes = R.drawable.ic_launcher_foreground, contentDescription = "vegetable") {
                navController.navigate("vegetableDetail/2")
            }
            // 为其他两种蔬菜添加ImageButton
        }
        // 根据需要添加更多的Row来展示其他食物
    }
}