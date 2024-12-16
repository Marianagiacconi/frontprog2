package org.dqmdz.consumer.ui.componentes

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import model.Dispositivo

@Composable
fun DispositivoCard(
    dispositivo: Dispositivo,
    onEdit: (Int) -> Unit
) {
    // Card del dispositivo
    Card(
        modifier = Modifier
            .padding(8.dp)
            .fillMaxWidth(),
        shape = RoundedCornerShape(12.dp),
        elevation = 8.dp
    ) {
        Column(
            modifier = Modifier.padding(16.dp),
            verticalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            // Información básica
            Text(
                text = "Código: ${dispositivo.codigo}",
                fontSize = 14.sp,
                color = Color.Gray
            )
            Text(
                text = "Nombre: ${dispositivo.nombre}",
                fontSize = 18.sp,
                fontWeight = FontWeight.Bold
            )
            Text(
                text = "Descripción: ${dispositivo.descripcion}",
                fontSize = 14.sp,
                color = Color.DarkGray
            )
            Text(
                text = "Precio Base: ${String.format("%.2f", dispositivo.precioBase)} ${dispositivo.moneda}",
                fontSize = 14.sp,
                color = Color.Green
            )

            // Botones de acción
            Row(
                horizontalArrangement = Arrangement.spacedBy(16.dp),
                modifier = Modifier.fillMaxWidth()
            ) {

                Button(
                    onClick = {
                        dispositivo.id?.let { onEdit(it) } // Solo llamar a onEdit si id no es nulo

                    },
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Text("Comprar")
                }
            }
        }
    }
}
