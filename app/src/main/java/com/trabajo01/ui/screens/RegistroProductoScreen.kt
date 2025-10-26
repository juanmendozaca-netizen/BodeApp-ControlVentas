package com.trabajo01.ui.screens

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.ArrowDropDown
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import com.trabajo01.model.Producto
import com.trabajo01.ui.viewmodel.ProductoViewModel
import kotlinx.coroutines.launch

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun RegistroProductoScreen(
    navController: NavController,
    viewModel: ProductoViewModel = viewModel()
) {
    var nombre by remember { mutableStateOf("") }
    var precio by remember { mutableStateOf("") }
    var stock by remember { mutableStateOf("") }
    var categoriaSeleccionada by remember { mutableStateOf("") }
    var otraCategoria by remember { mutableStateOf("") }
    var expandedCategoria by remember { mutableStateOf(false) }

    // Para modo edición
    var productoEnEdicion by remember { mutableStateOf<Producto?>(null) }
    var mostrarDialogoEliminar by remember { mutableStateOf<Producto?>(null) }

    val snackbarHostState = remember { SnackbarHostState() }
    val scope = rememberCoroutineScope()
    val categorias = listOf("Snack", "Abarrotes", "Lácteos", "Limpieza", "Otros")

    val productos by viewModel.todosLosProductos.collectAsState(initial = emptyList())

    // Diálogo de confirmación para eliminar
    mostrarDialogoEliminar?.let { producto ->
        AlertDialog(
            onDismissRequest = { mostrarDialogoEliminar = null },
            title = { Text("Confirmar eliminación") },
            text = { Text("¿Está seguro de eliminar '${producto.nombre}'?") },
            confirmButton = {
                TextButton(
                    onClick = {
                        viewModel.eliminarProducto(producto)
                        mostrarDialogoEliminar = null
                        scope.launch {
                            snackbarHostState.showSnackbar("Producto eliminado")
                        }
                    }
                ) {
                    Text("Eliminar", color = MaterialTheme.colorScheme.error)
                }
            },
            dismissButton = {
                TextButton(onClick = { mostrarDialogoEliminar = null }) {
                    Text("Cancelar")
                }
            }
        )
    }

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Gestión de Productos") },
                navigationIcon = {
                    IconButton(onClick = { navController.popBackStack() }) {
                        Icon(
                            imageVector = Icons.AutoMirrored.Filled.ArrowBack,
                            contentDescription = "Volver"
                        )
                    }
                }
            )
        },
        snackbarHost = { SnackbarHost(snackbarHostState) }
    ) { padding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(padding)
        ) {
            Card(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(16.dp),
                elevation = CardDefaults.cardElevation(defaultElevation = 2.dp)
            ) {
                Column(
                    modifier = Modifier.padding(16.dp),
                    verticalArrangement = Arrangement.spacedBy(12.dp)
                ) {
                    Text(
                        text = if (productoEnEdicion == null) "Agregar Nuevo Producto" else "Editar Producto",
                        style = MaterialTheme.typography.titleLarge
                    )

                    OutlinedTextField(
                        value = nombre,
                        onValueChange = { nombre = it },
                        label = { Text("Nombre del Producto") },
                        modifier = Modifier.fillMaxWidth(),
                        singleLine = true
                    )

                    ExposedDropdownMenuBox(
                        expanded = expandedCategoria,
                        onExpandedChange = { expandedCategoria = it }
                    ) {
                        OutlinedTextField(
                            value = categoriaSeleccionada,
                            onValueChange = {},
                            readOnly = true,
                            label = { Text("Categoría") },
                            trailingIcon = {
                                Icon(
                                    imageVector = Icons.Default.ArrowDropDown,
                                    contentDescription = "Seleccionar categoría"
                                )
                            },
                            modifier = Modifier
                                .fillMaxWidth()
                                .menuAnchor(),
                            colors = ExposedDropdownMenuDefaults.outlinedTextFieldColors()
                        )

                        ExposedDropdownMenu(
                            expanded = expandedCategoria,
                            onDismissRequest = { expandedCategoria = false }
                        ) {
                            categorias.forEach { categoria ->
                                DropdownMenuItem(
                                    text = { Text(categoria) },
                                    onClick = {
                                        categoriaSeleccionada = categoria
                                        expandedCategoria = false
                                        if (categoria != "Otros") {
                                            otraCategoria = ""
                                        }
                                    }
                                )
                            }
                        }
                    }

                    if (categoriaSeleccionada == "Otros") {
                        OutlinedTextField(
                            value = otraCategoria,
                            onValueChange = { otraCategoria = it },
                            label = { Text("Especificar Categoría") },
                            modifier = Modifier.fillMaxWidth(),
                            singleLine = true,
                            placeholder = { Text("Ej: Bebidas, Golosinas") }
                        )
                    }

                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.spacedBy(8.dp)
                    ) {
                        OutlinedTextField(
                            value = precio,
                            onValueChange = { precio = it },
                            label = { Text("Precio") },
                            modifier = Modifier.weight(1f),
                            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Decimal),
                            prefix = { Text("S/ ") },
                            singleLine = true
                        )

                        OutlinedTextField(
                            value = stock,
                            onValueChange = { stock = it },
                            label = { Text("Stock") },
                            modifier = Modifier.weight(1f),
                            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
                            singleLine = true
                        )
                    }

                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.spacedBy(8.dp)
                    ) {
                        if (productoEnEdicion != null) {
                            OutlinedButton(
                                onClick = {
                                    // Cancelar edición
                                    productoEnEdicion = null
                                    nombre = ""
                                    precio = ""
                                    stock = ""
                                    categoriaSeleccionada = ""
                                    otraCategoria = ""
                                },
                                modifier = Modifier.weight(1f)
                            ) {
                                Text("Cancelar")
                            }
                        }

                        Button(
                            onClick = {
                                val categoriaFinal = if (categoriaSeleccionada == "Otros") {
                                    otraCategoria
                                } else {
                                    categoriaSeleccionada
                                }

                                if (productoEnEdicion != null) {
                                    // Actualizar producto existente
                                    val productoActualizado = productoEnEdicion!!.copy(
                                        nombre = nombre,
                                        categoria = categoriaFinal,
                                        precio = precio.toDoubleOrNull() ?: 0.0,
                                        stock = stock.toIntOrNull() ?: 0
                                    )
                                    viewModel.actualizarProducto(productoActualizado)
                                    scope.launch {
                                        snackbarHostState.showSnackbar("Producto actualizado")
                                    }
                                    productoEnEdicion = null
                                } else {
                                    // Crear nuevo producto
                                    val producto = Producto(
                                        nombre = nombre,
                                        categoria = categoriaFinal,
                                        precio = precio.toDoubleOrNull() ?: 0.0,
                                        stock = stock.toIntOrNull() ?: 0
                                    )
                                    viewModel.insertarProducto(producto)
                                    scope.launch {
                                        snackbarHostState.showSnackbar("Producto guardado")
                                    }
                                }

                                // Limpiar formulario
                                nombre = ""
                                precio = ""
                                stock = ""
                                categoriaSeleccionada = ""
                                otraCategoria = ""
                            },
                            modifier = Modifier.weight(1f),
                            enabled = nombre.isNotBlank() &&
                                    precio.isNotBlank() &&
                                    stock.isNotBlank() &&
                                    categoriaSeleccionada.isNotBlank() &&
                                    (categoriaSeleccionada != "Otros" || otraCategoria.isNotBlank())
                        ) {
                            Text(if (productoEnEdicion == null) "Guardar" else "Actualizar")
                        }
                    }
                }
            }

            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 16.dp)
            ) {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween
                ) {
                    Text(
                        text = "Productos Registrados",
                        style = MaterialTheme.typography.titleMedium
                    )
                    Text(
                        text = "${productos.size} productos",
                        style = MaterialTheme.typography.bodyMedium,
                        color = MaterialTheme.colorScheme.primary
                    )
                }

                Spacer(modifier = Modifier.height(8.dp))

                if (productos.isEmpty()) {
                    Card(
                        modifier = Modifier.fillMaxWidth()
                    ) {
                        Box(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(32.dp),
                            contentAlignment = androidx.compose.ui.Alignment.Center
                        ) {
                            Text(
                                text = "No hay productos registrados",
                                style = MaterialTheme.typography.bodyMedium,
                                color = MaterialTheme.colorScheme.onSurfaceVariant
                            )
                        }
                    }
                } else {
                    LazyColumn(
                        verticalArrangement = Arrangement.spacedBy(8.dp),
                        contentPadding = PaddingValues(bottom = 16.dp)
                    ) {
                        items(productos) { producto ->
                            ProductoCard(
                                producto = producto,
                                onEdit = { prod ->
                                    // Cargar datos en el formulario
                                    productoEnEdicion = prod
                                    nombre = prod.nombre
                                    precio = prod.precio.toString()
                                    stock = prod.stock.toString()

                                    // Manejar categoría
                                    if (categorias.contains(prod.categoria)) {
                                        categoriaSeleccionada = prod.categoria
                                        otraCategoria = ""
                                    } else {
                                        categoriaSeleccionada = "Otros"
                                        otraCategoria = prod.categoria
                                    }
                                },
                                onDelete = { prod ->
                                    mostrarDialogoEliminar = prod
                                }
                            )
                        }
                    }
                }
            }
        }
    }
}

