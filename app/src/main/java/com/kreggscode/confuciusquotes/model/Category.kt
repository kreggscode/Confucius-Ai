package com.kreggscode.confuciusquotes.model

data class Category(
    val name: String,
    val quoteCount: Int,
    val icon: String = "??"
)
