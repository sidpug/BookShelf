package com.sidpug.bookshelf.adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.sidpug.bookshelf.database.entity.TagEntity
import com.sidpug.bookshelf.databinding.TagItemBinding

class TagListAdapter(private val context: Context) :
    RecyclerView.Adapter<TagListAdapter.MyViewHolder>() {

    private var data: List<TagEntity> = listOf()

    fun setData(data: List<TagEntity>) {
        this.data = data
        notifyDataSetChanged()
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyViewHolder {
        val binding = TagItemBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return MyViewHolder(binding)
    }

    override fun onBindViewHolder(holder: MyViewHolder, position: Int) {
        val item = data[position]
        holder.bind(item)
    }


    override fun getItemCount(): Int {
        return data.size
    }

    inner class MyViewHolder(private val binding: TagItemBinding) : RecyclerView.ViewHolder(binding.root) {
        fun bind(tagEntity: TagEntity) {
            binding.tagTitle.text = tagEntity.tag
        }
    }
}
