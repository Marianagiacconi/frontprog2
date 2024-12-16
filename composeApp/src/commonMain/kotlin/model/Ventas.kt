package model

import kotlinx.datetime.Instant
import kotlinx.serialization.Serializable
import java.util.Date

@Serializable
data class Venta(
    val id: Int,
    val fechaVenta: String, // ISO-8601 formatted string
    val precioFinal: Double,
    val user: UserResponse
)


@Serializable
data class VentaResponse(
    val id: Int,
    val fechaVenta: String,
    val precioFinal: Double,
    val user : UserResponse
)

@Serializable
data class UserResponse(
    val id: Int,
    val login: String?,

)

@Serializable
data class VentaRequest(
    val userId: Int,
    val idDispositivo: Int,
    val personalizaciones: List<OpcionSeleccionada>,
    val adicionales: List<AdicionalRequest>,
    val precioFinal: Double,
    val fechaVenta: String
)

@Serializable
data class AdicionalRequest(
    val id: Int,
    val precio: Double
)


@Serializable
data class OpcionSeleccionada(
    val id: Int,
    val precioAdicional: Double
)