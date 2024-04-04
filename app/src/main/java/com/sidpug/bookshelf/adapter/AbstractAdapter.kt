package com.sidpug.bookshelf.adapter

import android.content.Context
import android.view.ViewGroup
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.sidpug.bookshelf.utility.showLog

abstract class AbstractAdapter(
    private val itemList: MutableList<*>, private val isDummy: Boolean = false
) : RecyclerView.Adapter<RecyclerView.ViewHolder>() {

    var isLoaderVisible = false

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        return getViewHolder(parent, viewType)
    }

    override fun getItemCount(): Int {
        showLog("RECYCLER SIZE ${itemList.size}")
        return if (isDummy) 5
        else itemList.size
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        showLog("content ${itemList[position]}")
        bindHolder(holder, position)
    }

    abstract fun getViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder
    abstract fun bindHolder(holder: RecyclerView.ViewHolder, position: Int)

}

fun RecyclerView.init(
    mContext: Context,
    mAdapter: RecyclerView.Adapter<RecyclerView.ViewHolder>,
    isDecorated: Boolean = false,
    manager: RecyclerView.LayoutManager = LinearLayoutManager(mContext)
) {
    apply {
        setHasFixedSize(false)
        layoutManager = manager
        if (isDecorated) addItemDecoration(DividerItemDecoration(mContext, RecyclerView.VERTICAL))
        adapter = mAdapter
    }
}