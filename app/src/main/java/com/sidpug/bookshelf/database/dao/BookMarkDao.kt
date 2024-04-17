package com.sidpug.bookshelf.database.dao

import androidx.lifecycle.LiveData
import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.sidpug.bookshelf.database.entity.Book
import com.sidpug.bookshelf.database.entity.BookmarkEntity


@Dao
interface BookMarkDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insert(vararg books: BookmarkEntity)
}