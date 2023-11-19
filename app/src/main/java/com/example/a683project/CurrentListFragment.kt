package com.example.a683project

import android.media.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material.IconButton
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.navigation.NavHostController

@Composable
fun CurrentListFragment(navController: NavHostController, kind: String) {
    Column {
    Box(
        modifier = Modifier
            .background(Color(0xFFFFA500))
            .fillMaxWidth()
            .height(160.dp),
        contentAlignment = Alignment.CenterStart
    ) {
        Column(modifier = Modifier.fillMaxHeight()) {
            TopBar()
            AppBar()
        }
    }
    if (kind == "see all") {
        val kinds = listOf("meat", "vegetable", "soup", "cuisine", "noodle")
        Column {
            kinds.forEach { itemKind ->
                ItemRow(navController, itemKind)
            }
        }
    } else {
        ItemRow(navController, kind)
    }
}
}

@Composable
fun ItemRow(navController: NavHostController, kind: String) {
    Column {
        Row(verticalAlignment = Alignment.CenterVertically) {
            ImageButton(
                imageRes = getImageResource(kind),
                contentDescription = kind,
                onClick = { navController.navigate("detail/$kind") }
            )
            Column(modifier = Modifier.padding(start = 8.dp)) {
                Text("Name", fontWeight = FontWeight.Bold)
                Text("Made of")
            }
        }
    }
}

fun getImageResource(kind: String): Int {
    return when (kind) {
        "meat" -> R.drawable.meat
        "vegetable" -> R.drawable.vegetable
        "cuisine" -> R.drawable.cuisine
        "noodle" -> R.drawable.noodle
        "soup" -> R.drawable.soup
        "signature" -> R.drawable.spicy
        "spicy" -> R.drawable.spicy
        else -> R.drawable.meat
    }
}

