package com.sidpug.bookshelf.adapter

import android.app.Notification.Action
import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Filter
import android.widget.Filterable
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import coil.load
import com.sidpug.bookshelf.R
import com.sidpug.bookshelf.booklist.ListItemActions
import com.sidpug.bookshelf.databinding.ItemBookBinding
import com.sidpug.bookshelf.model.BookItems
import com.sidpug.bookshelf.utility.DateHelperInstance
import java.math.BigDecimal
import java.math.RoundingMode

class BookListAdapter(
    private val mContext: Context,
    private var list: MutableList<BookItems>,
    private val onItemClick: (BookItems, ListItemActions, Int) -> Unit
) : AbstractAdapter(list, false), Filterable {

    private var arraylist = ArrayList<BookItems>()

    init {
        arraylist = list as ArrayList<BookItems>
    }

    class BookHolder(binding: ItemBookBinding) : RecyclerView.ViewHolder(binding.root) {
        // Holds the TextView that will add each animal to
        val icon: ImageView = binding.ivBookItemIcon
        val rightIcon: ImageView = binding.favToggle
        val title: TextView = binding.tvBookItemTitle
        val rating: TextView = binding.tvBookItemRating
        val year: TextView = binding.tvBookPublished
    }

    override fun getViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        val binding = ItemBookBinding.inflate(LayoutInflater.from(mContext), parent, false)
        return BookHolder(binding)
    }

    override fun getItemCount(): Int {
        return arraylist.size
    }

    override fun bindHolder(holder: RecyclerView.ViewHolder, position: Int) {
        (holder as BookHolder).apply {
            val currentItem = list[position]
            title.text = currentItem.title
            val formattedRating = currentItem.rating?.getRating()
            rating.text = formattedRating
            year.text = currentItem.year?.let { DateHelperInstance.getDateTime(it) }
            if (currentItem.icon != null) {
                icon.visibility = View.VISIBLE
                icon.load(currentItem.icon)
            } else
                icon.visibility = View.GONE
            if (currentItem.isFavorite) {
                rightIcon.visibility = View.VISIBLE
                rightIcon.setImageResource(R.drawable.ic_fav_selected)
            } else
                rightIcon.setImageResource(R.drawable.ic_fav_unselected)
            rightIcon.setOnClickListener {
                onItemClick(currentItem, ListItemActions.FavToggle, position)
            }
            itemView.setOnClickListener {
                onItemClick(currentItem, ListItemActions.None, position)
            }
        }
    }

    override fun getFilter(): Filter {
        return object : Filter() {
            override fun performFiltering(constraint: CharSequence?): FilterResults {
                val charSearch = constraint.toString().lowercase()
                arraylist = if (charSearch.isEmpty()) {
                    list as ArrayList<BookItems>
                } else {
                    val resultList = ArrayList<BookItems>()
                    for (row in list) {
                        if (row.title?.lowercase()?.contains(charSearch) == true) {
                            resultList.add(row)
                        }
                    }
                    resultList
                }
                val filterResults = FilterResults()
                filterResults.values = arraylist
                return filterResults
            }

            override fun publishResults(constraint: CharSequence?, results: FilterResults?) {
                arraylist = results?.values as ArrayList<BookItems>
                notifyDataSetChanged()
            }
        }
    }


}

fun Double.getRating(): String =
    BigDecimal(this * 0.05).setScale(1, RoundingMode.HALF_EVEN).toString()
