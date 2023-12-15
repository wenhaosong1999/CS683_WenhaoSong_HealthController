package com.example.a683project.viewmodel

import android.app.Application
import android.content.Context
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.google.common.reflect.TypeToken
import com.google.gson.Gson

data class FavoriteItem(
    val kind: String,
    val name: String,
    val imageUrl: String?,
    val textContent: String,
    val isFavorited: Boolean
)


class FavoriteViewModel(application: Application) : AndroidViewModel(application) {
    private val sharedPreferences = application.getSharedPreferences("favorites_pref", Context.MODE_PRIVATE)
    private val gson = Gson()

    private val _favoriteList = MutableLiveData(loadFavorites())
    val favoriteList: LiveData<List<FavoriteItem>> = _favoriteList

    private fun loadFavorites(): List<FavoriteItem> {
        val favoritesJson = sharedPreferences.getString("favorites", "[]")
        val itemType = object : TypeToken<List<FavoriteItem>>() {}.type
        return gson.fromJson(favoritesJson, itemType) ?: emptyList()
    }

    private fun saveFavorites(favorites: List<FavoriteItem>) {
        val editor = sharedPreferences.edit()
        val favoritesJson = gson.toJson(favorites)
        editor.putString("favorites", favoritesJson)
        editor.apply()
    }

    fun toggleFavorite(item: FavoriteItem) {
        val currentFavorites = _favoriteList.value?.toMutableList() ?: mutableListOf()
        val existingItem = currentFavorites.firstOrNull { it.kind == item.kind && it.name == item.name }
        if (existingItem != null) {
            currentFavorites.remove(existingItem)
        } else {
            currentFavorites.add(item)
        }
        saveFavorites(currentFavorites)
        _favoriteList.value = currentFavorites
    }

    fun refreshFavorites() {
        _favoriteList.value = loadFavorites()
    }
}






