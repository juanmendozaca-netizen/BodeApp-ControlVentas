package com.trabajo01.ui.screens

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.trabajo01.data.AppDatabase
import com.trabajo01.data.entity.Compra
import kotlinx.coroutines.launch
import java.text.SimpleDateFormat
import java.util.*

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ComprasScreen(navController: NavController) {
    val context = LocalContext.current
    val database = remember { AppDatabase.getDatabase(context) }
    val compraDao = database.compraDao()
    val scope = rememberCoroutineScope()

    var insumoNombre by remember { mutableStateOf("") }
    var cantidad by remember { mutableStateOf("") }
    var costo by remember { mutableStateOf("") }

    // ✅ Mantener flujo estable para evitar recomposiciones
    val comprasFlow = remember { compraDao.obtenerTodasLasCompras() }
    val comprasDelDia by comprasFlow.collectAsState(initial = emptyList())

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
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            Text(
                text = "Registrar Compras",
                style = MaterialTheme.typography.headlineMedium,
                fontWeight = FontWeight.Bold
            )

            OutlinedTextField(
                value = insumoNombre,
                onValueChange = { insumoNombre = it },
                label = { Text("Insumo/Producto") },
                placeholder = { Text("Ej: Harina, Azúcar, etc.") },
                modifier = Modifier.fillMaxWidth()
            )

            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                OutlinedTextField(
                    value = cantidad,
                    onValueChange = { cantidad = it },
                    label = { Text("Cantidad") },
                    placeholder = { Text("0") },
                    modifier = Modifier.weight(1f)
                )

                OutlinedTextField(
                    value = costo,
                    onValueChange = { costo = it },
                    label = { Text("Costo Unitario") },
                    placeholder = { Text("0.00") },
                    modifier = Modifier.weight(1f)
                )
            }

            Text(
                text = "Total: S/ %.2f".format(totalCompra),
                style = MaterialTheme.typography.titleMedium
            )

            Button(
                onClick = {
                    if (insumoNombre.isNotBlank() && cantidad.toDoubleOrNull() != null && costo.toDoubleOrNull() != null) {
                        val nuevaCompra = Compra(
                            insumo = insumoNombre,
                            cantidad = cantidad.toIntOrNull() ?: 0,
                            costo = costo.toDoubleOrNull() ?: 0.0,
                            total = totalCompra
                        )

                        scope.launch {
                            compraDao.insertarCompra(nuevaCompra)
                        }

                        insumoNombre = ""
                        cantidad = ""
                        costo = ""
                    }
                },
                modifier = Modifier.fillMaxWidth()
            ) {
                Text("Agregar Compra")
            }

            Spacer(modifier = Modifier.height(8.dp))

            Text(
                text = "Compras del día (${comprasDelDia.size})",
                fontWeight = FontWeight.Bold,
                style = MaterialTheme.typography.titleMedium
            )

            LazyColumn(
                verticalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                items(comprasDelDia) { compra ->
                    CompraItemCard(compra)
                }
            }

            Spacer(modifier = Modifier.height(24.dp))
            Button(onClick = { navController.popBackStack() }) {
                Text("Volver al Home")
            }
        }
    }
}
@Composable
fun CompraItemCard(compra: Compra) {
    val dateFormat = SimpleDateFormat("HH:mm", Locale.getDefault())
    val horaFormateada = dateFormat.format(Date(compra.fecha))

    Card(
        modifier = Modifier.fillMaxWidth()
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(12.dp),
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            Column(modifier = Modifier.weight(1f)) {
                Text(
                    text = compra.insumo,
                    fontWeight = FontWeight.Bold
                )
                Text(
                    text = "${compra.cantidad} x S/ %.2f".format(compra.costo)
                )
                Text(
                    text = horaFormateada,
                    style = MaterialTheme.typography.bodySmall,
                    color = MaterialTheme.colorScheme.onSurfaceVariant
                )
            }

            Text(
                text = "S/ %.2f".format(compra.total),
                style = MaterialTheme.typography.titleMedium,
                fontWeight = FontWeight.Bold,
                color = MaterialTheme.colorScheme.primary
            )
        }
    }
}

