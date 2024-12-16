package org.dqmdz.consumer.ui.componentes

import androidx.compose.material.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.sp

@Composable
fun EliminarDispositivoDialog(
    dispositivoNombre: String,
    onConfirm: () -> Unit,
    onDismiss: () -> Unit
) {
    AlertDialog(
        onDismissRequest = onDismiss,
        title = {
            Text(
                text = "Confirmación",
                fontSize = 18.sp,
                fontWeight = FontWeight.Bold,
                color = Color.Black
            )
        },
        text = {
            Text(
                text = "¿Estás seguro de que deseas eliminar el dispositivo \"$dispositivoNombre\"?",
                fontSize = 14.sp,
                color = Color.DarkGray
            )
        },
        confirmButton = {
            Button(
                onClick = onConfirm,
                colors = ButtonDefaults.buttonColors(backgroundColor = Color.Red)
            ) {
                Text(
                    text = "Eliminar",
                    color = Color.White,
                    fontWeight = FontWeight.Bold
                )
            }
        },
        dismissButton = {
            Button(
                onClick = onDismiss,
                colors = ButtonDefaults.buttonColors(backgroundColor = Color.LightGray)
            ) {
                Text(
                    text = "Cancelar",
                    color = Color.Black
                )
            }
        }
    )
}
