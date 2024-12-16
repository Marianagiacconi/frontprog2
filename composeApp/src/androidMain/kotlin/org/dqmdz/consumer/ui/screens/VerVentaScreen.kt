package org.dqmdz.consumer.ui.screens

import android.annotation.SuppressLint
import androidx.compose.foundation.layout.*
import androidx.compose.material.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.input.TextFieldValue
import androidx.compose.ui.unit.dp
import model.Venta

@SuppressLint("UnusedMaterialScaffoldPaddingParameter")
@Composable
fun VerVentasScreen(
    venta: Venta,
    onNavigateBack: () -> Unit
) {
    // Usa directamente el dispositivo para inicializar los estados
    var nombre by remember { mutableStateOf(TextFieldValue(venta.id.toString())) }
    var precio by remember { mutableStateOf(TextFieldValue(venta.precioFinal.toString())) }

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Modificar Venta") },
                backgroundColor = MaterialTheme.colors.primary
            )
        },
        content = {
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(16.dp),
                verticalArrangement = Arrangement.spacedBy(16.dp)
            ) {
                OutlinedTextField(
                    value = nombre,
                    onValueChange = { nombre = it },
                    label = { Text("Nombre") },
                    modifier = Modifier.fillMaxWidth()
                )
                OutlinedTextField(
                    value = precio,
                    onValueChange = { precio = it },
                    label = { Text("Descripción") },
                    modifier = Modifier.fillMaxWidth()
                )


            }
        }
    )
}
