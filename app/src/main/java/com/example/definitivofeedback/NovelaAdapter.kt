package com.example.definitivofeedback

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.CheckBox
import android.widget.TextView
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.RecyclerView

class NovelaAdapter(
    private val context: Context,
    private val novelas: List<Novela>,
    private val onFavoriteChanged: (Novela) -> Unit,
    private val onItemClick: (Novela) -> Unit
) : RecyclerView.Adapter<NovelaAdapter.NovelaViewHolder>() {

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

        // Set text color based on dark mode
        val isDarkMode = context.getSharedPreferences("settings", Context.MODE_PRIVATE)
            .getBoolean("dark_mode", false)
        val textColor = if (isDarkMode) {
            ContextCompat.getColor(context, R.color.textColorDark)
        } else {
            ContextCompat.getColor(context, R.color.textColorLight)
        }

        holder.nombreTextView.setTextColor(textColor)
        holder.descripcionTextView.setTextColor(textColor)
        holder.valoracionTextView.setTextColor(textColor)

        holder.favoriteCheckBox.setOnCheckedChangeListener { _, isChecked ->
            val updatedNovela = novela.copy(isFavorite = isChecked)
            onFavoriteChanged(updatedNovela)
        }

        holder.itemView.setOnClickListener {
            onItemClick(novela)
        }
    }

    override fun getItemCount() = novelas.size
}