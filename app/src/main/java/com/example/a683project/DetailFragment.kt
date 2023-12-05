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
import com.google.firebase.storage.FirebaseStorage
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.ui.text.font.FontWeight

@Composable
fun DetailFragment(navController: NavHostController, kind: String, name: String) {
    val viewModel: FavoriteViewModel = viewModel()
    var imageUrl by remember { mutableStateOf<String?>(null) }
    var textContent by remember { mutableStateOf("Loading description...") }
    val favoriteList by viewModel.favoriteList.observeAsState(initial = emptyList())
    val isFavorited = favoriteList.any { it.kind == kind && it.name == name }

    LaunchedEffect(kind, name) {
        try {
            imageUrl = fetchImageUrl(kind, name) ?: "Failed to load image."
        } catch (e: Exception) {
            imageUrl = "Failed to load image."
            Log.e("DetailFragment", "Error fetching image URL", e)
        }
    }

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text(name.substringBeforeLast(".png").capitalize(Locale.current), fontWeight = FontWeight.Normal) },
                actions = {
                    IconButton(onClick = {
                        val item = FavoriteItem(kind, name, imageUrl, textContent, !isFavorited)
                        viewModel.toggleFavorite(item)
                    }) {
                        Icon(
                            imageVector = if (isFavorited) Icons.Filled.Favorite else Icons.Filled.FavoriteBorder,
                            contentDescription = "favorite"
                        )
                    }
                }
            )
        }
    ) { paddingValues ->
        DetailContent(kind, name, paddingValues, imageUrl, textContent) {
            imageUrl = it.first
            textContent = it.second
        }
    }
}


@Composable
fun DetailContent(
    kind: String,
    name: String,
    paddingValues: PaddingValues,
    initialImageUrl: String?,
    initialTextContent: String,
    updateData: (Pair<String?, String>) -> Unit
) {
    var imageUrl by remember { mutableStateOf(initialImageUrl) }
    var textContent by remember { mutableStateOf(initialTextContent) }
    val scope = rememberCoroutineScope()

    LaunchedEffect(kind, name) {
        scope.launch {
            val newImageUrl = fetchImageUrl(kind, name)
            imageUrl = newImageUrl

            val textFileName = name.substringBeforeLast(".png") + ".txt"
            Log.d("ItemRow", "URL: $textFileName")
            try {
                val textRef = FirebaseStorage.getInstance().reference.child("descriptions/$kind/$textFileName")
                val url = textRef.downloadUrl.await().toString()
                textContent = withContext(Dispatchers.IO) {
                    URL(url).readText()
                }
            } catch (e: Exception) {
                textContent = "Failed to load description."
                Log.e("DetailContent", "Error loading text content", e)
            }
        }
    }

    LazyColumn(
        modifier = Modifier
            .fillMaxWidth()
            .padding(paddingValues)
    ) {
        item {
            Spacer(modifier = Modifier.height(16.dp))

            imageUrl?.let { url ->
                val painter = rememberImagePainter(data = url)
                Image(
                    painter = painter,
                    contentDescription = "Top Image",
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(200.dp)
                )
            }

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


suspend fun fetchImageUrl(kind: String, name: String): String? {
    val storageReference = FirebaseStorage.getInstance().reference.child("images/$kind/$name")
    return try {
        storageReference.downloadUrl.await().toString()
    } catch (e: Exception) {
        Log.e("fetchImageUrl", "Error fetching image URL", e)
        null
    }
}






