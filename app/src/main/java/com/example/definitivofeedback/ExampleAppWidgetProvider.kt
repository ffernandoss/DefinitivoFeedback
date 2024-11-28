package com.example.definitivofeedback

import android.appwidget.AppWidgetManager
import android.appwidget.AppWidgetProvider
import android.content.Context
import android.widget.RemoteViews
import androidx.work.*
import com.google.firebase.firestore.FirebaseFirestore
import java.util.concurrent.TimeUnit

class ExampleAppWidgetProvider : AppWidgetProvider() {
    override fun onUpdate(context: Context, appWidgetManager: AppWidgetManager, appWidgetIds: IntArray) {
        for (appWidgetId in appWidgetIds) {
            scheduleWidgetUpdate(context, appWidgetId)
        }
    }

    private fun scheduleWidgetUpdate(context: Context, appWidgetId: Int) {
        val workRequest = PeriodicWorkRequestBuilder<WidgetUpdateWorker>(15, TimeUnit.MINUTES)
            .setInputData(workDataOf("appWidgetId" to appWidgetId))
            .build()
        WorkManager.getInstance(context).enqueueUniquePeriodicWork(
            "WidgetUpdateWork_$appWidgetId",
            ExistingPeriodicWorkPolicy.REPLACE,
            workRequest
        )
    }
}

class WidgetUpdateWorker(context: Context, params: WorkerParameters) : CoroutineWorker(context, params) {
    override suspend fun doWork(): Result {
        val appWidgetId = inputData.getInt("appWidgetId", -1)
        if (appWidgetId == -1) return Result.failure()

        val appWidgetManager = AppWidgetManager.getInstance(applicationContext)
        updateWidget(applicationContext, appWidgetManager, appWidgetId)
        return Result.success()
    }

    private fun updateWidget(context: Context, appWidgetManager: AppWidgetManager, appWidgetId: Int) {
        obtenerTodasLasNovelas { novelas ->
            val novelasTexto = if (novelas.isNotEmpty()) {
                novelas.joinToString(separator = "\n") { it.nombre }
            } else {
                "No hay novelas disponibles"
            }

            val views = RemoteViews(context.packageName, R.layout.example_loading_appwidget)
            views.setTextViewText(R.id.appwidget_text, novelasTexto)

            appWidgetManager.updateAppWidget(appWidgetId, views)
        }
    }

    private fun obtenerTodasLasNovelas(callback: (List<Novela>) -> Unit) {
        val db = FirebaseFirestore.getInstance()
        db.collection("novelas")
            .get()
            .addOnSuccessListener { result ->
                val novelas = result.map { document ->
                    document.toObject(Novela::class.java)
                }
                callback(novelas)
            }
            .addOnFailureListener {
                callback(emptyList())
            }
    }
}