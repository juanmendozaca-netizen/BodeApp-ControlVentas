package com.trabajo01.data.dao

import androidx.room.*
import com.trabajo01.model.Producto
import kotlinx.coroutines.flow.Flow

@Dao
interface ProductoDao {
    @Insert
    suspend fun insertarProducto(producto: Producto)

    @Update
    suspend fun actualizarProducto(producto: Producto)

    @Delete
    suspend fun eliminarProducto(producto: Producto)

    @Query("SELECT * FROM productos ORDER BY nombre ASC")
    fun obtenerTodosLosProductos(): Flow<List<Producto>>

    @Query("SELECT * FROM productos WHERE id = :id")
    suspend fun obtenerProductoPorId(id: Int): Producto?

    @Query("SELECT * FROM productos WHERE stock > 0 ORDER BY nombre ASC")
    fun obtenerProductosConStock(): Flow<List<Producto>>
}