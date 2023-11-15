package com.example.a683project

import androidx.annotation.StringRes
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.padding
import androidx.compose.material.Scaffold
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.Person
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController


sealed class Screen(val route: String, @StringRes val resourceId: Int, val icon: ImageVector) {
    object Home : Screen("home", R.string.home, Icons.Filled.Home)
    object Personal : Screen("personal", R.string.personal, Icons.Filled.Person)
}

@Composable
fun ScreenNavigation() {
    val paddingValues = PaddingValues()
    val navController = rememberNavController()
    Scaffold(
        bottomBar = { BottomNavigationBar(navController) }
    ) { innerPadding ->
        NavHost(navController, startDestination = Screen.Home.route, Modifier.padding(innerPadding)) {
            composable(Screen.Home.route) { HomeScreen(navController) }
            composable(Screen.Personal.route) { PersonalScreen(navController, paddingValues) }
            composable("detail/meat") { CurrentListFragment(navController,"meat") }
            composable("detail/vegetable") { CurrentListFragment(navController, "vegetable") }
            composable("detail/cuisine") { CurrentListFragment(navController,"cuisine") }
            composable("detail/soup") { CurrentListFragment(navController, "vegetable") }
            composable("detail/noodle") { CurrentListFragment(navController,"noodle") }
            composable("detail/see all") { CurrentListFragment(navController, "see all") }
        }
    }
}