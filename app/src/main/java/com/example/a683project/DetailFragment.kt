package com.example.a683project

import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.util.Log
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.wrapContentSize
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material.CircularProgressIndicator
import androidx.compose.material.Icon
import androidx.compose.material.IconButton
import androidx.compose.material.Scaffold
import androidx.compose.material.Text
import androidx.compose.material.TopAppBar
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Favorite
import androidx.compose.material.icons.filled.FavoriteBorder
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.ImageBitmap
import androidx.compose.ui.graphics.asImageBitmap
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.capitalize
import androidx.compose.ui.text.intl.Locale
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavHostController
import coil.compose.rememberImagePainter
import com.google.firebase.ktx.Firebase
import com.google.firebase.storage.ktx.storage
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.tasks.await
import kotlinx.coroutines.withContext
import java.io.IOException
import java.net.URL

@Composable
fun DetailFragment(navController: NavHostController, kind: String) {
    val viewModel: FavoriteViewModel = viewModel()
    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text(kind.capitalize(Locale.current)) },
                actions = {
                    IconButton(onClick = { viewModel.toggleFavorite(kind) }) {
                        Icon(
                            imageVector = if (kind in viewModel.favoriteList) Icons.Filled.Favorite else Icons.Filled.FavoriteBorder,
                            contentDescription = "Favorite"
                        )
                    }
                }
            )
        }
    ) { paddingValues ->
        DetailContent(kind, paddingValues)
    }
}

@Composable
fun DetailContent(kind: String, paddingValues: PaddingValues) {
    val imageUrl = "https://firebasestorage.googleapis.com/v0/b/foodlist12.appspot.com/o/images%2Fshuizhuroupian.png?alt=media&token=2623da8a-ab55-4a3e-acb1-6241e6e1daea"
    val textUrl = "https://firebasestorage.googleapis.com/v0/b/foodlist12.appspot.com/o/descriptions%2Fshuizhuroupian.txt?alt=media&token=5ca19fa3-1e95-4764-a46e-e62d4e668189"
    var textContent by remember { mutableStateOf("Loading description...") }
    val scope = rememberCoroutineScope()

    LaunchedEffect(kind) {
        scope.launch {
            textContent = try {
                URL(textUrl).readText()
            } catch (e: Exception) {
                "Failed to load description."
            }
        }
    }
    LazyColumn(
        modifier = Modifier
            .fillMaxWidth()
            .padding(paddingValues)
    ) {
        item {
            val painter = rememberImagePainter(data = imageUrl)
            Image(
                painter = painter,
                contentDescription = "Top Image",
                modifier = Modifier
                    .fillMaxWidth()
                    .height(200.dp)
            )
            Spacer(modifier = Modifier.height(16.dp))
            Text(
                text = textContent,
                textAlign = TextAlign.Center,
                modifier = Modifier
                    .padding(16.dp)
                    .fillMaxWidth()
            )
        }
    }
}






