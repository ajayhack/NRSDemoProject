package com.example.nrsdemoproject.view.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.widget.AppCompatButton
import androidx.recyclerview.widget.RecyclerView
import com.example.nrsdemoproject.R
import com.example.nrsdemoproject.modal.NRSDataModal

class DefaultStateListAdapter(private var defaultStateList : MutableList<NRSDataModal>?,
                              private var onDefaultStateItemClickEvent: (Int , Boolean) -> Unit) :
    RecyclerView.Adapter<DefaultStateListAdapter.DefaultStateViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): DefaultStateViewHolder {
        return DefaultStateViewHolder(
            LayoutInflater.from(parent.context).inflate(R.layout.list_item, parent, false))
    }

    override fun onBindViewHolder(holder: DefaultStateViewHolder, position: Int) {
        holder.bindData(position)
    }

    override fun getItemCount(): Int = defaultStateList?.size ?: 0

    inner class DefaultStateViewHolder(itemView : View) : RecyclerView.ViewHolder(itemView){
        val stateNameButton = itemView.findViewById<AppCompatButton>(R.id.stateNameButton)

        fun bindData(position: Int){
            val model = defaultStateList?.get(position)
            stateNameButton.text = model?.state
        }

        init {
            stateNameButton.setOnClickListener { onDefaultStateItemClickEvent(adapterPosition , true) }
        }
    }
}