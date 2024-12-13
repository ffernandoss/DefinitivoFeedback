package com.example.definitivofeedback

import android.content.Context
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.viewinterop.AndroidView
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.definitivofeedback.ui.theme.DefinitivoFeedbackTheme
import android.content.Intent
import android.content.SharedPreferences
import android.net.Uri
import androidx.compose.ui.unit.dp

class ListaNovelasActivity : ComponentActivity() {
    private lateinit var sharedPreferences: SharedPreferences
    private lateinit var dbHelper: UserDatabaseHelper

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        sharedPreferences = getSharedPreferences("settings", Context.MODE_PRIVATE)
        dbHelper = UserDatabaseHelper(this)
        val isDarkMode = sharedPreferences.getBoolean("dark_mode", false)
        val currentUser = sharedPreferences.getString("current_user", "") ?: ""

        setContent {
            DefinitivoFeedbackTheme(darkTheme = isDarkMode) {
                Scaffold(modifier = Modifier.fillMaxSize()) { innerPadding ->
                    ListaNovelasScreen(modifier = Modifier.padding(innerPadding), dbHelper = dbHelper, currentUser = currentUser)
                }
            }
        }
    }
}

@Composable
fun ListaNovelasScreen(modifier: Modifier = Modifier, dbHelper: UserDatabaseHelper, currentUser: String) {
    var showDialog by remember { mutableStateOf(false) }
    var showDeleteDialog by remember { mutableStateOf(false) }
    var showErrorDialog by remember { mutableStateOf(false) }
    var showDetailsDialog by remember { mutableStateOf(false) }
    var showMapDialog by remember { mutableStateOf(false) }
    var nombre by remember { mutableStateOf("") }
    var año by remember { mutableStateOf("") }
    var descripcion by remember { mutableStateOf("") }
    var valoracion by remember { mutableStateOf("") }
    var latitud by remember { mutableStateOf("") }
    var longitud by remember { mutableStateOf("") }
    var novelas by remember { mutableStateOf(listOf<Novela>()) }
    var nombreABorrar by remember { mutableStateOf("") }
    var mensajeError by remember { mutableStateOf("") }
    var mostrarFavoritas by remember { mutableStateOf(false) }
    var novelaSeleccionada by remember { mutableStateOf<Novela?>(null) }

    val context = LocalContext.current
    val novelaStorage = NovelaStorage()
    val userId = dbHelper.getUserIdByUsername(currentUser)

    LaunchedEffect(Unit) {
        novelas = dbHelper.getNovelasByUser(userId)
    }

    Column(
        modifier = modifier
            .fillMaxSize()
            .padding(16.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        // Display coordinates at the top
        Text(text = "Latitud: $latitud, Longitud: $longitud")
        Spacer(modifier = Modifier.height(16.dp))

        // Display specified coordinates
        Text(text = "1. 39.8570° N, -4.0235° W")
        Text(text = "2. 39.8444° N, -4.0065° W")
        Text(text = "3. 39.8640° N, -4.0006° W")
        Spacer(modifier = Modifier.height(16.dp))

        Button(onClick = { showDialog = true }) {
            Text(text = "Añadir novela")
        }
        Spacer(modifier = Modifier.height(16.dp))
        Button(onClick = { showDeleteDialog = true }) {
            Text(text = "Eliminar novela")
        }
        Spacer(modifier = Modifier.height(16.dp))
        Button(onClick = { mostrarFavoritas = !mostrarFavoritas }) {
            Text(text = if (mostrarFavoritas) "Mostrar todas las novelas" else "Mostrar novelas favoritas")
        }
        Spacer(modifier = Modifier.height(16.dp))
        Button(onClick = {
            val intent = Intent(context, MainActivity::class.java)
            context.startActivity(intent)
        }) {
            Text(text = "Volver a Main")
        }

        val novelasAMostrar = if (mostrarFavoritas) novelas.filter { it.isFavorite } else novelas

        val recyclerView = remember { RecyclerView(context) }
        recyclerView.layoutManager = LinearLayoutManager(context)
        recyclerView.adapter = NovelaAdapter(context, novelasAMostrar, { novela ->
            novelaStorage.updateFavoriteStatus(novela) { success ->
                if (success) {
                    novelas = novelas.map { if (it.nombre == novela.nombre) novela else it }
                } else {
                    mensajeError = "Error al actualizar el estado de favorito en Firestore"
                    showErrorDialog = true
                }
            }
        }, { novela ->
            novelaSeleccionada = novela
            showDetailsDialog = true
        })

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
                        TextField(
                            value = latitud,
                            onValueChange = { latitud = it },
                            label = { Text("Latitud") }
                        )
                        TextField(
                            value = longitud,
                            onValueChange = { longitud = it },
                            label = { Text("Longitud") }
                        )
                    }
                },
                confirmButton = {
                    Button(onClick = {
                        when {
                            nombre.isBlank() || año.isBlank() || descripcion.isBlank() || valoracion.isBlank() || latitud.isBlank() || longitud.isBlank() -> {
                                mensajeError = "Todos los campos deben estar completos"
                                showErrorDialog = true
                            }
                            !año.all { it.isDigit() } -> {
                                mensajeError = "El año no debe contener letras"
                                showErrorDialog = true
                            }
                            !valoracion.all { it.isDigit() || it == '.' } -> {
                                mensajeError = "La valoración no debe contener letras"
                                showErrorDialog = true
                            }
                            !latitud.matches(Regex("-?\\d*\\.?\\d+")) -> {  // Allow negative values and decimal
                                mensajeError = "La latitud debe ser un número válido"
                                showErrorDialog = true
                            }
                            !longitud.matches(Regex("-?\\d*\\.?\\d+")) -> {  // Allow negative values and decimal
                                mensajeError = "La longitud debe ser un número válido"
                                showErrorDialog = true
                            }
                            else -> {
                                val nuevaNovela = Novela(
                                    nombre = nombre,
                                    año = año.toInt(),
                                    descripcion = descripcion,
                                    valoracion = valoracion.toDouble(),
                                    isFavorite = false,
                                    latitud = latitud.toDouble(),
                                    longitud = longitud.toDouble()
                                )
                                dbHelper.addNovelaForUser(userId, nuevaNovela)
                                novelaStorage.saveNovela(nuevaNovela) { success ->
                                    if (success) {
                                        novelas = dbHelper.getNovelasByUser(userId)
                                        nombre = ""
                                        año = ""
                                        descripcion = ""
                                        valoracion = ""
                                        latitud = ""
                                        longitud = ""
                                        showDialog = false
                                    } else {
                                        mensajeError = "Error al guardar la novela en Firestore"
                                        showErrorDialog = true
                                    }
                                }
                            }
                        }
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
                        dbHelper.deleteNovelaForUser(userId, nombreABorrar)
                        novelaStorage.deleteNovela(nombreABorrar) { success ->
                            if (success) {
                                novelas = dbHelper.getNovelasByUser(userId)
                                nombreABorrar = ""
                                showDeleteDialog = false
                            } else {
                                mensajeError = "Error al borrar la novela en Firestore"
                                showErrorDialog = true
                            }
                        }
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

        if (showDetailsDialog && novelaSeleccionada != null) {
            AlertDialog(
                onDismissRequest = { showDetailsDialog = false },
                title = { Text(text = "Detalles de la novela") },
                text = {
                    Column {
                        Text(text = "Nombre: ${novelaSeleccionada?.nombre}")
                        Text(text = "Año: ${novelaSeleccionada?.año}")
                        Text(text = "Descripción: ${novelaSeleccionada?.descripcion}")
                        Text(text = "Valoración: ${novelaSeleccionada?.valoracion}")
                        Text(text = "Latitud: ${novelaSeleccionada?.latitud}")
                        Text(text = "Longitud: ${novelaSeleccionada?.longitud}")
                        Text(text = "Favorita: ${if (novelaSeleccionada?.isFavorite == true) "Sí" else "No"}")
                    }
                },
                confirmButton = {
                    Button(onClick = { showDetailsDialog = false }) {
                        Text("Cerrar")
                    }
                },
                dismissButton = {
                    Button(onClick = {
                        novelaStorage.getNovelaByName(novelaSeleccionada?.nombre ?: "") { novela ->
                            if (novela != null) {
                                val mapsUrl = "https://www.google.com/maps?q=${novela.latitud},${novela.longitud}"
                                val intent = Intent(Intent.ACTION_VIEW, Uri.parse(mapsUrl))
                                context.startActivity(intent)
                            }
                        }
                    }) {
                        Text("MAPA")
                    }
                }
            )
        }

        if (showMapDialog) {
            AlertDialog(
                onDismissRequest = { showMapDialog = false },
                title = { Text(text = "Coordenadas de la novela") },
                text = {
                    Column {
                        Text(text = "Latitud: $latitud")
                        Text(text = "Longitud: $longitud")
                    }
                },
                confirmButton = {
                    Button(onClick = { showMapDialog = false }) {
                        Text("Cerrar")
                    }
                }
            )
        }
    }
}

fun isValidCoordinate(coordinate: String): Boolean {
    return try {
        val value = coordinate.toDouble()
        value in -180.0..180.0
    } catch (e: Exception) {
        false
    }
}
