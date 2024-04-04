package com.sidpug.bookshelf.database.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query
import com.sidpug.bookshelf.database.entity.TagEntity


@Dao
interface TagDao {
    @Query("SELECT * FROM tagEntityTable WHERE bookId = :bookId ORDER BY createdAt ASC")
    suspend fun getTagsByBook(bookId: String): List<TagEntity>

    @Insert
    suspend fun insertAll(vararg tag: TagEntity)
}