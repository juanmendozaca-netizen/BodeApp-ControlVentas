package com.trabajo01.ui.screens

import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.trabajo01.navigation.Screen

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun HomeScreen(navController: NavController) {
    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("BodeApp - Control de Ventas") }
            )
        }
    ) { padding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(padding)
                .padding(16.dp),
            verticalArrangement = Arrangement.Center,
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Text(
                text = "Pantalla Home",
                style = MaterialTheme.typography.headlineMedium
            )
            Spacer(modifier = Modifier.height(24.dp))

            Button(onClick = { navController.navigate(Screen.RegistroProducto.route) }) {
                Text("Ir a Registro Producto")
            }
            Spacer(modifier = Modifier.height(8.dp))

            Button(onClick = { navController.navigate(Screen.Ventas.route) }) {
                Text("Ir a Ventas")
            }
            Spacer(modifier = Modifier.height(8.dp))

            Button(onClick = { navController.navigate(Screen.Compras.route) }) {
                Text("Ir a Compras")
            }
            Spacer(modifier = Modifier.height(8.dp))

            Button(onClick = { navController.navigate(Screen.CierreCaja.route) }) {
                Text("Ir a Cierre de Caja")
            }
        }
    }
}