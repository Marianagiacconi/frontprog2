package org.dqmdz.consumer.ui.screens

import org.dqmdz.consumer.ui.componentes.DispositivoCard
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.material.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import model.Dispositivo
import org.dqmdz.consumer.viewModel.DispositivoViewModel

@Composable
fun ListarDispositivosScreen(
    dispositivoViewModel: DispositivoViewModel,
    onNavigateToAgregar: (() -> Unit)?,
    onNavigateToModificar: ((Dispositivo) -> Unit)?
) {
    val devices by dispositivoViewModel.dispositivos.collectAsState() // Observa los cambios del StateFlow

    // Estado para manejar si los datos están cargándose
    var isLoading by remember { mutableStateOf(true) }
    var hasError by remember { mutableStateOf(false) }

    // Cargar los datos cuando la pantalla se inicializa
    LaunchedEffect(Unit) {
        try {
            dispositivoViewModel.loadDispositivos()
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
                            text = "Ocurrió un error al cargar los dispositivos. Por favor, intenta nuevamente.",
                            textAlign = TextAlign.Center,
                            color = MaterialTheme.colors.error
                        )
                    }
                }
                devices.isEmpty() -> {
                    // Mostrar indicador de carga
                    Box(
                        modifier = Modifier
                            .fillMaxSize()
                            .padding(paddingValues),
                        contentAlignment = Alignment.Center,
                    ) {
                        CircularProgressIndicator()
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
                        items(dispositivoViewModel.dispositivos.value) { dispositivo ->
                            DispositivoCard(
                                dispositivo = dispositivo,
                                onEdit = {
                                    onNavigateToModificar?.invoke(dispositivo)
                                }
                            )
                        }
                    }
                }
            }
        }
    )
}
