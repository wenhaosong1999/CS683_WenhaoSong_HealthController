package com.example.a683project

import androidx.annotation.StringRes
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.padding
import androidx.compose.material.Scaffold
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Favorite
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.Person
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController


sealed class Screen(val route: String, @StringRes val resourceId: Int, val icon: ImageVector) {
    object Home : Screen("home", R.string.home, Icons.Filled.Home)
    object Personal : Screen("personal", R.string.personal, Icons.Filled.Person)
    object Favorite : Screen("favorite", R.string.favorite, Icons.Filled.Favorite)
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
            composable(Screen.Favorite.route) { FavoriteScreen(viewModel(), navController) }
            composable("list/meat") { CurrentListFragment(navController,"meat") }
            composable("list/vegetable") { CurrentListFragment(navController, "vegetable") }
            composable("list/stir-fry") { CurrentListFragment(navController,"stir-fry") }
            composable("list/soup") { CurrentListFragment(navController, "soup") }
            composable("list/noodle") { CurrentListFragment(navController,"noodle") }
            composable("signature") { CurrentListFragment(navController,"signature") }
            composable("spicy") { CurrentListFragment(navController,"spicy") }
            composable("list/see all") { CurrentListFragment(navController, "see all") }
            composable("detail/meat") { DetailFragment(navController, "meat") }
            composable("detail/vegetable") { DetailFragment(navController, "vegetable") }
            composable("detail/stir-fry") { DetailFragment(navController, "stir-fry") }
            composable("detail/noodle") { DetailFragment(navController, "noodle") }
            composable("detail/soup") { DetailFragment(navController, "soup") }
            composable("detail/signature") { DetailFragment(navController, "signature") }
            composable("detail/spicy") { DetailFragment(navController, "spicy") }
        }
    }
}