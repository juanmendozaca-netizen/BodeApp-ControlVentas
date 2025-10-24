package com.trabajo01.navigation

import androidx.compose.runtime.Composable
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import com.trabajo01.ui.screens.CierreCajaScreen
import com.trabajo01.ui.screens.ComprasScreen
import com.trabajo01.ui.screens.HomeScreen
import com.trabajo01.ui.screens.RegistroProductoScreen
import com.trabajo01.ui.screens.VentasScreen

sealed class Screens(val route: String) {
    object Home : Screen("home")
    object RegistroProducto : Screen("registro_producto")
    object Ventas : Screen("ventas")
    object Compras : Screen("compras")
    object CierreCaja : Screen("cierre_caja")
}
@Composable
fun NavGraph(navController: NavHostController) {
    NavHost(
        navController = navController,
        startDestination = Screen.Home.route
    ) {
        composable(Screen.Home.route) {
            HomeScreen(navController = navController)
        }
        composable(Screen.RegistroProducto.route) {
            RegistroProductoScreen(navController = navController)
        }
        composable(Screen.Ventas.route) {
            VentasScreen(navController = navController)
        }
        composable(Screen.Compras.route) {
            ComprasScreen(navController = navController)
        }
        composable(Screen.CierreCaja.route) {
            CierreCajaScreen(navController = navController)
        }
    }
}