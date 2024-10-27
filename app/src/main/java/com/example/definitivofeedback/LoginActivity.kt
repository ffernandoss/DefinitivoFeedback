package com.example.definitivofeedback

import android.content.ContentValues
import android.content.Context
import android.content.Intent
import android.database.Cursor
import android.os.Bundle
import android.widget.Toast
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.unit.dp
import com.example.definitivofeedback.ui.theme.DefinitivoFeedbackTheme

class LoginActivity : ComponentActivity() {
    private lateinit var dbHelper: UserDatabaseHelper

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        dbHelper = UserDatabaseHelper(this)

        setContent {
            DefinitivoFeedbackTheme {
                Scaffold(modifier = Modifier.fillMaxSize()) { innerPadding ->
                    LoginScreen(modifier = Modifier.padding(innerPadding))
                }
            }
        }
    }

    @Composable
    fun LoginScreen(modifier: Modifier = Modifier) {
        val context = LocalContext.current
        var username by remember { mutableStateOf("") }
        var password by remember { mutableStateOf("") }
        var isLoginMode by remember { mutableStateOf(true) }

        Column(
            modifier = modifier
                .fillMaxSize()
                .padding(16.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center
        ) {
            Text(text = if (isLoginMode) "Iniciar Sesión" else "Registrarse")
            Spacer(modifier = Modifier.height(16.dp))
            TextField(
                value = username,
                onValueChange = { username = it },
                label = { Text("Usuario") }
            )
            Spacer(modifier = Modifier.height(16.dp))
            TextField(
                value = password,
                onValueChange = { password = it },
                label = { Text("Contraseña") },
                visualTransformation = PasswordVisualTransformation()
            )
            Spacer(modifier = Modifier.height(16.dp))
            Button(onClick = {
                if (isLoginMode) {
                    if (loginUser(context, username, password)) {
                        context.startActivity(Intent(context, MainActivity::class.java))
                    } else {
                        Toast.makeText(context, "Usuario o contraseña incorrectos", Toast.LENGTH_SHORT).show()
                    }
                } else {
                    if (registerUser(context, username, password)) {
                        Toast.makeText(context, "Registro exitoso", Toast.LENGTH_SHORT).show()
                        isLoginMode = true
                    } else {
                        Toast.makeText(context, "Error al registrar usuario", Toast.LENGTH_SHORT).show()
                    }
                }
            }) {
                Text(text = if (isLoginMode) "Iniciar Sesión" else "Registrarse")
            }
            Spacer(modifier = Modifier.height(16.dp))
            TextButton(onClick = { isLoginMode = !isLoginMode }) {
                Text(text = if (isLoginMode) "¿No tienes cuenta? Regístrate" else "¿Ya tienes cuenta? Inicia sesión")
            }
        }
    }

    private fun loginUser(context: Context, username: String, password: String): Boolean {
        val db = dbHelper.readableDatabase
        val cursor: Cursor = db.query(
            UserDatabaseHelper.TABLE_USERS,
            arrayOf(UserDatabaseHelper.COLUMN_ID),
            "${UserDatabaseHelper.COLUMN_USERNAME} = ? AND ${UserDatabaseHelper.COLUMN_PASSWORD} = ?",
            arrayOf(username, password),
            null, null, null
        )
        val isLoggedIn = cursor.moveToFirst()
        cursor.close()
        return isLoggedIn
    }

    private fun registerUser(context: Context, username: String, password: String): Boolean {
        val db = dbHelper.writableDatabase
        val values = ContentValues().apply {
            put(UserDatabaseHelper.COLUMN_USERNAME, username)
            put(UserDatabaseHelper.COLUMN_PASSWORD, password)
        }
        val newRowId = db.insert(UserDatabaseHelper.TABLE_USERS, null, values)
        return newRowId != -1L
    }
}