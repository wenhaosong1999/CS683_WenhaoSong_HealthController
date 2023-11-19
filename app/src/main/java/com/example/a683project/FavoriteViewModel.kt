package com.example.a683project

import androidx.compose.runtime.mutableStateListOf
import androidx.lifecycle.ViewModel

class FavoriteViewModel : ViewModel() {
    val favoriteList = mutableStateListOf<String>()

    fun toggleFavorite(item: String) {
        if (item in favoriteList) {
            favoriteList.remove(item)
        } else {
            favoriteList.add(item)
        }
    }
}



