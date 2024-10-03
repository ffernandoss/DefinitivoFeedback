package com.example.definitivofeedback

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.CheckBox
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView

class NovelaAdapter(private val novelas: List<Novela>, private val onFavoriteChanged: (Novela) -> Unit) :
    RecyclerView.Adapter<NovelaAdapter.NovelaViewHolder>() {

    inner class NovelaViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val nombreTextView: TextView = itemView.findViewById(R.id.nombreTextView)
        val descripcionTextView: TextView = itemView.findViewById(R.id.descripcionTextView)
        val valoracionTextView: TextView = itemView.findViewById(R.id.valoracionTextView)
        val favoriteCheckBox: CheckBox = itemView.findViewById(R.id.favoriteCheckBox)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): NovelaViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.item_novela, parent, false)
        return NovelaViewHolder(view)
    }

    override fun onBindViewHolder(holder: NovelaViewHolder, position: Int) {
        val novela = novelas[position]
        holder.nombreTextView.text = "${novela.nombre} (${novela.año})"
        holder.descripcionTextView.text = novela.descripcion
        holder.valoracionTextView.text = "Valoración: ${novela.valoracion}"
        holder.favoriteCheckBox.isChecked = novela.isFavorite
        holder.favoriteCheckBox.setOnCheckedChangeListener { _, isChecked ->
            val updatedNovela = novela.copy(isFavorite = isChecked)
            onFavoriteChanged(updatedNovela)
        }
    }

    override fun getItemCount() = novelas.size
}