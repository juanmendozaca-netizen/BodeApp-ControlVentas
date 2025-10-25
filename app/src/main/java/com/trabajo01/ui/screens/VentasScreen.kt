package com.trabajo01.ui.screens

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.Add
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.setValue
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController

data class Venta(
    val producto: String = "",
    val cantidad: Int = 0,
    val precioUnitario: Double = 0.0,
    val total: Double = 0.0,
    val hora: String = ""
)

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun VentasScreen(navController: NavController) {
    var productoNombre by remember { mutableStateOf("") }
    var cantidad by remember { mutableStateOf("") }
    var precioUnitario by remember { mutableStateOf("") }
    val ventasDelDia = remember { mutableStateListOf<Venta>() }
    val subtotal = remember(cantidad, precioUnitario) {
        val cant = cantidad.toDoubleOrNull() ?: 0.0
        val precio = precioUnitario.toDoubleOrNull() ?: 0.0
        cant * precio
    }
    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Ventas") },
                navigationIcon = {
                    IconButton(onClick = { navController.popBackStack() }) {
                        Icon(
                            imageVector = Icons.AutoMirrored.Filled.ArrowBack,
                            contentDescription = "Volver"
                        )
                    }
                }
            )
        }
    ) { padding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(padding)
                .padding(16.dp),
            verticalArrangement = Arrangement.Top,
            horizontalAlignment = Alignment.Start
        ) {
            Text(
                text = "Registrar Venta",
                style = MaterialTheme.typography.headlineMedium,
                fontWeight = FontWeight.Bold
            )
            OutlinedTextField(
                value = productoNombre,
                onValueChange = { productoNombre = it },
                label = { Text("Producto") },
                modifier = Modifier.fillMaxWidth()
            )
            Spacer(modifier = Modifier.height(8.dp))

            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                OutlinedTextField(
                    value = cantidad,
                    onValueChange = { cantidad = it },
                    label = { Text("Cantidad") },
                    modifier = Modifier.weight(1f)
                )
                OutlinedTextField(
                    value = precioUnitario,
                    onValueChange = { precioUnitario = it },
                    label = { Text("Precio") },
                    modifier = Modifier.weight(1f)
                )
            }
            Spacer(modifier = Modifier.height(8.dp))

            Text("Subtotal: S/ %.2f".format(subtotal))
            Spacer(modifier = Modifier.height(8.dp))

            Button(
                onClick = {
                    if (productoNombre.isNotBlank() && cantidad.isNotBlank() && precioUnitario.isNotBlank()) {
                        ventasDelDia.add(0, Venta(productoNombre, cantidad.toInt(), precioUnitario.toDouble(), subtotal))
                        productoNombre = ""
                        cantidad = ""
                        precioUnitario = ""
                    }
                },
                modifier = Modifier.fillMaxWidth()
            ) {
                Text("Agregar Venta")
            }

            Spacer(modifier = Modifier.height(16.dp))
            Text("Ventas del día:", fontWeight = FontWeight.Bold)  // ⬅️ CAMBIAR el texto anterior
            Spacer(modifier = Modifier.height(8.dp))

            // ⬇️ AGREGAR: Lista de ventas
            LazyColumn(
                verticalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                items(ventasDelDia) { venta ->
                    Card(modifier = Modifier.fillMaxWidth()) {
                        Column(modifier = Modifier.padding(12.dp)) {
                            Text(venta.producto, fontWeight = FontWeight.Bold)
                            Text("${venta.cantidad} x S/ %.2f = S/ %.2f".format(venta.precioUnitario, venta.total))
                        }
                    }
                }
            }

            // ⬇️ MANTENER tu botón original al final
            Spacer(modifier = Modifier.height(24.dp))
            Button(onClick = { navController.popBackStack() }) {
                Text("Volver al Home")
            }
        }
    }
}