package com.kreggscode.confuciusquotes.data

import android.content.Context
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

data class AffirmationData(
    val id: Int,
    val affirmation: String,
    val category: String
)

class AffirmationsDataLoader(private val context: Context) {
    private val gson = Gson()
    
    suspend fun loadAffirmations(): List<AffirmationData> = withContext(Dispatchers.IO) {
        try {
            val jsonString = context.assets.open("confucius_affirmations.json")
                .bufferedReader()
                .use { it.readText() }
            
            val type = object : TypeToken<List<AffirmationData>>() {}.type
            gson.fromJson<List<AffirmationData>>(jsonString, type) ?: emptyList()
        } catch (e: Exception) {
            android.util.Log.e("AffirmationsDataLoader", "Error loading affirmations", e)
            emptyList()
        }
    }
}
