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
import com.trabajo01.data.entity.Venta
import kotlinx.coroutines.launch
import java.text.SimpleDateFormat
import java.util.*

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun VentasScreen(navController: NavController) {
    val context = LocalContext.current
    val database = remember { AppDatabase.getDatabase(context) }
    val ventaDao = database.ventaDao()

    val scope = rememberCoroutineScope()

    var productoNombre by remember { mutableStateOf("") }
    var cantidad by remember { mutableStateOf("") }
    var precioUnitario by remember { mutableStateOf("") }

    val ventasDelDia by ventaDao.obtenerTodasLasVentas()
        .collectAsState(initial = emptyList())

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
            verticalArrangement = Arrangement.spacedBy(16.dp)
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
                placeholder = { Text("Ej: Galleta de vainilla") },
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
                    value = precioUnitario,
                    onValueChange = { precioUnitario = it },
                    label = { Text("Precio") },
                    placeholder = { Text("0.00") },
                    modifier = Modifier.weight(1f)
                )
            }

            Text(
                text = "Subtotal: S/ %.2f".format(subtotal),
                style = MaterialTheme.typography.titleMedium
            )

            Button(
                onClick = {
                    if (productoNombre.isNotBlank() &&
                        cantidad.isNotBlank() &&
                        precioUnitario.isNotBlank()) {

                        val nuevaVenta = Venta(
                            producto = productoNombre,
                            cantidad = cantidad.toIntOrNull() ?: 0,
                            precioUnitario = precioUnitario.toDoubleOrNull() ?: 0.0,
                            total = subtotal
                        )

                        scope.launch {
                            ventaDao.insertarVenta(nuevaVenta)
                        }

                        productoNombre = ""
                        cantidad = ""
                        precioUnitario = ""
                    }
                },
                modifier = Modifier.fillMaxWidth()
            ) {
                Text("Agregar Venta")
            }

            Spacer(modifier = Modifier.height(8.dp))

            Text(
                text = "Ventas del día (${ventasDelDia.size})",
                fontWeight = FontWeight.Bold,
                style = MaterialTheme.typography.titleMedium
            )

            LazyColumn(
                verticalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                items(ventasDelDia) { venta ->
                    VentaItemCard(venta)
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
fun VentaItemCard(venta: Venta) {
    val dateFormat = SimpleDateFormat("HH:mm", Locale.getDefault())
    val horaFormateada = dateFormat.format(Date(venta.fecha))

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
                    text = venta.producto,
                    fontWeight = FontWeight.Bold
                )
                Text(
                    text = "${venta.cantidad} x S/ %.2f".format(venta.precioUnitario)
                )
                Text(
                    text = horaFormateada,
                    style = MaterialTheme.typography.bodySmall,
                    color = MaterialTheme.colorScheme.onSurfaceVariant
                )
            }

            Text(
                text = "S/ %.2f".format(venta.total),
                style = MaterialTheme.typography.titleMedium,
                fontWeight = FontWeight.Bold,
                color = MaterialTheme.colorScheme.primary
            )
        }
    }
}