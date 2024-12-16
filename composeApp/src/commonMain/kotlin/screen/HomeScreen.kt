package screen

import androidx.compose.runtime.*
import androidx.compose.foundation.layout.*
import androidx.compose.material.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import components.ProductGrid
import model.Dispositivo
import services.fetchDevices


@Composable
fun HomeScreen() {
    var dispositivos by remember { mutableStateOf<List<Dispositivo>>(emptyList()) }
    var loading by remember { mutableStateOf(true) }
    var error by remember { mutableStateOf<String?>(null) }

    // Lanzar efecto para obtener datos
    LaunchedEffect(Unit) {
        try {
            val fetchedDispositivos = fetchDevices()
            dispositivos = fetchedDispositivos // Asignar datos obtenidos
            loading = false
        } catch (e: Exception) {
            error = "Error al obtener datos: ${e.message}"
            loading = false
        }
    }

    // UI
    MaterialTheme {
        Column(modifier = Modifier.fillMaxSize().padding(16.dp)) {
            Text(
                text = "Dispositivos Disponibles",
                style = MaterialTheme.typography.h4,
                modifier = Modifier.padding(vertical = 16.dp)
            )

            when {
                loading -> Text("Cargando dispositivos...")
                error != null -> Text("Error: $error")
                dispositivos.isNotEmpty() -> ProductGrid(
                    dispositivos = dispositivos,
                    onDispositivoClick = { dispositivo ->
                        println("Dispositivo seleccionado: ${dispositivo.nombre}")
                    }
                )
                else -> Text("No se encontraron dispositivos.")
            }
        }
    }
}
