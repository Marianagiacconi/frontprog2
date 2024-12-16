package org.dqmdz.consumer.viewModel

import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import model.Adicional
import model.Caracteristica
import model.Dispositivo
import model.Personalizacion
import services.fetchDevices

class DispositivoViewModel : ViewModel() {

    // Lista local de dispositivos
    private val _dispositivos = MutableStateFlow<List<Dispositivo>>(emptyList())
    val dispositivos: StateFlow<List<Dispositivo>> get() = _dispositivos
    private val _caracteristicas = MutableStateFlow<List<Caracteristica>>(emptyList())
    val caracteristicas: StateFlow<List<Caracteristica>> = _caracteristicas

    private val _adicionales = MutableStateFlow<List<Adicional>>(emptyList())
    val adicionales: StateFlow<List<Adicional>> = _adicionales

    private val _customizations = MutableStateFlow<List<Personalizacion>>(emptyList())
    val customizations: StateFlow<List<Personalizacion>> = _customizations

    val opcionesSeleccionadas = mutableMapOf<Int, Int>()
    val adicionalesSeleccionados = mutableSetOf<Int>()

    // Estado de carga y errores
    val isLoading = mutableStateOf(false)
    val errorMessage = mutableStateOf<String?>(null)

    // Cargar dispositivos desde el backend
    fun loadDispositivos() {
        isLoading.value = true
        viewModelScope.launch {
            try {
                _dispositivos.value = fetchDevices()
            } catch (e: Exception) {
                errorMessage.value = "Error al cargar los dispositivos: ${e.message}"
            } finally {
                isLoading.value = false
            }
        }
    }

    // Obtener un dispositivo por ID
    fun getDispositivoById(id: Int?): Dispositivo? {
        return _dispositivos.value.find { it.id == id }
    }

    fun seleccionarOpcion(caracteristicaId: Int, opcionId: Int) {
        opcionesSeleccionadas[caracteristicaId] = opcionId
    }

    fun opcionSeleccionada(caracteristicaId: Int): Int? {
        return opcionesSeleccionadas[caracteristicaId]
    }

    fun toggleAdicional(adicionalId: Int) {
        if (adicionalesSeleccionados.contains(adicionalId)) {
            adicionalesSeleccionados.remove(adicionalId)
        } else {
            adicionalesSeleccionados.add(adicionalId)
        }
    }

    fun adicionalSeleccionado(adicionalId: Int): Boolean {
        return adicionalesSeleccionados.contains(adicionalId)
    }

}
