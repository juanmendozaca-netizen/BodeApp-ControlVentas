package com.trabajo01.ui.screens

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
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

data class Compra(
    val insumo: String = "",
    val cantidad: Int = 0,
    val costo: Double = 0.0,
    val total: Double = 0.0
)

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ComprasScreen(navController: NavController) {
    var insumoNombre by remember { mutableStateOf("") }
    var cantidad by remember { mutableStateOf("") }
    var costo by remember { mutableStateOf("") }
    val comprasDelDia = remember { mutableStateListOf<Compra>() }
    val totalCompra = remember(cantidad, costo) {
        val cant = cantidad.toDoubleOrNull() ?: 0.0
        val costoUnit = costo.toDoubleOrNull() ?: 0.0
        cant * costoUnit
    }
    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Compras e Insumos") },
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
                text = "Registrar Compras",
                style = MaterialTheme.typography.headlineMedium,
                fontWeight = FontWeight.Bold

            )
            Spacer(modifier = Modifier.height(16.dp))
            OutlinedTextField(
                value = insumoNombre,
                onValueChange = { insumoNombre = it },
                label = { Text("Insumo/Producto") },
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
                    value = costo,
                    onValueChange = { costo = it },
                    label = { Text("Costo") },
                    modifier = Modifier.weight(1f)
                )
            }
            Spacer(modifier = Modifier.height(8.dp))

            Text(
                text = "Total: S/ %.2f".format(totalCompra),
                style = MaterialTheme.typography.titleMedium
            )
            Spacer(modifier = Modifier.height(8.dp))

            Button(
                onClick = {
                    if (insumoNombre.isNotBlank() && cantidad.isNotBlank() && costo.isNotBlank()) {
                        val compra = Compra(
                            insumo = insumoNombre,
                            cantidad = cantidad.toIntOrNull() ?: 0,
                            costo = costo.toDoubleOrNull() ?: 0.0,
                            total = totalCompra
                        )
                        comprasDelDia.add(0, compra)
                        insumoNombre = ""
                        cantidad = ""
                        costo = ""
                    }
                },
                modifier = Modifier.fillMaxWidth()
            ) {
                Text("Agregar Compra")
            }

            Spacer(modifier = Modifier.height(16.dp))
            Text(
                text = "Compras del día:",
                fontWeight = FontWeight.Bold,
                style = MaterialTheme.typography.titleMedium
            )
            Spacer(modifier = Modifier.height(8.dp))

            LazyColumn(
                verticalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                items(comprasDelDia) { compra ->
                    Card(modifier = Modifier.fillMaxWidth()) {
                        Column(modifier = Modifier.padding(12.dp)) {
                            Text(
                                text = compra.insumo,
                                fontWeight = FontWeight.Bold
                            )
                            Text(
                                text = "${compra.cantidad} x S/ %.2f = S/ %.2f".format(
                                    compra.costo,
                                    compra.total
                                )
                            )
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