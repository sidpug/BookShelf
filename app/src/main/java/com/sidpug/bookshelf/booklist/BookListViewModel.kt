package com.sidpug.bookshelf.booklist

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.sidpug.bookshelf.database.AppDatabase
import com.sidpug.bookshelf.database.entity.BookWithBookmark
import com.sidpug.bookshelf.utility.showLog
import kotlinx.coroutines.CoroutineExceptionHandler
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

class BookListViewModel(private val application: Application) : AndroidViewModel(application) {
    private val _bookList = MutableLiveData<List<BookWithBookmark>>()
    val bookList: LiveData<List<BookWithBookmark>> = _bookList
    private val _book = MutableLiveData<BookWithBookmark>()
    val book: LiveData<BookWithBookmark> = _book
    private val appDb = AppDatabase.getInstance(application)
    private val bookshelfRepo = BookShelfRepo(application)


    suspend fun getBookList(searchText: String? = null) {
        val exceptionHandler = CoroutineExceptionHandler { _, throwable ->

            throwable.showLog()
        }
        viewModelScope.launch(Dispatchers.IO + exceptionHandler) {
            _bookList.postValue(bookshelfRepo.getBookList(searchText))
            bookshelfRepo.getBookListFromNetwork(searchText).let {
                delay(2000)
                _bookList.postValue(it)
            }
        }
    }

    suspend fun updateBookItem(bookId: String, isFavourite: Boolean) {
        bookshelfRepo.updateBookmark(bookId = bookId, isMarked = isFavourite)
    }

    suspend fun getBookItem(bookId: String) {
        bookshelfRepo.getBookDetails(bookId = bookId).let { book ->
            book?.let {
                _book.postValue(it)
            }
        }
    }

    private suspend fun reloadBookListFromDB() =
        appDb.bookDao().getAll()
}