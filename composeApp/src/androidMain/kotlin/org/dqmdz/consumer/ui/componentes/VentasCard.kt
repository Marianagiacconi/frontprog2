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
import model.Venta

@Composable
fun VentaCard(
    venta: Venta,
    onEdit: (Int) -> Unit
) {
    // Card de la venta
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
                text = "id: ${venta.id}",
                fontSize = 14.sp,
                color = Color.Gray
            )


            // Botones de acción
            Row(
                horizontalArrangement = Arrangement.spacedBy(16.dp),
                modifier = Modifier.fillMaxWidth()
            ) {
                Button(
                    onClick = {
                        venta.id.let { onEdit(it) } // Solo llamar a onEdit si id no es nulo
                    },
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Text("Ver")
                }
            }
        }
    }
}
