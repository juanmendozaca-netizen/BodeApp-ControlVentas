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
import com.trabajo01.model.Compra
import kotlinx.coroutines.launch
import java.text.SimpleDateFormat
import com.trabajo01.data.database.AppDatabase
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

    var mensajeError by remember { mutableStateOf("") }
    var mostrarError by remember { mutableStateOf(false) }

    var filtrarPorSemana by remember { mutableStateOf(false) }

    val todasLasCompras by compraDao.obtenerTodasLasCompras()
        .collectAsState(initial = emptyList())

    val haceSieteDias = System.currentTimeMillis() - (7 * 24 * 60 * 60 * 1000L)

    val comprasFiltradas = if (filtrarPorSemana) {
        todasLasCompras.filter { it.fecha >= haceSieteDias }
    } else {
        todasLasCompras
    }

    val totalCompras = comprasFiltradas.sumOf { it.total }

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
                modifier = Modifier.fillMaxWidth(),
                isError = mostrarError && insumoNombre.isBlank()
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
                    modifier = Modifier.weight(1f),
                    isError = mostrarError && (cantidad.isBlank() || cantidad.toIntOrNull() == null || cantidad.toIntOrNull()!! <= 0)
                )

                OutlinedTextField(
                    value = costo,
                    onValueChange = { costo = it },
                    label = { Text("Costo Unitario") },
                    placeholder = { Text("0.00") },
                    modifier = Modifier.weight(1f),
                    isError = mostrarError && (costo.isBlank() || costo.toDoubleOrNull() == null || costo.toDoubleOrNull()!! <= 0)
                )
            }

            Text(
                text = "Total: S/ %.2f".format(totalCompra),
                style = MaterialTheme.typography.titleMedium
            )

            Button(
                onClick = {
                    mostrarError = false
                    mensajeError = ""

                    if (insumoNombre.isBlank()) {
                        mensajeError = "El nombre del insumo es obligatorio"
                        mostrarError = true
                        return@Button
                    }

                    if (cantidad.isBlank()) {
                        mensajeError = "La cantidad es obligatoria"
                        mostrarError = true
                        return@Button
                    }

                    if (costo.isBlank()) {
                        mensajeError = "El costo es obligatorio"
                        mostrarError = true
                        return@Button
                    }

                    val cantidadInt = cantidad.toIntOrNull()
                    if (cantidadInt == null || cantidadInt <= 0) {
                        mensajeError = "La cantidad debe ser un número mayor a 0"
                        mostrarError = true
                        return@Button
                    }

                    val costoDouble = costo.toDoubleOrNull()
                    if (costoDouble == null || costoDouble <= 0) {
                        mensajeError = "El costo debe ser un número mayor a 0"
                        mostrarError = true
                        return@Button
                    }

                    val compra = Compra(
                        insumo = insumoNombre,
                        cantidad = cantidadInt,
                        costo = costoDouble,
                        total = totalCompra
                    )

                    scope.launch {
                        compraDao.insertarCompra(compra)
                    }

                    insumoNombre = ""
                    cantidad = ""
                    costo = ""
                },
                modifier = Modifier.fillMaxWidth()
            ) {
                Text("Agregar Compra")
            }

            if (mostrarError && mensajeError.isNotEmpty()) {
                Card(
                    colors = CardDefaults.cardColors(
                        containerColor = MaterialTheme.colorScheme.errorContainer
                    ),
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Text(
                        text = "⚠️ $mensajeError",
                        modifier = Modifier.padding(12.dp),
                        color = MaterialTheme.colorScheme.onErrorContainer,
                        style = MaterialTheme.typography.bodyMedium
                    )
                }
            }

            Spacer(modifier = Modifier.height(8.dp))

            Card(
                colors = CardDefaults.cardColors(
                    containerColor = MaterialTheme.colorScheme.tertiaryContainer
                ),
                modifier = Modifier.fillMaxWidth()
            ) {
                Column(
                    modifier = Modifier.padding(16.dp)
                ) {
                    Text(
                        text = if (filtrarPorSemana) "Compras de la semana" else "Reporte de Compras",
                        style = MaterialTheme.typography.titleLarge,
                        fontWeight = FontWeight.Bold
                    )
                    Spacer(modifier = Modifier.height(8.dp))

                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween
                    ) {
                        Column {
                            Text(
                                text = "Total compras:",
                                style = MaterialTheme.typography.bodyMedium
                            )
                            Text(
                                text = "${comprasFiltradas.size} compras",
                                style = MaterialTheme.typography.titleMedium,
                                fontWeight = FontWeight.Bold,
                                color = MaterialTheme.colorScheme.tertiary
                            )
                        }

                        Column(horizontalAlignment = Alignment.End) {
                            Text(
                                text = "Gastos totales:",
                                style = MaterialTheme.typography.bodyMedium
                            )
                            Text(
                                text = "S/ %.2f".format(totalCompras),
                                style = MaterialTheme.typography.titleMedium,
                                fontWeight = FontWeight.Bold,
                                color = MaterialTheme.colorScheme.tertiary
                            )
                        }
                    }
                }
            }

            Spacer(modifier = Modifier.height(8.dp))

            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                FilterChip(
                    selected = !filtrarPorSemana,
                    onClick = { filtrarPorSemana = false },
                    label = { Text("Todas") },
                    modifier = Modifier.weight(1f)
                )

                FilterChip(
                    selected = filtrarPorSemana,
                    onClick = { filtrarPorSemana = true },
                    label = { Text("Última semana") },
                    modifier = Modifier.weight(1f)
                )
            }

            Spacer(modifier = Modifier.height(8.dp))

            Text(
                text = "Compras registradas",
                fontWeight = FontWeight.Bold,
                style = MaterialTheme.typography.titleMedium
            )

            LazyColumn(
                verticalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                items(comprasFiltradas) { compra ->
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

