package com.example.att_android_mariane_mello

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.example.att_android_mariane_mello.network.CatFactApi
import com.example.att_android_mariane_mello.ui.theme.Att_Android_mariane_melloTheme
import kotlinx.coroutines.launch

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            Att_Android_mariane_melloTheme {
                Scaffold(modifier = Modifier.fillMaxSize()) { innerPadding ->
                    CatFactScreen(modifier = Modifier.padding(innerPadding))
                }
            }
        }
    }
}

@Composable
fun CatFactScreen(modifier: Modifier = Modifier) {
    val escopo = rememberCoroutineScope()
    var fato by remember { mutableStateOf("Toque no botao para buscar um fato sobre gatos.") }
    var carregando by remember { mutableStateOf(false) }

    Column(
        modifier = modifier
            .fillMaxSize()
            .padding(24.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        Text(
            text = "Fato aleatorio sobre gatos",
            style = MaterialTheme.typography.headlineSmall,
            textAlign = TextAlign.Center
        )

        Spacer(Modifier.height(24.dp))

        Card(modifier = Modifier.fillMaxWidth()) {
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .heightIn(min = 140.dp)
                    .padding(16.dp),
                contentAlignment = Alignment.Center
            ) {
                if (carregando) {
                    CircularProgressIndicator()
                } else {
                    Text(
                        text = fato,
                        style = MaterialTheme.typography.bodyLarge,
                        textAlign = TextAlign.Center
                    )
                }
            }
        }

        Spacer(Modifier.height(24.dp))

        Button(
            onClick = {
                // A corrotina chama a funcao suspend do Retrofit.
                // O Retrofit ja executa a requisicao fora da thread principal.
                escopo.launch {
                    carregando = true
                    fato = try {
                        CatFactApi.service.getFact().fact
                    } catch (e: Exception) {
                        "Erro ao buscar o fato: ${e.message}"
                    }
                    carregando = false
                }
            },
            enabled = !carregando
        ) {
            Text("Buscar fato")
        }
    }
}

@Preview(showBackground = true)
@Composable
fun CatFactScreenPreview() {
    Att_Android_mariane_melloTheme {
        CatFactScreen()
    }
}
