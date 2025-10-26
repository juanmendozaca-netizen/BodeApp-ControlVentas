package com.trabajo01.data.repository

import com.trabajo01.data.dao.ProductoDao
import com.trabajo01.model.Producto
import kotlinx.coroutines.flow.Flow

class ProductoRepository(private val productoDao: ProductoDao) {

    val todosLosProductos: Flow<List<Producto>> = productoDao.obtenerTodosLosProductos()
    val productosConStock: Flow<List<Producto>> = productoDao.obtenerProductosConStock()

    suspend fun insertarProducto(producto: Producto) {
        productoDao.insertarProducto(producto)
    }

    suspend fun actualizarProducto(producto: Producto) {
        productoDao.actualizarProducto(producto)
    }

    suspend fun eliminarProducto(producto: Producto) {
        productoDao.eliminarProducto(producto)
    }

    suspend fun obtenerProductoPorId(id: Int): Producto? {
        return productoDao.obtenerProductoPorId(id)
    }
}