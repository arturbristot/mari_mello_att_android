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
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
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
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.example.att_android_mariane_mello.network.JokeApi
import com.example.att_android_mariane_mello.network.ViaCepApi
import com.example.att_android_mariane_mello.ui.theme.Att_Android_mariane_melloTheme
import kotlinx.coroutines.launch

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            Att_Android_mariane_melloTheme {
                Scaffold(modifier = Modifier.fillMaxSize()) { innerPadding ->
                    CepScreen(modifier = Modifier.padding(innerPadding))
                }
            }
        }
    }
}

@Composable
fun CepScreen(modifier: Modifier = Modifier) {
    val escopo = rememberCoroutineScope()
    var cep by remember { mutableStateOf("") }
    var resultado by remember { mutableStateOf("Digite um CEP e toque em buscar.") }
    var carregando by remember { mutableStateOf(false) }

    Column(
        modifier = modifier
            .fillMaxSize()
            .padding(24.dp),
        verticalArrangement = Arrangement.Center
    ) {
        Text(
            text = "Busca de CEP",
            style = MaterialTheme.typography.headlineSmall
        )

        Spacer(Modifier.height(24.dp))

        OutlinedTextField(
            value = cep,
            onValueChange = { novo ->
                cep = novo.filter { it.isDigit() }.take(8)
            },
            label = { Text("CEP") },
            placeholder = { Text("88801100") },
            singleLine = true,
            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
            modifier = Modifier.fillMaxWidth()
        )

        Spacer(Modifier.height(16.dp))

        Button(
            onClick = {
                escopo.launch {
                    carregando = true
                    resultado = try {
                        val endereco = ViaCepApi.service.buscarCep(cep)
                        val piada = JokeApi.service.buscarPiada()
                        if (endereco.erro != null) {
                            "CEP não encontrado."
                        } else {
                            "Cidade: ${endereco.localidade}\n" +
                                "Estado: ${endereco.estado} (${endereco.uf})\n" +
                                "Bairro: ${endereco.bairro}\n" +
                                "Rua: ${endereco.logradouro}\n" +
                                "DDD: ${endereco.ddd}\n" +

                            "Piada: ${piada.texto}"
                        }
                    } catch (e: Exception) {
                        "Erro ao buscar o CEP."
                    }
                    carregando = false
                }
            },
            enabled = cep.length == 8 && !carregando,
            modifier = Modifier.fillMaxWidth()
        ) {
            Text("Buscar")
        }

        Spacer(Modifier.height(24.dp))

        Card(modifier = Modifier.fillMaxWidth()) {
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .heightIn(min = 160.dp)
                    .padding(16.dp),
                contentAlignment = Alignment.Center
            ) {
                if (carregando) {
                    CircularProgressIndicator()
                } else {
                    Text(
                        text = resultado,
                        style = MaterialTheme.typography.bodyLarge
                    )
                }
            }
        }
    }
}

@Preview(showBackground = true)
@Composable
fun CepScreenPreview() {
    Att_Android_mariane_melloTheme {
        CepScreen()
    }
}
