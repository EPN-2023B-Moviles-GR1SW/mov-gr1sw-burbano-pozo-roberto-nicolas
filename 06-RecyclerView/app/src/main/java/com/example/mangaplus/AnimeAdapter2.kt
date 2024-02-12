package com.example.mangaplus

import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.squareup.picasso.Callback
import com.squareup.picasso.Picasso

class AnimeAdapter2(
    private val contexto: Shelf,
    private val list: ArrayList<Animes>,
    private val recyclerView: RecyclerView
) : RecyclerView.Adapter<AnimeAdapter2.MyViewHolder>() {

    inner class MyViewHolder(view: View) : RecyclerView.ViewHolder(
        view
    ) {
        val titleTextView: TextView
        val viewsTextView: TextView
        val front_pageImagen: ImageView

        init {
            titleTextView = view.findViewById(R.id.tv_title)
            viewsTextView = view.findViewById(R.id.tv_views)
            front_pageImagen = view.findViewById(R.id.iv_front_page)
        }

    }


    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): AnimeAdapter2.MyViewHolder {
        val itemView = LayoutInflater
            .from(parent.context)
            .inflate(
                R.layout.animes_rv,
                parent,
                false
            )
        return MyViewHolder(itemView)
    }

    override fun getItemCount(): Int {
        return this.list.size
    }


    override fun onBindViewHolder(
        holder: MyViewHolder, position: Int
    ) {
        val animeCurrent = this.list[position]
        holder.titleTextView.text = animeCurrent.title

//        holder.front_pageImagen.contentDescription = animeCurrent.front_page
        holder.viewsTextView.text = animeCurrent.views.toString()
        Log.d("img",animeCurrent.front_page.toString())
        Picasso.get()
            .load(animeCurrent.front_page)
            .into(holder.front_pageImagen, object : Callback {
                override fun onSuccess() {
                    // Image loaded successfully
                }

                override fun onError(e: Exception) {
                    Log.d("error",e.toString())
                    // Handle errors, e.g., log an error message or show a toast
                }
            })
    }
}