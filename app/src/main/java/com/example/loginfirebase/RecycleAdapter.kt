package com.example.loginfirebase

import android.annotation.SuppressLint
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide

class RecycleAdapter(private val items: ArrayList<Item>) : RecyclerView.Adapter<RecycleAdapter.ViewHolder>() {
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        return ViewHolder(
            LayoutInflater.from(parent.context).inflate(R.layout.item_member, parent, false)
        )
    }

    override fun getItemCount() = items.size

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.bind(items[position])
    }

    class ViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        @SuppressLint("SetTextI18n")
        fun bind(item: Item) {
            val nameTextView = itemView.findViewById<TextView>(R.id.nameTextView)
            val imageView = itemView.findViewById<ImageView>(R.id.imageView)
            nameTextView.text = item.title
            Glide.with(itemView).load(item.url).into(imageView)

        }
    }
}