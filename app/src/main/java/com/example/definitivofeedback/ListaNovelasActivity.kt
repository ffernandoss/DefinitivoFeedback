package com.example.definitivofeedback

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.viewinterop.AndroidView
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.definitivofeedback.ui.theme.DefinitivoFeedbackTheme

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
    var showDeleteDialog by remember { mutableStateOf(false) }
    var showErrorDialog by remember { mutableStateOf(false) }
    var nombre by remember { mutableStateOf("") }
    var año by remember { mutableStateOf("") }
    var descripcion by remember { mutableStateOf("") }
    var valoracion by remember { mutableStateOf("") }
    var novelas by remember { mutableStateOf(listOf<Novela>()) }
    var nombreABorrar by remember { mutableStateOf("") }
    var mensajeError by remember { mutableStateOf("") }

    val context = LocalContext.current

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
        Button(onClick = { showDeleteDialog = true }) {
            Text(text = "Eliminar novela")
        }

        val recyclerView = remember { RecyclerView(context) }
        recyclerView.layoutManager = LinearLayoutManager(context)
        recyclerView.adapter = NovelaAdapter(novelas) { novela ->
            novelas = novelas.map { if (it == novela) it.copy(isFavorite = novela.isFavorite) else it }
        }

        AndroidView({ recyclerView }, modifier = Modifier.fillMaxSize())

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

        if (showDeleteDialog) {
            AlertDialog(
                onDismissRequest = { showDeleteDialog = false },
                title = { Text(text = "Eliminar novela") },
                text = {
                    Column {
                        TextField(
                            value = nombreABorrar,
                            onValueChange = { nombreABorrar = it },
                            label = { Text("Nombre de la novela a borrar") }
                        )
                    }
                },
                confirmButton = {
                    Button(onClick = {
                        val novelaAEliminar = novelas.find { it.nombre == nombreABorrar }
                        if (novelaAEliminar != null) {
                            novelas = novelas - novelaAEliminar
                            mensajeError = ""
                        } else {
                            mensajeError = "No se ha encontrado ninguna novela con ese nombre"
                            showErrorDialog = true
                        }
                        nombreABorrar = ""
                        showDeleteDialog = false
                    }) {
                        Text("Eliminar")
                    }
                },
                dismissButton = {
                    Button(onClick = { showDeleteDialog = false }) {
                        Text("Cancelar")
                    }
                }
            )
        }

        if (showErrorDialog) {
            AlertDialog(
                onDismissRequest = { showErrorDialog = false },
                title = { Text(text = "Error") },
                text = { Text(text = mensajeError) },
                confirmButton = {
                    Button(onClick = { showErrorDialog = false }) {
                        Text("OK")
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