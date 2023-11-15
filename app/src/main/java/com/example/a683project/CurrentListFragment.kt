package com.example.a683project

import android.media.Image
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.padding
import androidx.compose.material.IconButton
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.navigation.NavHostController

@Composable
fun CurrentListFragment(navController: NavHostController, kind:String) {
    if (kind == "meat"){
        Column {
            Row(verticalAlignment = Alignment.CenterVertically) {
                ImageButton(
                    imageRes = R.drawable.meat,
                    contentDescription = "meat",
                    onClick = { navController.navigate("meatDetail/1") }
                )
                Column(modifier = Modifier.padding(start = 8.dp)) {
                    Text("Name", fontWeight = FontWeight.Bold)
                    Text("Made of")
                }
            }
        }
    }else if (kind == "vegetable"){
        Column {
            Row(verticalAlignment = Alignment.CenterVertically) {
                ImageButton(
                    imageRes = R.drawable.vegetable,
                    contentDescription = "vegetable",
                    onClick = { navController.navigate("vegetableDetail/1") }
                )
                Column(modifier = Modifier.padding(start = 8.dp)) {
                    Text("Name", fontWeight = FontWeight.Bold)
                    Text("Made of")
                }
            }
        }
    }else if (kind == "cuisine"){
        Column {
            Row(verticalAlignment = Alignment.CenterVertically) {
                ImageButton(
                    imageRes = R.drawable.cuisine,
                    contentDescription = "cuisine",
                    onClick = { navController.navigate("cuisineDetail/1") }
                )
                Column(modifier = Modifier.padding(start = 8.dp)) {
                    Text("Name", fontWeight = FontWeight.Bold)
                    Text("Made of")
                }
            }
        }
    }else if (kind == "noodle"){
        Column {
            Row(verticalAlignment = Alignment.CenterVertically) {
                ImageButton(
                    imageRes = R.drawable.noodle,
                    contentDescription = "noodle",
                    onClick = { navController.navigate("noodleDetail/1") }
                )
                Column(modifier = Modifier.padding(start = 8.dp)) {
                    Text("Name", fontWeight = FontWeight.Bold)
                    Text("Made of")
                }
            }
        }
    }else{
        Column {
            Row(verticalAlignment = Alignment.CenterVertically) {
                ImageButton(
                    imageRes = R.drawable.meat,
                    contentDescription = "meat",
                    onClick = { navController.navigate("vegetableDetail/1") }
                )
                Column(modifier = Modifier.padding(start = 8.dp)) {
                    Text("Name", fontWeight = FontWeight.Bold)
                    Text("Made of")
                }
            }
        }
    }
}
