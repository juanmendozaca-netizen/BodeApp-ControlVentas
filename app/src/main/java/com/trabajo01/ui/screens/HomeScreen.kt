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
                title = { Text("BodeApp - Control de Ventas") },
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = MaterialTheme.colorScheme.primary
                )
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
                style = MaterialTheme.typography.headlineLarge
            )
            Spacer(modifier = Modifier.height(24.dp))

            Button(onClick = { navController.navigate(Screen.RegistroProducto.route) },
                modifier = Modifier.fillMaxWidth()
            ) {
                Text("Registro de Producto")
            }
            Spacer(modifier = Modifier.height(8.dp))

            Button(onClick = { navController.navigate(Screen.Ventas.route) },
                modifier = Modifier.fillMaxWidth()
            ) {
                Text("Ventas")
            }
            Spacer(modifier = Modifier.height(8.dp))

            Button(onClick = { navController.navigate(Screen.Compras.route) },
                modifier = Modifier.fillMaxWidth()
            ) {
                Text("Compras")
            }
            Spacer(modifier = Modifier.height(8.dp))

            Button(onClick = { navController.navigate(Screen.CierreCaja.route) },
                modifier = Modifier.fillMaxWidth()
            ) {
                Text("Cierre de Caja")
            }
        }
    }
}