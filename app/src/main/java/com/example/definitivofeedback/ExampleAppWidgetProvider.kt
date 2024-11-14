package com.example.definitivofeedback

import android.appwidget.AppWidgetManager
import android.appwidget.AppWidgetProvider
import android.content.Context
import android.os.Handler
import android.os.Looper
import android.widget.RemoteViews
import com.google.firebase.firestore.FirebaseFirestore

class ExampleAppWidgetProvider : AppWidgetProvider() {
    private val handler = Handler(Looper.getMainLooper())
    private val updateInterval = 3000L // 3 seconds

    override fun onUpdate(context: Context, appWidgetManager: AppWidgetManager, appWidgetIds: IntArray) {
        for (appWidgetId in appWidgetIds) {
            updateWidget(context, appWidgetManager, appWidgetId)
        }
    }

    private fun updateWidget(context: Context, appWidgetManager: AppWidgetManager, appWidgetId: Int) {
        // Obtener las novelas favoritas del usuario desde Firebase
        obtenerNovelasFavoritasDelUsuario { novelas ->
            // Crear el texto para mostrar las novelas
            val novelasTexto = novelas.joinToString(separator = "\n") { it.nombre }

            // Actualizar el RemoteViews del widget
            val views = RemoteViews(context.packageName, R.layout.example_loading_appwidget)
            views.setTextViewText(R.id.appwidget_text, novelasTexto)

            // Actualizar el widget
            appWidgetManager.updateAppWidget(appWidgetId, views)

            // Programar la siguiente actualización
            handler.postDelayed({ updateWidget(context, appWidgetManager, appWidgetId) }, updateInterval)
        }
    }

    private fun obtenerNovelasFavoritasDelUsuario(callback: (List<Novela>) -> Unit) {
        val db = FirebaseFirestore.getInstance()
        db.collection("novelas")
            .whereEqualTo("isFavorite", true)
            .get()
            .addOnSuccessListener { result ->
                val novelas = result.map { document ->
                    document.toObject(Novela::class.java)
                }
                callback(novelas)
            }
            .addOnFailureListener { exception ->
                callback(emptyList())
            }
    }
}