package org.dqmdz.consumer.ui.screens

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.ui.text.style.TextAlign
import model.Dispositivo
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.KeyboardArrowDown
import androidx.compose.material.icons.filled.KeyboardArrowUp
import androidx.compose.material.icons.filled.Remove
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import client.tokenManager
import kotlinx.coroutines.launch
import model.AdicionalRequest
import model.OpcionSeleccionada
import model.VentaRequest
import org.dqmdz.consumer.viewModel.DispositivoViewModel
import org.dqmdz.consumer.viewModel.VentasViewModel
import java.time.ZoneId
import java.time.ZonedDateTime

@Composable
fun ModificarCompraScreen(
    dispositivoId: Int,
    dispositivo: Dispositivo,
    dispositivoViewModel: DispositivoViewModel,
    ventasViewModel: VentasViewModel,
    onNavigateBack: () -> Unit
) {

    // Variables del ViewModel
    val isLoading by ventasViewModel.isLoading.collectAsState(initial = false)
    val errorMessage by ventasViewModel.errorMessage.collectAsState(initial = null)
    val ventaExitosa by ventasViewModel.ventaExitosa.collectAsState(initial = null)

    // Pantalla de carga
    if (isLoading) {
        Box(
            modifier = Modifier
                .fillMaxSize()
                .background(Color(0x80000000)),
            contentAlignment = Alignment.Center
        ) {
            CircularProgressIndicator()
        }
    }



    ventaExitosa?.let { success ->
        if (success) {
            LaunchedEffect(Unit) {
                onNavigateBack()
            }
        }
    }
    //region Mutables
    //Checkboxs
    val adicionalesChecked = remember { mutableStateMapOf<Int, Boolean>() }
    val selectedOptions = remember { mutableStateMapOf<Int, Int>() } // key: ID de la personalización, value: índice de la opción seleccionada
    //Quantity of the same sell
    var quantity by remember { mutableStateOf(1) }
    // Expansiones
    var isAdicionalesExpanded by remember { mutableStateOf(false) }
    var isCaracteristicasExpanded by remember { mutableStateOf(false) }
    var isPersonalizacionesExpanded by remember { mutableStateOf(false) }

    // Estado de selección de adicionales (Checkbox)
    val adicionalesSeleccionados = remember {
        mutableStateMapOf<Int, Boolean>().apply {
            dispositivo.adicionales.forEach { adicional ->
                this[adicional.id] = false
            }
        }
    }

    //endregion


    //region Calcular precios

    // Función para calcular el precio total
    fun calcularPrecioTotal(): Double {
        val precioBase = dispositivo.precioBase

        // Precio de adicionales seleccionados
        val precioAdicionales = dispositivo.adicionales.filter { adicionalesSeleccionados[it.id] == true }
            .sumOf { adicional ->
                if (adicional.precioGratis > -1 && (precioBase >= adicional.precioGratis)) {
                    0.0 // Este adicional es gratis
                } else {
                    adicional.precio // Precio normal
                }
            }

        // Precio de las opciones seleccionadas en personalizaciones
        val precioPersonalizaciones = dispositivo.personalizaciones.mapNotNull { personalizacion ->
            val opcionIdx = selectedOptions[personalizacion.id] // Obtiene el índice de la opción seleccionada
            opcionIdx?.let { personalizacion.opciones[it].precioAdicional }
        }.sum()

        return (precioBase + precioAdicionales + precioPersonalizaciones) * quantity
    }

    //INCIAR LA VISTA CON EL PRECIO
    var precioTotal by remember { mutableStateOf(calcularPrecioTotal()) }

    fun actualizarPrecio() {
        precioTotal = calcularPrecioTotal()
    }
    //endregion


    //region View

    // Mostrar el formulario para editar el dispositivo
    LazyColumn(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp),
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        items(1) {
            Spacer(modifier = Modifier.height(24.dp))

            Text(
                text = "Nombre:",
                fontSize = 16.sp,
                color = Color.Black,
                fontWeight = FontWeight.Bold
            )
            Text(
                text = dispositivo.nombre
            )

            Spacer(modifier = Modifier.height(24.dp))

            Text(
                text = "Descripción:",
                fontSize = 16.sp,
                color = Color.Black,
                fontWeight = FontWeight.Bold
            )
            Text(
                text = dispositivo.descripcion
            )

            Spacer(modifier = Modifier.height(24.dp))

            Text(
                text = "Cantidad:",
                fontSize = 16.sp,
                color = Color.Black,
                fontWeight = FontWeight.Bold
            )

            Row(verticalAlignment = Alignment.CenterVertically) {
                IconButton(onClick = {
                    if (quantity > 1) {
                        quantity--
                        actualizarPrecio()
                    }
                }) {
                    Icon(Icons.Default.Remove, contentDescription = "Disminuir cantidad")
                }
                Text(quantity.toString())
                IconButton(onClick = {
                    quantity++
                    actualizarPrecio()
                }) {
                    Icon(Icons.Default.Add, contentDescription = "Aumentar cantidad")
                }
            }

            Spacer(modifier = Modifier.height(16.dp))

            // PERSONALIZACIONES
            Column(modifier = Modifier.fillMaxWidth()) {
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .clickable { isPersonalizacionesExpanded = !isPersonalizacionesExpanded }
                        .padding(8.dp),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text(
                        text = "Personalizaciones:",
                        fontSize = 16.sp,
                        color = Color.Black,
                        fontWeight = FontWeight.Bold
                    )
                    Icon(
                        imageVector = if (isPersonalizacionesExpanded) Icons.Default.KeyboardArrowUp else Icons.Default.KeyboardArrowDown,
                        contentDescription = "Expandir/Colapsar"
                    )
                }

                AnimatedVisibility(visible = isPersonalizacionesExpanded) {
                    if (dispositivo.personalizaciones.isNotEmpty()) {
                        Column(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(8.dp)
                        ) {
                            dispositivo.personalizaciones.forEach { personalizacion ->
                                Column(
                                    modifier = Modifier
                                        .fillMaxWidth()
                                        .padding(vertical = 8.dp)
                                ) {
                                    Text(
                                        text = personalizacion.nombre,
                                        fontSize = 14.sp,
                                        fontWeight = FontWeight.Bold,
                                        color = Color.Black
                                    )
                                    Text(
                                        text = personalizacion.descripcion,
                                        fontSize = 12.sp,
                                        color = Color.Gray
                                    )

                                    personalizacion.opciones.forEachIndexed { index, opcion ->
                                        Row(
                                            modifier = Modifier
                                                .fillMaxWidth()
                                                .padding(vertical = 4.dp),
                                            verticalAlignment = Alignment.CenterVertically
                                        ) {
                                            RadioButton(
                                                selected = selectedOptions[personalizacion.id] == index,
                                                onClick = {
                                                    selectedOptions[personalizacion.id] = index
                                                    actualizarPrecio()
                                                }
                                            )
                                            Text(
                                                text = opcion.nombre,
                                                fontSize = 14.sp,
                                                color = Color.Black
                                            )
                                            Text(
                                                text = " (+${String.format("%.2f", opcion.precioAdicional)})",
                                                fontSize = 12.sp,
                                                color = Color.Gray
                                            )
                                        }
                                    }
                                }
                            }
                        }
                    } else {
                        Text(
                            text = "No hay personalizaciones disponibles.",
                            fontSize = 14.sp,
                            color = Color.Gray
                        )
                    }
                }
            }

            // ADICIONALES
            Column(modifier = Modifier.fillMaxWidth()) {
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .clickable { isAdicionalesExpanded = !isAdicionalesExpanded }
                        .padding(8.dp),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text(
                        text = "Adicionales:",
                        fontSize = 16.sp,
                        color = Color.Black,
                        fontWeight = FontWeight.Bold
                    )
                    Icon(
                        imageVector = if (isAdicionalesExpanded) Icons.Default.KeyboardArrowUp else Icons.Default.KeyboardArrowDown,
                        contentDescription = "Expandir/Colapsar"
                    )
                }

                AnimatedVisibility(visible = isAdicionalesExpanded) {
                    if (dispositivo.adicionales.isNotEmpty()) {
                        Column(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(8.dp)
                        ) {
                            dispositivo.adicionales.chunked(2).forEach { fila ->
                                Row(
                                    modifier = Modifier.fillMaxWidth(),
                                    horizontalArrangement = Arrangement.spacedBy(8.dp)
                                ) {
                                    fila.forEach { adicional ->
                                        Surface(
                                            modifier = Modifier
                                                .weight(1f)
                                                .padding(8.dp)
                                                .shadow(4.dp, shape = RoundedCornerShape(8.dp))
                                                .background(Color.White, shape = RoundedCornerShape(8.dp)),
                                            shape = RoundedCornerShape(8.dp),
                                            color = Color.White
                                        ) {
                                            Column(
                                                modifier = Modifier.padding(16.dp),
                                                horizontalAlignment = Alignment.Start
                                            ) {
                                                val isChecked = adicionalesSeleccionados[adicional.id] ?: false

                                                Row(
                                                    modifier = Modifier
                                                        .fillMaxWidth()
                                                        .padding(vertical = 4.dp),
                                                    verticalAlignment = Alignment.CenterVertically
                                                ) {
                                                    Checkbox(
                                                        checked = isChecked,
                                                        onCheckedChange = {
                                                            adicionalesSeleccionados[adicional.id] = !isChecked
                                                            actualizarPrecio()
                                                            adicionalesChecked[adicional.id] = isChecked
                                                        }
                                                    )
                                                    Text(
                                                        text = adicional.nombre,
                                                        fontSize = 14.sp,
                                                        color = Color.Black
                                                    )
                                                }
                                                Text(
                                                    text = "${adicional.descripcion} (+${String.format("%.2f", adicional.precio)})",
                                                    fontSize = 12.sp,
                                                    color = Color.Gray
                                                )
                                            }
                                        }
                                    }
                                    if (fila.size < 2) {
                                        Spacer(modifier = Modifier.weight(1f))
                                    }
                                }
                            }
                        }
                    } else {
                        Text(
                            text = "No hay adicionales disponibles.",
                            fontSize = 14.sp,
                            color = Color.Gray
                        )
                    }
                }
            }

            // CARACTERISTICAS
            Column(modifier = Modifier.fillMaxWidth()) {
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .clickable { isCaracteristicasExpanded = !isCaracteristicasExpanded }
                        .padding(8.dp),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text(
                        text = "Características:",
                        fontSize = 16.sp,
                        color = Color.Black,
                        fontWeight = FontWeight.Bold
                    )
                    Icon(
                        imageVector = if (isCaracteristicasExpanded) Icons.Default.KeyboardArrowUp else Icons.Default.KeyboardArrowDown,
                        contentDescription = "Expandir/Colapsar"
                    )
                }

                AnimatedVisibility(visible = isCaracteristicasExpanded) {
                    if (dispositivo.caracteristicas.isNotEmpty()) {
                        Column(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(8.dp),
                            verticalArrangement = Arrangement.spacedBy(8.dp)
                        ) {
                            dispositivo.caracteristicas.forEach { caracteristica ->
                                Surface(
                                    modifier = Modifier
                                        .fillMaxWidth()
                                        .shadow(4.dp, shape = RoundedCornerShape(8.dp))
                                        .background(Color.White, shape = RoundedCornerShape(8.dp))
                                        .padding(8.dp),
                                    shape = RoundedCornerShape(8.dp),
                                    color = Color.White
                                ) {
                                    Column(
                                        modifier = Modifier.padding(16.dp),
                                        horizontalAlignment = Alignment.Start
                                    ) {
                                        Text(
                                            text = caracteristica.nombre,
                                            fontSize = 14.sp,
                                            fontWeight = FontWeight.Bold,
                                            color = Color.Black
                                        )
                                        Spacer(modifier = Modifier.height(4.dp))
                                        Text(
                                            text = caracteristica.descripcion,
                                            fontSize = 12.sp,
                                            color = Color.Gray
                                        )
                                    }
                                }
                            }
                        }
                    } else {
                        Text(
                            text = "No hay características disponibles.",
                            fontSize = 14.sp,
                            color = Color.Gray
                        )
                    }
                }
            }


            Spacer(modifier = Modifier.height(40.dp))


            // Precio total
            Text(
                text = "Precio total: ${String.format("%.2f", precioTotal)} ${dispositivo.moneda}",
                fontSize = 18.sp,
                fontWeight = FontWeight.Bold,
                color = Color.Black
            )

            val scope = rememberCoroutineScope()

