package com.example.a683project

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.material.Button
import androidx.compose.material.ButtonDefaults
import androidx.compose.material.CircularProgressIndicator
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.produceState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.capitalize
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.intl.Locale
import androidx.compose.ui.unit.dp
import androidx.navigation.NavHostController
import coil.compose.rememberImagePainter
import com.google.firebase.storage.FirebaseStorage
import kotlinx.coroutines.tasks.await

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
                AppBar(navController)
            }
        }
        if (kind == "see all") {
            val kinds = listOf("meat", "vegetable", "soup", "stir-fry", "noodle")
            LazyColumn {
                items(kinds) { itemKind ->
                    ItemRow(navController, itemKind)
                }
            }
        } else {
            LazyColumn {
                item {
                    ItemRow(navController, kind)
                }
            }
        }
    }
}

@Composable
fun ItemRow(navController: NavHostController, kind: String) {
    val imageUrls = produceState(initialValue = listOf<String>(), kind) {
        value = fetchImageUrlsFromFolder(kind)
    }
    Column {
        Row(verticalAlignment = Alignment.CenterVertically) {
            Column(modifier = Modifier.padding(start = 8.dp)) {
                Text(kind.capitalize(Locale.current), fontWeight = FontWeight.Bold)
                Text("Made of")
            }
        }
        if (imageUrls.value.isEmpty()) {
            CircularProgressIndicator(modifier = Modifier.padding(16.dp))
        } else {
            LazyRow{
                items(imageUrls.value) { imageUrl ->
                    OnlineImageButton(imageUrl = imageUrl, contentDescription = "Image from $kind folder") {
                        navController.navigate("detail/meat")
                    }
                }
            }
        }
    }
}

@Composable
fun OnlineImageButton(imageUrl: String, contentDescription: String, onClick: () -> Unit) {
    Button(
        onClick = onClick,
        colors = ButtonDefaults.buttonColors(backgroundColor = Color.Transparent)
    ) {
        Image(
            painter = rememberImagePainter(imageUrl),
            contentDescription = contentDescription,
            modifier = Modifier
                .size(100.dp)
                .padding(8.dp)
        )
    }
}


suspend fun fetchImageUrlsFromFolder(kind: String): List<String> {
    val storageReference = FirebaseStorage.getInstance().reference.child("images/$kind")
    return try {
        val result = storageReference.listAll().await()
        result.items.map { it.downloadUrl.await().toString() }
    } catch (e: Exception) {
        emptyList()
    }
}




