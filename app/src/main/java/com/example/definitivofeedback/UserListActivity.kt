package com.example.definitivofeedback

import android.content.Intent
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import com.example.definitivofeedback.ui.theme.DefinitivoFeedbackTheme

class UserListActivity : ComponentActivity() {
    private lateinit var dbHelper: UserDatabaseHelper

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        dbHelper = UserDatabaseHelper(this)

        setContent {
            DefinitivoFeedbackTheme {
                Scaffold(modifier = Modifier.fillMaxSize()) { innerPadding ->
                    UserListScreen(modifier = Modifier.padding(innerPadding))
                }
            }
        }
    }

    @Composable
    fun UserListScreen(modifier: Modifier = Modifier) {
        val users by remember { mutableStateOf(dbHelper.getAllUsers()) }
        val context = LocalContext.current

        Column(
            modifier = modifier
                .fillMaxSize()
                .padding(16.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center
        ) {
            Text(text = "Lista de Usuarios Registrados")
            Spacer(modifier = Modifier.height(16.dp))
            users.forEach { user ->
                Text(text = "ID: ${user.id}, Usuario: ${user.username}, Contraseña: ${user.password}")
                Spacer(modifier = Modifier.height(8.dp))
            }
            Spacer(modifier = Modifier.height(16.dp))
            Button(onClick = {
                context.startActivity(Intent(context, MainActivity::class.java))
            }) {
                Text(text = "Volver a Main")
            }
        }
    }
}