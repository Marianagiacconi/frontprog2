package org.dqmdz.consumer.ui.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.CheckCircle
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

@Composable
fun SuccessScreen(
    message: String = "¡Operación exitosa!",
    onNavigateBack: () -> Unit
) {
    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(Color(0xFFE0F7FA)), // Fondo color claro (opcional)
        contentAlignment = Alignment.Center
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center
        ) {
            // Ícono o imagen opcional
            Icon(
                imageVector = Icons.Default.CheckCircle, // Ícono de éxito
                contentDescription = "Éxito",
                tint = Color(0xFF4CAF50), // Verde para éxito
                modifier = Modifier.size(64.dp)
            )

            Spacer(modifier = Modifier.height(16.dp))

            // Mensaje de éxito
            Text(
                text = message,
                fontSize = 20.sp,
                fontWeight = FontWeight.Bold,
                color = Color(0xFF4CAF50) // Verde para resaltar
            )

            Spacer(modifier = Modifier.height(32.dp))

            // Botón para volver
            Button(
                onClick = { onNavigateBack() },
                modifier = Modifier.fillMaxWidth(0.5f)
            ) {
                Text(text = "Aceptar", fontSize = 16.sp)
            }
        }
    }
}
