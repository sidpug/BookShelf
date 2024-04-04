package com.sidpug.bookshelf.database.entity

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "tagEntityTable")
data class TagEntity(
    @PrimaryKey val id: String,
    val bookId: String,
    val tag: String,
    val userId: String,
    val createdAt: Long
)