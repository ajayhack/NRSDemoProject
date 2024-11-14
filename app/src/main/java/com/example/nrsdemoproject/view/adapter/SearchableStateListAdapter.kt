package com.example.nrsdemoproject.view.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.annotation.Nullable
import androidx.appcompat.widget.AppCompatButton
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.example.nrsdemoproject.R
import com.example.nrsdemoproject.modal.NRSDataModal

class SearchableStateListAdapter(private var searchableStateList : MutableList<NRSDataModal>?,
                                 private var onSearchableStateItemClickEvent: (Int , Boolean) -> Unit) :
    RecyclerView.Adapter<SearchableStateListAdapter.SearchableStateViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): SearchableStateViewHolder {
        return SearchableStateViewHolder(
            LayoutInflater.from(parent.context).inflate(R.layout.list_item, parent, false))
    }

    override fun onBindViewHolder(holder: SearchableStateViewHolder, position: Int) {
        holder.bindData(position)
    }

    override fun getItemCount(): Int = searchableStateList?.size ?: 0

    fun updateDataList(searchList : MutableList<NRSDataModal>? = null){
        this.searchableStateList = searchList
        notifyDataSetChanged()
    }

    inner class SearchableStateViewHolder(itemView : View) : RecyclerView.ViewHolder(itemView){
        val stateNameButton = itemView.findViewById<AppCompatButton>(R.id.stateNameButton)

        fun bindData(position: Int){
            val model = searchableStateList?.get(position)
            stateNameButton.text = model?.state
        }

        init {
            stateNameButton.setOnClickListener { onSearchableStateItemClickEvent(adapterPosition , false) }
        }
    }
}