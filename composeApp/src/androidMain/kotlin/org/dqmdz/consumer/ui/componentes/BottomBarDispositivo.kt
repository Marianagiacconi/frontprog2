package org.dqmdz.consumer.ui.componentes

import androidx.compose.material.BottomNavigation
import androidx.compose.material.BottomNavigationItem
import androidx.compose.material.Icon
import androidx.compose.material.Text
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.List
import androidx.compose.runtime.Composable
import androidx.navigation.NavController


@Composable
fun BottomBarDispositivo(navController: NavController, onBackToHome: () -> Unit) {
    BottomNavigation {
        BottomNavigationItem(
            icon = { Icon(Icons.Filled.Add, contentDescription = "Agregar dispositivo") },
            label = { Text("Agregar") },
            selected = false,
            onClick = { navController.navigate("agregarDispositivo") }
        )
        BottomNavigationItem(
            icon = { Icon(Icons.Filled.List, contentDescription = "Listar dispositivos") },
            label = { Text("Listar") },
            selected = false,
            onClick = {
                // Volver a la pantalla de dispositivos para listar
                navController.navigate("dispositivos") {
                    popUpTo("dispositivos") { inclusive = true }
                }
            }
        )
        BottomNavigationItem(
            icon = { Icon(Icons.Filled.ArrowBack, contentDescription = "Volver") },
            label = { Text("Volver") },
            selected = false,
            onClick = { onBackToHome() }
        )
    }
}
