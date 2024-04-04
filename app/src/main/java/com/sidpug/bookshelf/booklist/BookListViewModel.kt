package com.sidpug.bookshelf.booklist

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.sidpug.bookshelf.database.AppDatabase
import com.sidpug.bookshelf.database.entity.Book
import com.sidpug.bookshelf.network.ApiUtils
import com.sidpug.bookshelf.utility.showLog
import kotlinx.coroutines.CoroutineExceptionHandler
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class BookListViewModel(private val application: Application) : AndroidViewModel(application) {
    private val _bookList = MutableLiveData<List<Book>>()
    val bookList: LiveData<List<Book>> = _bookList
    private val appDb = AppDatabase.getInstance(application)

    suspend fun getBookList() {
        val exceptionHandler = CoroutineExceptionHandler { _, throwable ->

            throwable.showLog()
        }
        viewModelScope.launch(Dispatchers.IO + exceptionHandler) {
            ApiUtils.book().getBookList().onEach { book ->
                AppDatabase.getInstance(application.applicationContext).bookDao().insertAll(book)
            }
            val data = fetchBookListFromDB()
            _bookList.postValue(data)
        }
    }

    private suspend fun fetchBookListFromDB() =
        appDb.bookDao().getAll()
}