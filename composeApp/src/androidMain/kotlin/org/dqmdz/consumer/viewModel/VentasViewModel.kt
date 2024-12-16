package org.dqmdz.consumer.viewModel

import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.snapshots.SnapshotMutableState
import androidx.compose.runtime.snapshots.SnapshotStateList
import androidx.compose.runtime.snapshots.SnapshotStateMap
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import services.fetchVentas
import kotlinx.coroutines.launch
import model.Venta
import model.VentaRequest
import model.VentaResponse
import services.registrarVenta

class VentasViewModel : ViewModel() {

    // Lista local de ventas
    private val _ventas = mutableStateListOf< List<Venta>>()
    val ventas: SnapshotStateList<List<Venta>> get() = _ventas

    // Lista local de venta
    private val _venta = mutableSetOf<VentaResponse>()
    val venta: MutableSet<VentaResponse> get() = _venta

    private val _isLoading = MutableStateFlow(false)
    val isLoading: StateFlow<Boolean> = _isLoading

    private val _errorMessage = MutableStateFlow<String?>(null)
    val errorMessage: StateFlow<String?> = _errorMessage

    private val _ventaExitosa = MutableStateFlow<Boolean?>(null)
    val ventaExitosa: StateFlow<Boolean?> = _ventaExitosa

    // Cargar ventas desde el backend
    fun loadVentas() {
        _isLoading.value = true
        viewModelScope.launch {
            try {
                val ventasCargadas = fetchVentas() // Llama a la función fetchVentas de services
                _ventas.clear()
                _ventas.addAll(listOf(ventasCargadas))
                _errorMessage.value = null
            } catch (e: Exception) {
                _errorMessage.value = "Error al cargar las ventas: ${e.message}"
            } finally {
                _isLoading.value = false
            }
        }
    }

    fun vender(venta: VentaRequest) {
        _isLoading.value = true
        viewModelScope.launch {
            try {
                val venta = registrarVenta(venta) // Llama a la función fetchVentas de services
                _venta.clear()
                _venta.addAll(listOf(venta))
                _errorMessage.value = null
                _isLoading.value = false
            } catch (e: Exception) {
                _errorMessage.value = "Error al cargar las ventas: ${e.message}"
            } finally {
                _isLoading.value = false
            }
        }
    }

}
