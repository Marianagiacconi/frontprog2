package org.dqmdz.consumer.ui.screens

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.material.*
import androidx.compose.runtime.*
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import model.Venta
import org.dqmdz.consumer.ui.componentes.VentaCard
import org.dqmdz.consumer.viewModel.VentasViewModel

@Composable
fun ListarHistorialVentasScreen(
    ventaViewModel: VentasViewModel,
    onNavigateToDetalle: ((Venta) -> Unit)?
) {
    // Estado para manejar si los datos están cargándose
    var isLoading by remember { mutableStateOf(true) }
    var hasError by remember { mutableStateOf(false) }

    // Cargar los datos cuando la pantalla se inicializa
    LaunchedEffect(Unit) {
        try {
            ventaViewModel.loadVentas()
            isLoading = false
        } catch (e: Exception) {
            hasError = true
            isLoading = false
        }
    }

    Scaffold(
        topBar = {
            Column {
                TopAppBar(
                    title = {
                        Text(
                            text = "Dev. Store",
                            modifier = Modifier.fillMaxWidth(),
                            textAlign = TextAlign.Center,
                            color = MaterialTheme.colors.onPrimary
                        )
                    },
                    backgroundColor = MaterialTheme.colors.primary
                )
                Divider(
                    color = MaterialTheme.colors.onPrimary,
                    thickness = 1.dp
                )
                TopAppBar(
                    title = {
                        Text(
                            text = "Historial de Ventas",
                            modifier = Modifier.fillMaxWidth(),
                            textAlign = TextAlign.Center,
                            color = MaterialTheme.colors.onPrimary,
                            fontWeight = FontWeight.Bold
                        )
                    },
                    backgroundColor = MaterialTheme.colors.primary
                )
            }
        },
        content = { paddingValues ->
            when {
                isLoading -> {
                    // Mostrar indicador de carga
                    Box(
                        modifier = Modifier
                            .fillMaxSize()
                            .padding(paddingValues),
                        contentAlignment = Alignment.Center
                    ) {
                        CircularProgressIndicator()
                    }
                }
                hasError -> {
                    // Mostrar mensaje de error
                    Box(
                        modifier = Modifier
                            .fillMaxSize()
                            .padding(paddingValues),
                        contentAlignment = Alignment.Center
                    ) {
                        Text(
                            text = "Ocurrió un error al cargar el historial de ventas. Por favor, intenta nuevamente.",
                            textAlign = TextAlign.Center,
                            color = MaterialTheme.colors.error
                        )
                    }
                }
                ventaViewModel.ventas.isEmpty() -> {
                    // Mostrar mensaje de lista vacía
                    Box(
                        modifier = Modifier
                            .fillMaxSize()
                            .padding(paddingValues),
                        contentAlignment = Alignment.Center
                    ) {
                        Text(
                            text = "No hay ventas registradas en el historial.",
                            textAlign = TextAlign.Center,
                            color = MaterialTheme.colors.onBackground
                        )
                    }
                }
                else -> {
                    LazyVerticalGrid(
                        columns = GridCells.Fixed(2),
                        modifier = Modifier.fillMaxSize(),
                        contentPadding = PaddingValues(16.dp),
                        verticalArrangement = Arrangement.spacedBy(16.dp),
                        horizontalArrangement = Arrangement.spacedBy(16.dp)
                    ) {
                        items(ventaViewModel.ventas.get(0)) { venta ->
                            VentaCard(
                                venta = venta,
                                onEdit = {
                                    onNavigateToDetalle?.invoke(venta)
                                }
                            )
                        }
                    }
                }
            }
        }
    )
}
