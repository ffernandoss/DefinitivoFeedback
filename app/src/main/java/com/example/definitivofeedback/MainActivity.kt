package com.example.definitivofeedback

import android.content.Intent
import android.os.Bundle
import android.util.Log
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.*
import androidx.compose.material3.Button
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.example.definitivofeedback.ui.theme.DefinitivoFeedbackTheme
import com.google.firebase.analytics.FirebaseAnalytics

class MainActivity : ComponentActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()


        setContent {
            DefinitivoFeedbackTheme {
                Scaffold(modifier = Modifier.fillMaxSize()) { innerPadding ->
                    MainScreen(modifier = Modifier.padding(innerPadding))
                }
            }
        }
    }
}

@Composable
fun MainScreen(modifier: Modifier = Modifier) {
    val context = LocalContext.current
    Column(
        modifier = modifier
            .fillMaxSize()
            .padding(16.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        Text(text = "GESTION DE NOVELAS", modifier = Modifier.padding(bottom = 32.dp))
        Button(onClick = {
            context.startActivity(Intent(context, ListaNovelasActivity::class.java))
        }) {
            Text(text = "Ver lista completa de novelas")
        }
    }
}

@Preview(showBackground = true)
@Composable
fun MainScreenPreview() {
    DefinitivoFeedbackTheme {
        MainScreen()
    }
}