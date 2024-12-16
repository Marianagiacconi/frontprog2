package services

import client.BASE_URL
import client.client
import io.ktor.client.*
import io.ktor.client.request.*
import io.ktor.client.call.*
import io.ktor.client.plugins.contentnegotiation.*
import io.ktor.client.plugins.logging.*
import io.ktor.client.statement.HttpResponse
import io.ktor.client.statement.bodyAsText
import io.ktor.http.*
import io.ktor.serialization.kotlinx.json.*
import kotlinx.serialization.builtins.ListSerializer
import kotlinx.serialization.json.Json
import model.Adicional
import model.Caracteristica
import model.Dispositivo
import model.Personalizacion
import model.Venta
import model.VentaRequest
import model.VentaResponse
import java.io.IOException


//region VENTAS

// Función para obtener todos las ventas

suspend fun fetchVentas(): List<Venta> {
    val url = "$BASE_URL/api/ventas"

    return try {
        val response: HttpResponse = client.get(url) {
            accept(ContentType.Application.Json)
        }

        if (response.status.isSuccess()) {
            val ventas: List<Venta> = Json.decodeFromString(
                ListSerializer(Venta.serializer()),
                response.bodyAsText()
            )
            ventas
        } else {
            val errorBody = response.bodyAsText()
            println("Error al obtener los datos: ${response.status}. Respuesta: $errorBody")
            emptyList()
        }
    } catch (e: IOException) {
        println("Error de red: ${e.message}")
        emptyList()
    } catch (e: Exception) {
        println("Error al obtener los datos: ${e.message}")
        emptyList()
    }
}

// Funcion para postear venta
suspend fun registrarVenta(venta: VentaRequest): VentaResponse {
    val url = "$BASE_URL/api/ventas/vender"

    return try {
        client.post(url) {
            contentType(ContentType.Application.Json)
            setBody(venta)
        }.body()
    } catch (e: IOException) {
        throw Exception("Error de red al registrar la venta: ${e.message}")
    } catch (e: Exception) {
        print("Error al registrar la venta: ${e.message}")
        throw Exception("Error al registrar la venta: ${e.message}")
    }
}
//endregion

//region DEVICES

// Función que consume el backend para obtener todos los dispositivos
suspend fun fetchDevices(): List<Dispositivo> {
    val url = "$BASE_URL/api/dispositivos"
    return try {
        val response: HttpResponse = client.get(url) {
            accept(ContentType.Application.Json)
        }

        if (response.status.isSuccess()) {
            val dispositivos: List<Dispositivo> = Json.decodeFromString(
                ListSerializer(Dispositivo.serializer()),
                response.bodyAsText()
            )
            dispositivos
        } else {
            val errorBody = response.bodyAsText()
            println("Error al obtener los datos: ${response.status}. Respuesta: $errorBody")
            emptyList()
        }
    } catch (e: IOException) {
        println("Error de red: ${e.message}")
        emptyList()
    } catch (e: Exception) {
        println("Error al obtener los datos: ${e.message}")
        emptyList()
    }
}

//endregion