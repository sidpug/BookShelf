package com.sidpug.bookshelf.database.dao

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.sidpug.bookshelf.database.entity.Book

@Dao
interface BookDao {
    @Query("SELECT * FROM book")
    suspend fun getAll(): List<Book>

    @Query("SELECT * FROM book where id = :id")
    suspend fun getBook(id: String): Book?

    @Insert(onConflict = OnConflictStrategy.IGNORE)
    suspend fun insertAll(vararg books: Book)

    @Delete
    suspend fun delete(book: Book)
}