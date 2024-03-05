package com.sidpug.bookshelf.model

data class BookItems(
    val id: String? = null,
    val icon: String? = null,
    val title: String? = null,
    val rating: Double? = null,
    val year: Long? = null,
    val rightIcon: Int? = null,
    var isFavorite: Boolean = false
)