// Botón comprar
            Button(
                onClick = {
                    scope.launch {
                        // Crear una lista de personalizaciones seleccionadas
                        val personalizacionesRequest = dispositivo.personalizaciones.mapNotNull { personalizacion ->
                            val opcionIdx = selectedOptions[personalizacion.id]
                            opcionIdx?.let {
                                OpcionSeleccionada(
                                    id = personalizacion.id,
                                   precioAdicional = personalizacion.opciones[opcionIdx].precioAdicional
                                )
                            }
                        }

                        // Crear una lista de adicionales seleccionados
                        val adicionalesRequest = dispositivo.adicionales.filter { adicionalesSeleccionados[it.id] == true }
                            .map { adicional ->
                                AdicionalRequest(
                                    id = adicional.id,
                                    precio = adicional.precio,
                                )
                            }

                        // Calcular el precio final
                        val precioFinal = calcularPrecioTotal()

                        // Generar la fecha actual en formato adecuado
                        val fechaVenta = ZonedDateTime.now(ZoneId.of("America/Argentina/Buenos_Aires"))

                        // Llamar a getToken dentro de la corrutina
                        val token = tokenManager.getToken()


                        // Crear un objeto VentaRequest con los datos recopilados
                        val dispositivoActualizado = VentaRequest(
                            userId = 1,
                            idDispositivo = dispositivoId,
                            personalizaciones = personalizacionesRequest,
                            adicionales = adicionalesRequest,
                            precioFinal = precioFinal,
                            fechaVenta = fechaVenta.toString()
                        )

                        // Llamar a la función para postear la venta
                        ventasViewModel.vender(dispositivoActualizado)


                    }
                },
                modifier = Modifier.fillMaxWidth()
            ) {
                Text("Guardar Cambios")
            }

            Button(
                onClick = {
                    onNavigateBack()
                },
                modifier = Modifier.fillMaxWidth()
            ) {
                Text("<- Cancelar")
            }




        }
    }

    //endregion
}
