package com.example.definitivofeedback

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.example.definitivofeedback.ui.theme.DefinitivoFeedbackTheme

data class Novela(val nombre: String, val año: String, val descripcion: String, val valoracion: String, var isFavorite: Boolean)

class ListaNovelasActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            DefinitivoFeedbackTheme {
                Scaffold(modifier = Modifier.fillMaxSize()) { innerPadding ->
                    ListaNovelasScreen(modifier = Modifier.padding(innerPadding))
                }
            }
        }
    }
}

@Composable
fun ListaNovelasScreen(modifier: Modifier = Modifier) {
    var showDialog by remember { mutableStateOf(false) }
    var nombre by remember { mutableStateOf("") }
    var año by remember { mutableStateOf("") }
    var descripcion by remember { mutableStateOf("") }
    var valoracion by remember { mutableStateOf("") }
    var novelas by remember { mutableStateOf(listOf<Novela>()) }

    Column(
        modifier = modifier
            .fillMaxSize()
            .padding(16.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        Button(onClick = { showDialog = true }) {
            Text(text = "Añadir novela")
        }
        Spacer(modifier = Modifier.height(16.dp))
        Button(onClick = { /* TODO: Eliminar novela */ }) {
            Text(text = "Eliminar novela")
        }

        LazyColumn(modifier = Modifier.fillMaxSize()) {
            items(novelas) { novela ->
                var isFavorite by remember { mutableStateOf(novela.isFavorite) }
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Column(modifier = Modifier.weight(1f)) {
                        Text(text = "${novela.nombre} (${novela.año}) - ${novela.descripcion}")
                        Text(text = "Valoración: ${novela.valoracion}")
                    }
                    Checkbox(
                        checked = isFavorite,
                        onCheckedChange = { isChecked ->
                            isFavorite = isChecked
                            novela.isFavorite = isChecked
                            // Trigger recomposition
                            novelas = novelas.map { if (it == novela) it.copy(isFavorite = isChecked) else it }
                        }
                    )
                }
                Spacer(modifier = Modifier.height(8.dp))
            }
        }

        if (showDialog) {
            AlertDialog(
                onDismissRequest = { showDialog = false },
                title = { Text(text = "Añadir novela") },
                text = {
                    Column {
                        TextField(
                            value = nombre,
                            onValueChange = { nombre = it },
                            label = { Text("Nombre") }
                        )
                        TextField(
                            value = año,
                            onValueChange = { año = it },
                            label = { Text("Año") }
                        )
                        TextField(
                            value = descripcion,
                            onValueChange = { descripcion = it },
                            label = { Text("Descripción") }
                        )
                        TextField(
                            value = valoracion,
                            onValueChange = { valoracion = it },
                            label = { Text("Valoración") }
                        )
                    }
                },
                confirmButton = {
                    Button(onClick = {
                        novelas = novelas + Novela(nombre, año, descripcion, valoracion, false)
                        nombre = ""
                        año = ""
                        descripcion = ""
                        valoracion = ""
                        showDialog = false
                    }) {
                        Text("Añadir")
                    }
                },
                dismissButton = {
                    Button(onClick = { showDialog = false }) {
                        Text("Cancelar")
                    }
                }
            )
        }
    }
}

@Preview(showBackground = true)
@Composable
fun ListaNovelasScreenPreview() {
    DefinitivoFeedbackTheme {
        ListaNovelasScreen()
    }
}