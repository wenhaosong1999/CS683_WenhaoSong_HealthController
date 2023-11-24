package com.example.a683project

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.Button
import androidx.compose.material.Icon
import androidx.compose.material.Scaffold
import androidx.compose.material.TextField
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.Search
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Surface
import androidx.compose.material.Text
import androidx.compose.material.TextFieldDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavHostController
import com.example.a683project.ui.theme._683projectTheme


class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            MainTheme{
                ScreenNavigation()
            }
        }
    }
}

@Composable
fun MainTheme(content: @Composable () -> Unit){
    _683projectTheme{
        Surface(color = MaterialTheme.colors.background) {
          content()
        }
    }
}

@Preview(showBackground = true)
@Composable
fun GreetingPreview() {
    MainTheme{
        ScreenNavigation()
    }
}


@Composable
fun HomeScreen(navController: NavHostController) {
    Column {
        Box(
            modifier = Modifier
                .background(MaterialTheme.colors.primary)
                .fillMaxWidth()
                .height(160.dp),
            contentAlignment = Alignment.CenterStart
        ) {
            Column(
                modifier = Modifier.fillMaxHeight()
            ) {
                Row(
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Image(
                        painter = painterResource(id = R.drawable.chef),
                        contentDescription = "logo",
                        modifier = Modifier.size(80.dp)
                    )
                    Column(
                        horizontalAlignment = Alignment.CenterHorizontally,
                        modifier = Modifier.padding(start = 16.dp)
                    ) {
                        Text(
                            text = "Health Controller",
                            color = Color.White,
                            fontSize = 24.sp,
                            fontWeight = FontWeight.Bold
                        )
                        Text(
                            text = "The first step to be a chef",
                            color = Color.White,
                            fontSize = 16.sp
                        )
                    }
                }
                AppBar(navController)
            }
        }
        Scaffold { paddingValues ->
            Content(paddingValues, navController)
        }
    }
}

@Composable
fun AppBar(navController: NavHostController) {
    var searchText by remember { mutableStateOf("") }
    var showMessage by remember { mutableStateOf(false) }
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(8.dp),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.Center
    ) {
        TextField(
            value = "",
            onValueChange = {searchText = it
                showMessage = searchText.isNotBlank() &&
                        !searchText.equals("meat", ignoreCase = true) &&
                        !searchText.equals("vegetable", ignoreCase = true)
                if (searchText.equals("meat", ignoreCase = true) || searchText.equals("vegetable", ignoreCase = true)) {
                    navController.navigate("list/$searchText")
                }
                            },
            modifier = Modifier
                .fillMaxWidth(0.9f)
                .height(56.dp),
            placeholder = { Text("Search food, Vegetable, etc...") },
            leadingIcon = {
                Icon(
                    imageVector = Icons.Rounded.Search,
                    contentDescription = "Search",
                    modifier = Modifier.size(24.dp)
                )
            },
            colors = TextFieldDefaults.textFieldColors(
                backgroundColor = Color.White,
                focusedIndicatorColor = Color.Transparent,
                unfocusedIndicatorColor = Color.Transparent
            ),
            shape = RoundedCornerShape(8.dp)
        )
        if (showMessage) {
            Text("Please enter another keyword", color = Color.Red)
        }
    }
}


@Composable
fun Content(paddingValues: PaddingValues, navController: NavHostController) {
    Column(modifier = Modifier.padding(paddingValues)) {
        Row(
            horizontalArrangement = Arrangement.SpaceEvenly,
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp)
        ) {
            Column(horizontalAlignment = Alignment.CenterHorizontally) {
                ImageButton(imageRes = R.drawable.stirfry, contentDescription = "stir-fry") {
                    navController.navigate("list/stir-fry")
                }
                Text("Stir-fry")
            }
            Column(horizontalAlignment = Alignment.CenterHorizontally) {
                ImageButton(imageRes = R.drawable.meat, contentDescription = "meat") {
                    navController.navigate("list/meat")
                }
                Text("Meat")
            }
            Column(horizontalAlignment = Alignment.CenterHorizontally) {
                ImageButton(imageRes = R.drawable.vegetable, contentDescription = "vegetable") {
                    navController.navigate("list/vegetable")
                }
                Text("Vegetable")
            }
        }

        Row(
            horizontalArrangement = Arrangement.SpaceEvenly,
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp)
        ) {
            Column(horizontalAlignment = Alignment.CenterHorizontally) {
                ImageButton(imageRes = R.drawable.noodle, contentDescription = "noodle") {
                    navController.navigate("list/noodle")
                }
                Text("Noodle")
            }
            Column(horizontalAlignment = Alignment.CenterHorizontally) {
                ImageButton(imageRes = R.drawable.soup, contentDescription = "soup") {
                    navController.navigate("list/soup")
                }
                Text("Soup")
            }
            Column(horizontalAlignment = Alignment.CenterHorizontally) {
                ImageButton(imageRes = R.drawable.seeall, contentDescription = "see all") {
                    navController.navigate("list/see all")
                }
                Text("See All")
            }
        }
        FeatureCard(
            title = "Signature Recipes",
            subtitle = "Recommendation",
            imageRes = R.drawable.stirfry,
            onClick = {navController.navigate("signature")}
        )
        FeatureCard(
            title = "Spicy Recipes",
            subtitle = "Recommendation",
            imageRes = R.drawable.stirfry,
            onClick = {navController.navigate("spicy")}
        )
    }
}
@Composable
fun FeatureCard(title: String, subtitle: String, imageRes: Int, onClick: () -> Unit) {
    Row(modifier = Modifier
        .padding(16.dp)
        .clickable(onClick = onClick)
        .background(Color.White, RoundedCornerShape(10.dp))
        .padding(16.dp)) {

        Column(modifier = Modifier.weight(1f)) {
            Text(text = title, style = MaterialTheme.typography.h6)
            Spacer(modifier = Modifier.height(4.dp))
            Text(text = subtitle, style = MaterialTheme.typography.body2)
            Spacer(modifier = Modifier.height(8.dp))
            Button(onClick = onClick) {
                Text(text = "GO>")
            }
        }
        Image(
            painter = painterResource(id = imageRes),
            contentDescription = null,
            modifier = Modifier.size(100.dp)
        )
    }
}









