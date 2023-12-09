package com.example.a683project

import android.annotation.SuppressLint
import android.util.Log
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Scaffold
import androidx.compose.material.Text
import androidx.compose.material.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.navigation.NavHostController
import coil.compose.rememberImagePainter
import com.google.firebase.storage.FirebaseStorage
import kotlinx.coroutines.tasks.await
import java.net.URL
import androidx.compose.runtime.*
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.capitalize
import androidx.compose.ui.text.font.FontStyle
import androidx.compose.ui.text.intl.Locale
import androidx.compose.ui.text.style.TextOverflow
import coil.request.CachePolicy
import com.google.firebase.storage.StorageReference
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import kotlin.math.min

@SuppressLint("UnusedMaterialScaffoldPaddingParameter")
@Composable
fun CurrentListFragment(navController: NavHostController, kind: String) {
    val storage = FirebaseStorage.getInstance()
    val imageList = remember { mutableStateListOf<String>() }
    val isLoading = remember { mutableStateOf(true) }
    val hasResults = remember { mutableStateOf(true) }

    LaunchedEffect(kind) {
        isLoading.value = true
        if (kind.startsWith("search:")) {
            val searchTerm = kind.removePrefix("search:")
            val results = withContext(Dispatchers.IO) { searchInIngredients(storage, searchTerm) }
            if (results.isNotEmpty()) {
                hasResults.value = true
                imageList.clear()
                imageList.addAll(results)
            } else {
                hasResults.value = false
            }
        } else if (kind == "see all") {
            imageList.addAll(fetchImageUrlsFromFolder("meat"))
            imageList.addAll(fetchImageUrlsFromFolder("vegetable"))
            imageList.addAll(fetchImageUrlsFromFolder("soup"))
            imageList.addAll(fetchImageUrlsFromFolder("stir-fry"))
            imageList.addAll(fetchImageUrlsFromFolder("noodle"))
        } else {
            imageList.addAll(fetchImageUrlsFromFolder(kind))
        }
        isLoading.value = false
    }
    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text(kind.capitalize(Locale.current), fontWeight = FontWeight.Normal) }
            )
        }
    ) {
        Box(modifier = Modifier.fillMaxSize()) {
            if (isLoading.value) {
                Text("Loading...", modifier = Modifier.align(Alignment.Center))
            } else if (!hasResults.value) {
                Text("No matching recipes", modifier = Modifier.align(Alignment.Center), fontStyle = FontStyle.Italic)
            } else {
                LazyColumn(
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(top = 8.dp)
                ) {
                    items(imageList) { imageUrl ->
                        ClickableImageItem(navController, kind, imageUrl, storage)
                    }
                }
            }
        }
    }
}

@Composable
fun ClickableImageItem(navController: NavHostController, kind: String, imageUrl: String, storage: FirebaseStorage) {
    Log.d("ClickableImageItem", "kind:$kind, imageUrl:$imageUrl")
    val actualKind = if (kind == "see all"){ determineKindFromImageUrl(imageUrl)} else if (kind.startsWith("search:")){imageUrl.substringAfterLast("images%2F").substringBefore("%2")} else kind
    Log.d("ClickableImageItem", "actualKind:$actualKind, imageUrl:$imageUrl")
    val imageName = imageUrl.substringAfterLast("%2F").substringBefore('?')
    var ingredients by remember { mutableStateOf("") }
    val title =imageName.substringBefore(".png")
    val painter = rememberImagePainter(
        data = imageUrl,
        builder = {
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
    ) {
        Image(
            painter = painter,
            contentDescription = "Image from folder",
            modifier = Modifier
                .clickable {
                    navController.navigate("detail/$actualKind/$imageName")
                }
                .size(100.dp)
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

suspend fun fetchIngredients(storage: FirebaseStorage, kind: String, imageName: String): String {
    return withContext(Dispatchers.IO) {
        val storageReference = storage.reference.child("ingredients/$kind/$imageName")
        try {
            val url = storageReference.downloadUrl.await().toString()
            URL(url).readText()
        } catch (e: Exception) {
            ""
        }
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

@Composable
fun LimitedText(text: String, style: TextStyle, maxChars: Int) {
    var displayedText by remember { mutableStateOf(text) }

    Text(
        text = displayedText,
        style = style,
        maxLines = 1,
        overflow = TextOverflow.Ellipsis,
        onTextLayout = { textLayoutResult ->
            if (textLayoutResult.didOverflowWidth) {
                val endPosition = min(maxChars, text.length)
                displayedText = text.substring(0, endPosition) + if (text.length > maxChars) "..." else ""
            }
        }
    )
}

@Composable
fun CapitalizeWords(text: String): String {
    return text.split(" ").joinToString(" ") {
        if (it.isNotEmpty()) it[0].uppercaseChar() + it.substring(1).lowercase()
        else it
    }
}

fun determineKindFromImageUrl(imageUrl: String): String {
    return when {
        "meat" in imageUrl -> "meat"
        "vegetable" in imageUrl -> "vegetable"
        "soup" in imageUrl -> "soup"
        "stir-fry" in imageUrl -> "stir-fry"
        "noodle" in imageUrl -> "noodle"
        else -> "unknown"
    }
}

suspend fun searchInIngredients(storage: FirebaseStorage, searchTerm: String): List<String> {
    val ingredientsRef = storage.reference.child("ingredients")
    Log.d("Search", "Searching in: ingredients")
    return searchInDirectory(storage, ingredientsRef, searchTerm)
}

suspend fun searchInDirectory(storage: FirebaseStorage, directoryRef: StorageReference, searchTerm: String): List<String> {
    val urls = mutableListOf<String>()
    val listResult = directoryRef.listAll().await()

    listResult.prefixes.forEach { subDir ->
        Log.d("Search", "Found subdirectory: ${subDir.path}")
    }
    listResult.items.forEach { fileRef ->
        Log.d("Search", "Found file: ${fileRef.path}")
        if (fileRef.name.endsWith(".txt")) {
            val content = downloadFileContent(fileRef) // 获取文件内容
            if (content.contains(searchTerm, ignoreCase = true)) {
                val imageName = fileRef.name.replace(".txt", ".png")
                val subDirPath = fileRef.path.substringBeforeLast('/').substringAfterLast("ingredients/")
                Log.d("Search", "Found subdirectory: $subDirPath")
                processImage(subDirPath, imageName, urls, storage)
            }
        }
    }
    for (subDir in listResult.prefixes) {
        urls.addAll(searchInDirectory(storage, subDir, searchTerm))
    }
    return urls
}

suspend fun downloadFileContent(fileRef: StorageReference): String {
    return withContext(Dispatchers.IO) {
        try {
            val bytes = fileRef.getBytes(Long.MAX_VALUE).await()
            String(bytes, Charsets.UTF_8)
        } catch (e: Exception) {
            ""
        }
    }
}

suspend fun processImage(subDirPath: String, imageName: String, urls: MutableList<String>, storage: FirebaseStorage) {
    val imageRef = storage.reference.child("images/$subDirPath/$imageName")
    try {
        val url = imageRef.downloadUrl.await().toString()
        urls.add(url)
        Log.d("Search", "Match found, image URL: $url")
    } catch (e: Exception) {
        Log.e("Search", "Error getting image URL for $imageName", e)
    }
}