@Composable
fun ProductoCard(
    producto: Producto,
    onEdit: (Producto) -> Unit,
    onDelete: (Producto) -> Unit
) {
    Card(
        modifier = Modifier.fillMaxWidth(),
        colors = CardDefaults.cardColors(
            containerColor = MaterialTheme.colorScheme.surfaceVariant
        )
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp),
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            Column(
                modifier = Modifier.weight(1f)
            ) {
                Text(
                    text = producto.nombre,
                    style = MaterialTheme.typography.titleMedium,
                    color = MaterialTheme.colorScheme.onSurfaceVariant
                )

                Spacer(modifier = Modifier.height(4.dp))

                Text(
                    text = producto.categoria,
                    style = MaterialTheme.typography.bodySmall,
                    color = MaterialTheme.colorScheme.primary
                )

                Spacer(modifier = Modifier.height(8.dp))

                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween
                ) {
                    Text(
                        text = "S/ %.2f".format(producto.precio),
                        style = MaterialTheme.typography.bodyMedium
                    )
                    Text(
                        text = "Stock: ${producto.stock}",
                        style = MaterialTheme.typography.bodyMedium,
                        color = if (producto.stock > 0)
                            MaterialTheme.colorScheme.tertiary
                        else
                            MaterialTheme.colorScheme.error
                    )
                }
            }

            Column(
                verticalArrangement = Arrangement.spacedBy(4.dp)
            ) {
                IconButton(
                    onClick = { onEdit(producto) }
                ) {
                    Icon(
                        imageVector = Icons.Default.Edit,
                        contentDescription = "Editar",
                        tint = MaterialTheme.colorScheme.primary
                    )
                }
                IconButton(
                    onClick = { onDelete(producto) }
                ) {
                    Icon(
                        imageVector = Icons.Default.Delete,
                        contentDescription = "Eliminar",
                        tint = MaterialTheme.colorScheme.error
                    )
                }
            }
        }
    }
}