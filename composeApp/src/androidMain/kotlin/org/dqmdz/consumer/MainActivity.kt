package org.dqmdz.consumer

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.padding
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.Info
import androidx.compose.material.icons.filled.ShoppingCart
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import org.dqmdz.consumer.ui.screens.AboutScreen
import org.dqmdz.consumer.ui.screens.ListarDispositivosScreen
import org.dqmdz.consumer.ui.screens.ListarHistorialVentasScreen
import org.dqmdz.consumer.ui.screens.ModificarCompraScreen
import org.dqmdz.consumer.ui.screens.VerVentasScreen
import org.dqmdz.consumer.viewModel.DispositivoViewModel
import org.dqmdz.consumer.viewModel.VentasViewModel

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            val dispositivoViewModel: DispositivoViewModel = viewModel()
            val ventasViewModel: VentasViewModel = viewModel()

            MainScreen(
                dispositivoViewModel,
                ventasViewModel
            )
        }
    }
}

@Composable
fun MainScreen(
    dispositivoViewModel: DispositivoViewModel,
    ventasViewModel: VentasViewModel
) {
    val navController = rememberNavController()
    val currentRoute by remember { derivedStateOf { navController.currentDestination?.route } }

    Scaffold(
        bottomBar = {
            BottomNavigationBar(navController = navController, currentRoute = currentRoute)
        }
    ) { contentPadding ->
        NavHost(
            navController = navController,
            startDestination = "home",
            modifier = Modifier.padding(contentPadding)
        ) {


            // Home Screen
            composable("home") {
                ListarDispositivosScreen(
                    dispositivoViewModel = dispositivoViewModel,
                    onNavigateToAgregar = { /* Navegar a agregar */ },
                    onNavigateToModificar = { dispositivo ->
                        navController.navigate("modificarDispositivo/${dispositivo.id}")
                    }
                )
            }


            // Modificar Venta Screen
            composable("modificarDispositivo/{dispositivoId}") { backStackEntry ->
                val dispositivoId = backStackEntry.arguments?.getString("dispositivoId")?.toInt()
                val dispositivo = dispositivoViewModel.getDispositivoById(dispositivoId)
                if (dispositivo != null) {
                    ModificarCompraScreen(
                        dispositivoId = dispositivoId!!,
                        dispositivo,
                        dispositivoViewModel = dispositivoViewModel,
                        ventasViewModel,
                        onNavigateBack = {
                            navController.popBackStack()
                }
                    )
                }
            }


            // Ventas Screen
            composable("ventas") {
                ListarHistorialVentasScreen(
                    ventaViewModel = ventasViewModel,
                    onNavigateToDetalle = { venta ->
                        navController.navigate("modificarVenta/${venta.id}") },
                )
            }



            // Modificar Venta Screen
            composable("modificarVenta/{ventaId}") { backStackEntry ->
                val ventaId = backStackEntry.arguments?.getString("ventaId")?.toInt()
                val venta = ventasViewModel.ventas.get(0).find { it.id == ventaId }
                if (venta != null) {
                    VerVentasScreen(
                        venta = venta,
                        onNavigateBack = { navController.popBackStack() }
                    )
                }
            }


            // About Screen
            composable("about") {
                AboutScreen()
            }
        }
    }
}

@Composable
fun BottomNavigationBar(navController: NavHostController, currentRoute: String?) {
    BottomNavigation {
        BottomNavigationItem(
            selected = currentRoute == "home",
            onClick = { navController.navigate("home") },
            label = { Text("Home") },
            icon = { Icon(Icons.Default.Home, contentDescription = "Home") }
        )
        BottomNavigationItem(
            selected = currentRoute == "ventas",
            onClick = { navController.navigate("ventas") },
            label = { Text("Ventas") },
            icon = { Icon(Icons.Default.ShoppingCart, contentDescription = "Ventas") }
        )
        BottomNavigationItem(
            selected = currentRoute == "about",
            onClick = { navController.navigate("about") },
            label = { Text("About") },
            icon = { Icon(Icons.Default.Info, contentDescription = "About") }
        )
    }
}
