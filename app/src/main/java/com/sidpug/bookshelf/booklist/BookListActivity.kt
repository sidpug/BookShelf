package com.sidpug.bookshelf.booklist

import android.os.Bundle
import android.widget.SearchView
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.viewModelScope
import com.sidpug.bookshelf.adapter.BookListAdapter
import com.sidpug.bookshelf.adapter.init
import com.sidpug.bookshelf.bookdetail.BookDetailActivity
import com.sidpug.bookshelf.database.AppDatabase
import com.sidpug.bookshelf.databinding.ActivityBookListBinding
import com.sidpug.bookshelf.login.LoginActivity
import com.sidpug.bookshelf.model.BookItems
import com.sidpug.bookshelf.utility.Preferences
import com.sidpug.bookshelf.utility.getModelView
import com.sidpug.bookshelf.utility.launchActivity
import com.sidpug.bookshelf.utility.setBoolean
import com.sidpug.bookshelf.utility.showLog
import kotlinx.coroutines.launch

class BookListActivity : AppCompatActivity(), SearchView.OnQueryTextListener {
    private val TAG = "BookListActivity"
    private lateinit var binding: ActivityBookListBinding
    private lateinit var bookListViewModel: BookListViewModel
    private lateinit var listAdapter: BookListAdapter
    private var data: MutableList<BookItems> = mutableListOf()

    private lateinit var dbInstance: AppDatabase
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityBookListBinding.inflate(layoutInflater)
        bookListViewModel = getModelView(BookListViewModel(application)) as BookListViewModel
        dbInstance = AppDatabase.getInstance(this)
        lifecycleScope.launch {
            setupForBookList()
        }
        enableEdgeToEdge()
        setContentView(binding.root)
        setupRecyclerView()
        initObservers()
        searchHandler()
        setupLogout()
    }

    private fun initObservers() {
        bookListViewModel.bookList.observe(this) { bookList ->
            data.clear()
            listAdapter.notifyDataSetChanged()
            bookList.forEach { book ->
                data.add(
                    BookItems(
                        book.book.id,
                        book.book.image,
                        book.book.title,
                        book.book.score,
                        book.book.publishedChapterDate,
                        isFavorite = book.isMarked ?: false
                    )
                )
            }
            listAdapter.notifyItemRangeInserted(0, data.size)
        }
    }

    private fun setupLogout() {
        binding.ivLogout.setOnClickListener {
            Preferences.instance.setBoolean("isLogged", false)
            launchActivity<LoginActivity> {
                finish()
            }
        }
    }

    private suspend fun setupForBookList() {
        bookListViewModel.getBookList()
    }

    private fun searchHandler() {
        binding.searchView.setOnQueryTextListener(this)
    }

    private fun setupRecyclerView() {
        // This will pass the ArrayList to our Adapter

        listAdapter = BookListAdapter(this, data) { item, action, position ->
            when(action) {
                ListItemActions.FavToggle -> {
                    lifecycleScope.launch {
                        item.id?.let { bookListViewModel.updateBookItem(it, !item.isFavorite) }
                        item.id?.let { updateBookItem(it, position) }
                    }
                }
                ListItemActions.None -> {
                    launchActivity<BookDetailActivity> {
                    putExtra("id", item.id)
                    }
                }
            }

        }
        // Setting the Adapter with the recyclerview
        binding.recyclerview.apply {
            init(this@BookListActivity, listAdapter, true)
        }
    }

    fun updateRecyclerView(book: BookItems) {
        data.add(book)
        listAdapter.notifyItemInserted(data.lastIndex)

    }

    private fun updateBookItem(bookId: String, position: Int) {
        bookListViewModel.viewModelScope.launch {
            bookListViewModel.getBookItem(bookId).also {
                bookListViewModel.book.removeObservers(this@BookListActivity)
                bookListViewModel.book.observe(this@BookListActivity) { book ->
                    data[position] = BookItems(
                        book.book.id,
                        book.book.image,
                        book.book.title,
                        book.book.score,
                        book.book.publishedChapterDate,
                        isFavorite = book.isMarked ?: false
                    )
                    listAdapter.notifyItemChanged(position)
                    bookListViewModel.book.removeObservers(this@BookListActivity)
                }
            }
        }
    }

    override fun onQueryTextSubmit(query: String?): Boolean {
        return true
    }

    override fun onQueryTextChange(query: String?): Boolean {
        showLog(TAG, query)
        if (query != null) {
            listAdapter.filter.filter(query)
            return false
        }
        return false
    }

    override fun onResume() {
        super.onResume()
        lifecycleScope.launch {
            setupForBookList()
        }
    }
}