package com.luwliette.mylibrary.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.luwliette.mylibrary.R

class BookAdapter(private val bookList: List<Book>, private val onItemClick: (Book) -> Unit) :
    RecyclerView.Adapter<BookAdapter.BookViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): BookViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.item_book, parent, false)
        return BookViewHolder(view)
    }

    override fun onBindViewHolder(holder: BookViewHolder, position: Int) {
        val book = bookList[position]
        holder.textViewName.text = book.name
        holder.textViewCode.text = book.code

        // Cambia el color del círculo dependiendo del estado de disponibilidad
        if (book.estado == "disponible") {
            holder.viewStatus.setBackgroundResource(R.drawable.circle_green)
        } else {
            holder.viewStatus.setBackgroundResource(R.drawable.circle_red)
        }

        // Manejar clics en el ítem
        holder.itemView.setOnClickListener {
            onItemClick(book)
        }
    }

    override fun getItemCount(): Int {
        return bookList.size
    }

    class BookViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val textViewName: TextView = itemView.findViewById(R.id.textViewName)
        val textViewCode: TextView = itemView.findViewById(R.id.textViewCode)
        val imageViewBook: ImageView = itemView.findViewById(R.id.imageViewBook)
        val viewStatus: View = itemView.findViewById(R.id.viewStatus)
    }
}
