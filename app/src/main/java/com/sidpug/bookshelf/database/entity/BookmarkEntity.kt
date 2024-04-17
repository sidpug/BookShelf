package com.sidpug.bookshelf.database.entity

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "bookMarkTable")
data class BookmarkEntity(
    @PrimaryKey val bookId: String,
    val isMarked: Boolean
)