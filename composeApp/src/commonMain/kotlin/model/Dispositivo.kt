package model

import kotlinx.serialization.Serializable
import kotlinx.serialization.SerialName

@Serializable
data class Dispositivo(
    val id: Int,
    val codigo: String,
    val nombre: String,
    val descripcion: String,
    val precioBase: Double,
    val moneda: String,
    val caracteristicas: List<Caracteristica>,
    val personalizaciones: List<Personalizacion>,
    val adicionales: List<Adicional>
)

@Serializable
data class Caracteristica(
    val id: Int,
    val nombre: String,
    val descripcion: String,
    val dispositivo: Nothing? = null
    )

@Serializable
data class Personalizacion(
    val id: Int,
    val nombre: String,
    val descripcion: String,
    val opciones: List<Opcion>,
    val dispositivo: String? = null

)

@Serializable
data class Opcion(
    val id: Int,
    val codigo: String,
    val nombre: String,
    val descripcion: String,
    val precioAdicional: Double,
    val personalizacion: Nothing? = null

)

@Serializable
data class Adicional(
    val id: Int,
    val nombre: String,
    val descripcion: String,
    val precio: Double,
    val precioGratis: Double,
    val dispositivos : List<Dispositivo> = emptyList()
)

