package com.sidpug.bookshelf.booklist

import android.content.Context
import com.sidpug.bookshelf.database.AppDatabase
import com.sidpug.bookshelf.database.entity.BookWithBookmark
import com.sidpug.bookshelf.database.entity.BookmarkEntity
import com.sidpug.bookshelf.network.ApiUtils
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

class BookShelfRepo(private val context: Context) {
    private val appDB = AppDatabase.getInstance(context)
    suspend fun getBookList(searchText: String? = null): List<BookWithBookmark> =
        withContext(Dispatchers.IO) {
            appDB.bookDao().getBookListDataWithMarked()
        }

    suspend fun getBookListFromNetwork(searchText: String? = null): List<BookWithBookmark> =
        withContext(Dispatchers.IO) {
            ApiUtils.book().getBookList().onEach { book ->
                appDB.bookDao().insertAll(book)
            }
            appDB.bookDao().getBookListDataWithMarked()
        }


    suspend fun getBookDetails(bookId: String): BookWithBookmark? = withContext(Dispatchers.IO) {
        appDB.bookDao().getBookDataWithMarked(bookId)
    }

    suspend fun updateBookmark(bookId: String, isMarked: Boolean): Boolean =
        withContext(Dispatchers.IO) {
            appDB.bookmarkDao().insert(
                BookmarkEntity(
                    bookId,
                    isMarked
                )
            )
            return@withContext true
        }
}