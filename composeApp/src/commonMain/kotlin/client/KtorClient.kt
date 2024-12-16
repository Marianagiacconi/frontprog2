package client

import io.ktor.client.*
import io.ktor.client.call.body
import io.ktor.client.plugins.*
import io.ktor.client.plugins.auth.*
import io.ktor.client.plugins.auth.providers.*
import io.ktor.client.plugins.contentnegotiation.*
import io.ktor.client.plugins.logging.*
import io.ktor.client.request.*
import io.ktor.client.statement.*
import io.ktor.http.*
import io.ktor.serialization.kotlinx.json.*
import kotlinx.coroutines.runBlocking
import kotlinx.serialization.Serializable
import kotlinx.serialization.json.*
import model.Dispositivo
import java.io.File
import java.time.Instant

// Definir la base URL en un solo lugar
 const val BASE_URL = "http://192.168.0.122:8081"

// Instancia de Json configurada para pretty print
val jsonPretty = Json {
    prettyPrint = true
    isLenient = true
    ignoreUnknownKeys = true
}

// Clase para gestionar el token
class TokenManager(private val username: String, private val password: String) {
    private var token: String? = null
    private var tokenExpirationTime: Instant = Instant.EPOCH

    private val tokenFile = File("token.txt")
    private val authClient = HttpClient {
        install(ContentNegotiation) {
            json(jsonPretty)
        }
        install(Logging) {
            logger = Logger.DEFAULT
            level = LogLevel.ALL
        }
    }

    init {
        loadToken()
    }

    private fun loadToken() {
        if (tokenFile.exists()) {
            val lines = tokenFile.readLines()
            if (lines.size >= 2) {
                token = lines[0]
                tokenExpirationTime = Instant.ofEpochMilli(lines[1].toLong())
            }
        }
    }

    private fun saveToken() {
        tokenFile.writeText("$token\n${tokenExpirationTime.toEpochMilli()}")
    }

    suspend fun getToken(): String {
        if (token == null || Instant.now().isAfter(tokenExpirationTime)) {
            fetchToken()
        }
        return token!!
    }

    suspend fun fetchToken() {
        println("Obteniendo nuevo token de autenticación...")
        val response: HttpResponse = authClient.post("$BASE_URL/api/authenticate") {
            contentType(ContentType.Application.Json)
            setBody(AuthRequest(username, password, rememberMe = true))
        }

        if (response.status.isSuccess()) {
            val tokenResponse = response.body<TokenResponse>()
            token = tokenResponse.id_token
            // Supongamos que el token expira en 1 hora
            tokenExpirationTime = Instant.now().plusSeconds(3600)
            println("Token obtenido y guardado correctamente.")
        } else {
            val errorBody = response.bodyAsText()
            println("Error al obtener el token: ${response.status}. Respuesta: $errorBody")
            throw Exception("Error al obtener el token: ${response.status}")
        }
    }

    @Serializable
    data class AuthRequest(
        val username: String,
        val password: String,
        val rememberMe: Boolean
    )

    @Serializable
    data class TokenResponse(
        val id_token: String
    )
}

// Inicializar el TokenManager
val tokenManager = TokenManager("admin", "admin")

// Configurar el cliente HTTP
val client = HttpClient {
    install(Logging) {
        logger = Logger.DEFAULT
        level = LogLevel.BODY
    }
    install(ContentNegotiation) {
        json(jsonPretty)
    }
    install(Auth) {
        bearer {
            loadTokens {
                BearerTokens(
                    accessToken = tokenManager.getToken(),
                    refreshToken = "eyJhbGciOiJIUzUxMiJ9.eyJzdWIiOiJtbS5naWFjY29uaSIsImV4cCI6MTc0MDY3MzEzNSwiYXV0aCI6IlJPTEVfVVNFUiIsImlhdCI6MTczMjAzMzEzNX0.-8YrH_8R59Tb28idgC2sxJMVP8QWYMMnDxBxp6n2ooFEhBTnxlnPuz49VMehO3XF1zvIXbqFmWQnKBBUhwEf3g"
                )
            }
            // Importante: Configura sendWithoutRequest como true para enviar el token en todas las solicitudes
            sendWithoutRequest { true }
        }
    }
    defaultRequest {
        // Añadir encabezados o configuraciones por defecto si es necesario
    }
}
