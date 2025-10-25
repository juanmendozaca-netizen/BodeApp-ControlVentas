package com.trabajo01.ui.screens

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.ArrowDropDown
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun RegistroProductoScreen(navController: NavController) {
    var nombre by remember { mutableStateOf("") }
    var precio by remember { mutableStateOf("") }
    var stock by remember { mutableStateOf("") }
    var categoriaSeleccionada by remember { mutableStateOf("") }
    var otraCategoria by remember { mutableStateOf("") }
    var expandedCategoria by remember { mutableStateOf(false) }

    val categorias = listOf("Snack", "Abarrotes", "Lácteos", "Limpieza", "Otros")

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Registro de Producto") },
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
                text = "Agregar Nuevo Producto",
                style = MaterialTheme.typography.headlineSmall
            )

            OutlinedTextField(
                value = nombre,
                onValueChange = { nombre = it },
                label = { Text("Nombre del Producto") },
                modifier = Modifier.fillMaxWidth(),
                singleLine = true
            )

            // Dropdown de Categoría
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

            // Campo para "Otra Categoría" si seleccionó "Otros"
            if (categoriaSeleccionada == "Otros") {
                OutlinedTextField(
                    value = otraCategoria,
                    onValueChange = { otraCategoria = it },
                    label = { Text("Especificar Categoría") },
                    modifier = Modifier.fillMaxWidth(),
                    singleLine = true,
                    placeholder = { Text("Ej: Bebidas, Golosinas, etc.") }
                )
            }

            OutlinedTextField(
                value = precio,
                onValueChange = { precio = it },
                label = { Text("Precio de Venta") },
                modifier = Modifier.fillMaxWidth(),
                keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Decimal),
                prefix = { Text("S/ ") },
                singleLine = true
            )

            OutlinedTextField(
                value = stock,
                onValueChange = { stock = it },
                label = { Text("Stock Inicial") },
                modifier = Modifier.fillMaxWidth(),
                keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
                singleLine = true
            )

            Spacer(modifier = Modifier.weight(1f))

            Button(
                onClick = {
                    // TODO: Guardar producto (Día 4)
                    navController.popBackStack()
                },
                modifier = Modifier.fillMaxWidth(),
                enabled = nombre.isNotBlank() &&
                        precio.isNotBlank() &&
                        stock.isNotBlank() &&
                        categoriaSeleccionada.isNotBlank() &&
                        (categoriaSeleccionada != "Otros" || otraCategoria.isNotBlank())
            ) {
                Text("Guardar Producto")
            }
        }
    }
}