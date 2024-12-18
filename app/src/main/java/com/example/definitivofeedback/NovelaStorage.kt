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
    val isFavorite: Boolean = false,
    val latitud: Double = 0.0,
    val longitud: Double = 0.0
)

class NovelaStorage {
    private val db: FirebaseFirestore = Firebase.firestore

    fun getNovelaByName(nombre: String, callback: (Novela?) -> Unit) {
        db.collection("novelas")
            .whereEqualTo("nombre", nombre)
            .get()
            .addOnSuccessListener { result ->
                val novela = result.documents.firstOrNull()?.toObject(Novela::class.java)
                callback(novela)
            }
            .addOnFailureListener { exception ->
                Log.w("NovelaStorage", "Error getting document.", exception)
                callback(null)
            }
    }



    fun saveNovela(novela: Novela, callback: (Boolean) -> Unit) {
        db.collection("novelas")
            .add(novela)
            .addOnSuccessListener {
                Log.d("NovelaStorage", "DocumentSnapshot successfully written!")
                callback(true)
            }
            .addOnFailureListener { e ->
                Log.w("NovelaStorage", "Error writing document", e)
                callback(false)
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

    fun deleteNovela(nombre: String, callback: (Boolean) -> Unit) {
        db.collection("novelas")
            .whereEqualTo("nombre", nombre)
            .get()
            .addOnSuccessListener { result ->
                for (document in result) {
                    db.collection("novelas").document(document.id).delete()
                        .addOnSuccessListener {
                            Log.d("NovelaStorage", "DocumentSnapshot successfully deleted!")
                            callback(true)
                        }
                        .addOnFailureListener { e ->
                            Log.w("NovelaStorage", "Error deleting document", e)
                            callback(false)
                        }
                }
            }
            .addOnFailureListener { e ->
                Log.w("NovelaStorage", "Error finding document", e)
                callback(false)
            }
    }

    fun updateFavoriteStatus(novela: Novela, callback: (Boolean) -> Unit) {
        db.collection("novelas")
            .whereEqualTo("nombre", novela.nombre)
            .get()
            .addOnSuccessListener { result ->
                for (document in result) {
                    db.collection("novelas").document(document.id)
                        .update("isFavorite", novela.isFavorite)
                        .addOnSuccessListener {
                            Log.d("NovelaStorage", "DocumentSnapshot successfully updated!")
                            callback(true)
                        }
                        .addOnFailureListener { e ->
                            Log.w("NovelaStorage", "Error updating document", e)
                            callback(false)
                        }
                }
            }
            .addOnFailureListener { e ->
                Log.w("NovelaStorage", "Error finding document", e)
                callback(false)
            }
    }
}