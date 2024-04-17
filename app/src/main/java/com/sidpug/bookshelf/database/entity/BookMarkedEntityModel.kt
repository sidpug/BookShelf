package com.sidpug.bookshelf.database.entity

import androidx.room.Embedded

data class BookWithBookmark(
    @Embedded val book: Book,
    val isMarked: Boolean?
)