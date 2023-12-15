package com.example.a683project.fragments

import android.annotation.SuppressLint
import android.util.Log
import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Scaffold
import androidx.compose.material.Text
import androidx.compose.material.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalLifecycleOwner
import androidx.compose.ui.text.font.FontStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.LifecycleEventObserver
import androidx.navigation.NavHostController
import androidx.navigation.compose.currentBackStackEntryAsState
import coil.compose.rememberImagePainter
import coil.request.CachePolicy
import com.example.a683project.viewmodel.FavoriteViewModel
import com.example.a683project.R
import com.google.firebase.storage.FirebaseStorage

@SuppressLint("UnusedMaterialScaffoldPaddingParameter", "RememberReturnType")
@Composable
fun FavoriteScreen(viewModel: FavoriteViewModel = viewModel(), navController: NavHostController) {
    val storage = FirebaseStorage.getInstance()
    val favoriteList by viewModel.favoriteList.observeAsState(initial = emptyList())
    val navBackStackEntry by navController.currentBackStackEntryAsState()
    val currentRoute = navBackStackEntry?.destination?.route
    val lifecycleOwner = LocalLifecycleOwner.current
    val lifecycleEventObserver = remember {
        LifecycleEventObserver { _, event ->
            if (event == Lifecycle.Event.ON_RESUME && navController.currentDestination?.route == "favorite") {
                viewModel.refreshFavorites()
            }
        }
    }

    DisposableEffect(lifecycleOwner) {
        lifecycleOwner.lifecycle.addObserver(lifecycleEventObserver)
        onDispose {
            lifecycleOwner.lifecycle.removeObserver(lifecycleEventObserver)
        }
    }
    LaunchedEffect(currentRoute) {
        if (currentRoute == "personal") {
            viewModel.refreshFavorites()
        }
    }
    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Favorite", fontWeight = FontWeight.Normal) }
            )
        }
    ) {
        Column {
            if (favoriteList.isEmpty()) {
                EmptyFavoritesMessage()
            } else {
                LazyColumn (
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(top = 8.dp)
                ){
                    items(favoriteList) { favoriteItem ->
                        FavoriteClickableImageItem(
                            navController = navController,
                            kind = favoriteItem.kind,
                            imageUrl = favoriteItem.imageUrl ?: "",
                            isFavorited = favoriteItem.isFavorited,
                            storage = storage
                        )
                    }
                }
            }
        }
    }
}

@Composable
fun EmptyFavoritesMessage() {
    Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
        Text("No recipe has been favorited yet", fontStyle = FontStyle.Italic)
    }
}

@Composable
fun FavoriteClickableImageItem(
    navController: NavHostController,
    kind: String,
    imageUrl: String,
    isFavorited: Boolean,
    storage: FirebaseStorage
) {
    Log.d("FavoriteClickableImageItem", "imageUrl: $imageUrl")
    val actualKind = if (kind == "see all") determineKindFromImageUrl(imageUrl) else kind
    val imageName = imageUrl.substringAfterLast("%2F").substringBefore('?')
    var ingredients by remember { mutableStateOf("") }
    val title = imageName.substringBefore(".png")
    val painter = rememberImagePainter(
        data = imageUrl,
        builder = {
            crossfade(true)
            placeholder(R.drawable.chef)
            error(R.drawable.chef)
            memoryCachePolicy(CachePolicy.ENABLED)
            diskCachePolicy(CachePolicy.ENABLED)
        }
    )

    LaunchedEffect(imageUrl) {
        val ingredientsName = imageName.substringBefore(".png") + ".txt"
        ingredients = fetchIngredients(storage, actualKind, ingredientsName)
    }

    Row(verticalAlignment = Alignment.CenterVertically,
        modifier = Modifier
            .padding(horizontal = 20.dp)
            .padding(top = 8.dp)
            .clickable { navController.navigate("detail/$actualKind/$imageName") }
    ) {
        Image(
            painter = painter,
            contentDescription = "Image from folder",
            modifier = Modifier.size(100.dp)
        )

        Column(modifier = Modifier.padding(start = 8.dp)) {
            LimitedText(
                text = CapitalizeWords(title),
                style = MaterialTheme.typography.h6.copy(
                    fontWeight = FontWeight.Bold,
                    fontSize = MaterialTheme.typography.h5.fontSize
                ),
                maxChars = 1
            )
            if (ingredients.isNotEmpty()) {
                LimitedText(
                    text = "Ingredients: $ingredients",
                    style = MaterialTheme.typography.body2.copy(color = Color.Gray),
                    maxChars = 1
                )
            }
        }
    }
}



