package com.trabajo01.ui.viewmodel

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import com.trabajo01.data.database.AppDatabase
import com.trabajo01.data.repository.ProductoRepository
import com.trabajo01.model.Producto
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.launch

class ProductoViewModel(application: Application) : AndroidViewModel(application) {

    private val repository: ProductoRepository
    val todosLosProductos: Flow<List<Producto>>
    val productosConStock: Flow<List<Producto>>

    init {
        val productoDao = AppDatabase.getDatabase(application).productoDao()
        repository = ProductoRepository(productoDao)
        todosLosProductos = repository.todosLosProductos
        productosConStock = repository.productosConStock
    }

    fun insertarProducto(producto: Producto) = viewModelScope.launch {
        repository.insertarProducto(producto)
    }

    fun actualizarProducto(producto: Producto) = viewModelScope.launch {
        repository.actualizarProducto(producto)
    }

    fun eliminarProducto(producto: Producto) = viewModelScope.launch {
        repository.eliminarProducto(producto)
    }
}