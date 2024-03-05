package com.sidpug.bookshelf.database.entity

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity
data class Book(
    @PrimaryKey val id: String,
    @ColumnInfo(name = "image") val image: String,
    @ColumnInfo(name = "score") val score: Double,
    @ColumnInfo(name = "popularity") val popularity: Int,
    @ColumnInfo(name = "title") val title: String,
    @ColumnInfo(name = "publishedChapterDate") var publishedChapterDate: Long
)