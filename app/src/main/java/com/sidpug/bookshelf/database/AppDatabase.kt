package com.sidpug.bookshelf.database

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import com.sidpug.bookshelf.database.dao.BookDao
import com.sidpug.bookshelf.database.dao.TagDao
import com.sidpug.bookshelf.database.dao.UserDao
import com.sidpug.bookshelf.database.entity.Book
import com.sidpug.bookshelf.database.entity.TagEntity
import com.sidpug.bookshelf.database.entity.User

@Database(
    entities = [User::class, Book::class, TagEntity::class], version = 1,
    exportSchema = false
)
abstract class AppDatabase : RoomDatabase() {
    abstract fun userDao(): UserDao
    abstract fun bookDao(): BookDao
    abstract fun tagDao(): TagDao

    companion object {

        private var instance: AppDatabase? = null
        fun getInstance(context: Context): AppDatabase {
            if (instance == null) {
                synchronized(this) {
                    instance = Room.databaseBuilder(
                        context, AppDatabase::class.java, "bookshelf.db"
                    ).allowMainThreadQueries().build()
                }
            }
            return instance as AppDatabase
        }
    }
}