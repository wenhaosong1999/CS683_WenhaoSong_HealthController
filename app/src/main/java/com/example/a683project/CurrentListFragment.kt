package com.example.a683project

import android.util.Log
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
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
import androidx.compose.material.MaterialTheme
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
import java.net.URL

@Composable
fun CurrentListFragment(navController: NavHostController, kind: String) {
    val storage = FirebaseStorage.getInstance()
    Column {
        Box(
            modifier = Modifier
                .background(MaterialTheme.colors.primary)
                .fillMaxWidth()
                .height(160.dp),
            contentAlignment = Alignment.CenterStart
        ) {
            Column(modifier = Modifier.fillMaxHeight()) {
                TopBar()
                AppBar(navController, storage)
            }
        }
        if (kind == "see all") {
            val kinds = listOf("meat", "vegetable", "soup", "stir-fry", "noodle")
            LazyColumn {
                items(kinds) { itemKind ->
                    ItemRow(navController, itemKind, storage)
                }
            }
        } else {
            LazyColumn {
                item {
                    ItemRow(navController, kind, storage)
                }
            }
        }
    }
}

@Composable
fun ItemRow(navController: NavHostController, kind: String, storage: FirebaseStorage) {
    val imageUrls = produceState(initialValue = listOf<String>(), kind) {
        value = fetchImageUrlsFromFolder(kind)
    }

    Column {
        imageUrls.value.forEach { imageUrl ->
            val imageName = imageUrl.substringAfterLast("%2F").substringBefore('?')
            Row(
                verticalAlignment = Alignment.CenterVertically,
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(8.dp)
            ) {
                OnlineImageButton(imageUrl = imageUrl, contentDescription = "Image from $kind folder") {
                    navController.navigate("detail/$kind/$imageName")
                }
                Column(modifier = Modifier.padding(start = 8.dp)) {
                    Text(imageName, style = MaterialTheme.typography.h6)
                    val ingredientsText = produceState(initialValue = "", imageName) {
                        value = fetchIngredients(storage, "ingredients/$kind/$imageName")
                    }
                    Text(ingredientsText.value)
                }
            }
        }
    }
}




@Composable
fun OnlineImageButton(imageUrl: String, contentDescription: String, onClick: () -> Unit) {
    Button(
        onClick = onClick,
        modifier = Modifier
            .padding(8.dp) // 添加外部填充
            .height(100.dp), // 设置固定高度
        colors = ButtonDefaults.buttonColors(backgroundColor = Color.Transparent)
    ) {
        Image(
            painter = rememberImagePainter(data = imageUrl),
            contentDescription = contentDescription,
            modifier = Modifier
                .fillMaxWidth() // 填充按钮的宽度
                .height(80.dp) // 设置图片的高度，小于按钮高度以留出空间
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

suspend fun fetchIngredients(storage: FirebaseStorage, path: String): String {
    val storageReference = storage.reference.child(path)
    return try {
        val url = storageReference.downloadUrl.await().toString()
        URL(url).readText()
    } catch (e: Exception) {
        ""
    }
}




