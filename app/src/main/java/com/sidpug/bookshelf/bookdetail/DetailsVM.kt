package com.sidpug.bookshelf.bookdetail

import android.app.Application
import android.os.Bundle
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.sidpug.bookshelf.database.AppDatabase
import com.sidpug.bookshelf.database.entity.BookWithBookmark
import com.sidpug.bookshelf.database.entity.BookmarkEntity
import com.sidpug.bookshelf.database.entity.TagEntity
import com.sidpug.bookshelf.login.BookShelfAppUser
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import java.util.Date
import java.util.UUID

class DetailsVM(appInstance: Application) : AndroidViewModel(appInstance) {
    private val _bookData: MutableLiveData<Pair<BookWithBookmark?, Boolean>> by lazy { MutableLiveData<Pair<BookWithBookmark?, Boolean>>() }
    val bookData = _bookData

    private val _tagStored: MutableLiveData<Boolean> by lazy { MutableLiveData<Boolean>() }
    val tagStored = _tagStored
    private val _tagsStored: MutableLiveData<List<TagEntity>> by lazy { MutableLiveData<List<TagEntity>>() }
    val tagsStored = _tagsStored

    private val _bookMarkUpdate: MutableLiveData<Boolean> by lazy { MutableLiveData<Boolean>() }
    val bookMarkUpdate = _bookMarkUpdate

    private val appDB = AppDatabase.getInstance(appInstance)

    private var bookId: String = ""
    fun handleExtras(bundle: Bundle) {
        bookId = bundle.getString("id") ?: ""
    }

    var bookMarkState = false

    fun getBookData() {
        viewModelScope.launch(Dispatchers.IO) {
            appDB.bookDao().getBookDataWithMarked(bookId)?.let {
                bookMarkState = it.isMarked ?: false
                _bookData.postValue(Pair(it, true))
            } ?: kotlin.run {
                _bookData.postValue(Pair(null, false))
            }
        }
    }

    fun storeTag(text: String) {
        viewModelScope.launch(Dispatchers.IO) {
            appDB.tagDao().insertAll(
                TagEntity(
                    UUID.randomUUID().toString(),
                    bookId = bookId,
                    userId = BookShelfAppUser.userId ?: "",
                    tag = text,
                    createdAt = Date().time
                )
            )
            _tagStored.postValue(true)
        }
    }

    fun getTagData() {
        viewModelScope.launch(Dispatchers.IO) {
            _tagsStored.postValue(appDB.tagDao().getTagsByBook(bookId))
        }
    }

    fun setBookMark() {
        viewModelScope.launch(Dispatchers.IO) {
            bookMarkState = !bookMarkState
            appDB.bookmarkDao().insert(BookmarkEntity(bookId, bookMarkState))
            _bookMarkUpdate.postValue(bookMarkState)
        }
    }
}