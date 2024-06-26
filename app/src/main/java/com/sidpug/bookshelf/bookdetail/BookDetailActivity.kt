package com.sidpug.bookshelf.bookdetail

import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.core.view.isVisible
import androidx.recyclerview.widget.LinearLayoutManager
import coil.load
import com.sidpug.bookshelf.R
import com.sidpug.bookshelf.adapter.TagListAdapter
import com.sidpug.bookshelf.adapter.getRating
import com.sidpug.bookshelf.database.entity.BookWithBookmark
import com.sidpug.bookshelf.databinding.ActivityBookDetailBinding
import com.sidpug.bookshelf.databinding.DialogEditTextBinding
import com.sidpug.bookshelf.utility.DateHelperInstance
import com.sidpug.bookshelf.utility.getModelView

class BookDetailActivity : AppCompatActivity() {
    private lateinit var detailsViewModel: DetailsVM
    private lateinit var binding: ActivityBookDetailBinding

    private lateinit var tagAdapter: TagListAdapter
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityBookDetailBinding.inflate(layoutInflater)
        setContentView(binding.root)
        detailsViewModel = getModelView(DetailsVM(application)) as DetailsVM
        binding.bookDetailLayout
        binding.floatingIcon.setOnClickListener {
            showEditTextDialog(this)
        }
        intent.extras?.let { detailsViewModel.handleExtras(it) } ?: finish()
        enableEdgeToEdge()
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }
        initButtons()
        initData()
        initObserver()
    }

    private fun initButtons() {
        binding.bookDetailLayout.favToggle.setOnClickListener {
            detailsViewModel.setBookMark()
        }
    }

    private fun updateBookMarkState() {
        if (detailsViewModel.bookMarkState) {
            binding.bookDetailLayout.favToggle.setImageResource(R.drawable.ic_fav_selected)
        } else {
            binding.bookDetailLayout.favToggle.setImageResource(R.drawable.ic_fav_unselected)
        }
    }

    private fun initObserver() {
        detailsViewModel.bookData.observe(this) {
            if (it.second) {
                it.first?.let { it1 ->
                    showBookDetails(it1)
                    tagAdapter = TagListAdapter(this)
                    binding.tagList.apply {
                        adapter = tagAdapter
                        layoutManager =
                            LinearLayoutManager(context, LinearLayoutManager.VERTICAL, false)
                    }
                    detailsViewModel.getTagData()
                }
            } else {
                Toast.makeText(
                    applicationContext,
                    "Unable to load book details", Toast.LENGTH_SHORT
                ).show()
                setResult(RESULT_OK)
                finish()
            }
        }
        detailsViewModel.tagStored.observe(
            this
        ) { //Add list fragment whenever data changes.
            if (it) {
                Toast.makeText(
                    applicationContext,
                    "Tag added", Toast.LENGTH_SHORT
                ).show()
                detailsViewModel.getTagData()
                detailsViewModel.tagStored
            }
        }
        detailsViewModel.tagsStored.observe(
            this
        ) {
            if (it.isNotEmpty()) {
                if (::tagAdapter.isInitialized)
                    tagAdapter.setData(it)
            }
        }

        detailsViewModel.bookMarkUpdate.observe(
            this
        ) {
            updateBookMarkState()
        }
    }

    private fun showBookDetails(book: BookWithBookmark) {
        binding.bookDetailLayout.tvBookPublished.text =
            DateHelperInstance.getDateTime(book.book.publishedChapterDate)
        binding.bookDetailLayout.ivBookItemIcon.load(book.book.image)
        binding.bookDetailLayout.tvBookItemRating.text = book.book.score.getRating()
        binding.bookDetailLayout.tvBookItemTitle.text = book.book.title
        updateBookMarkState()
    }

    private fun initData() {
        detailsViewModel.getBookData()
    }

    private fun showEditTextDialog(context: Context) {
        val binding = DialogEditTextBinding.inflate(LayoutInflater.from(context))

        AlertDialog.Builder(context)
            .setView(binding.root)
            .setNegativeButton("Cancel", null)
            .setPositiveButton("ok") { _, _ ->
                detailsViewModel.storeTag(binding.editText.text.toString())
            }
            .create()
            .show()
    }
}