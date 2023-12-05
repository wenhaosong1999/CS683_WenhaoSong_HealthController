package com.example.a683project

import android.annotation.SuppressLint
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
import androidx.compose.ui.text.intl.Locale
import androidx.compose.ui.text.style.TextOverflow
import coil.request.CachePolicy
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import kotlin.math.min


@SuppressLint("UnusedMaterialScaffoldPaddingParameter")
@Composable
fun CurrentListFragment(navController: NavHostController, kind: String) {
    val storage = FirebaseStorage.getInstance()
    val imageList = remember { mutableStateListOf<String>() }

    LaunchedEffect(kind) {
        if (kind == "see all") {
            imageList.addAll(fetchImageUrlsFromFolder("meat"))
            imageList.addAll(fetchImageUrlsFromFolder("vegetable"))
            imageList.addAll(fetchImageUrlsFromFolder("soup"))
            imageList.addAll(fetchImageUrlsFromFolder("stir-fry"))
            imageList.addAll(fetchImageUrlsFromFolder("noodle"))
        } else {
            imageList.addAll(fetchImageUrlsFromFolder(kind))
        }
    }

    // 使用Scaffold代替Box、TopAppBar和AppBar
    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text(kind.capitalize(Locale.current), fontWeight = FontWeight.Normal)}
            )
        }
    ) {
        Column {
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

@Composable
fun ClickableImageItem(navController: NavHostController, kind: String, imageUrl: String, storage: FirebaseStorage) {
    val actualKind = if (kind == "see all") determineKindFromImageUrl(imageUrl) else kind
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
