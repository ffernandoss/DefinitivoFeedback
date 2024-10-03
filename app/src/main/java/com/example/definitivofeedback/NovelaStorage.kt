package com.example.definitivofeedback

import android.util.Log
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase


data class Novela(
    val nombre: String = "",
    val año: Int = 0,
    val descripcion: String = "",
    val valoracion: Double = 0.0,
    val isFavorite: Boolean = false
)

class NovelaStorage {
    private val db: FirebaseFirestore = Firebase.firestore

    fun saveNovela(novela: Novela) {
        db.collection("novelas")
            .add(novela)
            .addOnSuccessListener { documentReference ->
                Log.d("NovelaStorage", "DocumentSnapshot added with ID: ${documentReference.id}")
            }
            .addOnFailureListener { e ->
                Log.w("NovelaStorage", "Error adding document", e)
            }
    }

    fun getNovelas(callback: (List<Novela>) -> Unit) {
        db.collection("novelas")
            .get()
            .addOnSuccessListener { result ->
                val novelas = result.map { document ->
                    document.toObject(Novela::class.java)
                }
                callback(novelas)
            }
            .addOnFailureListener { exception ->
                Log.w("NovelaStorage", "Error getting documents.", exception)
                callback(emptyList())
            }
    }
}