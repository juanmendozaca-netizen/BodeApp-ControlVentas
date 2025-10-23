package com.trabajo01.navigation

sealed class Screen(val route: String) {
    object Home : Screen("home")
    object RegistroProducto : Screen("registro_producto")
    object Ventas : Screen("ventas")
    object Compras : Screen("compras")
    object CierreCaja : Screen("cierre_caja")
}