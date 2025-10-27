package com.trabajo01.data.dao

import androidx.room.*
import com.trabajo01.model.Venta
import kotlinx.coroutines.flow.Flow

@Dao
interface VentaDao {

    @Insert
    suspend fun insertarVenta(venta: Venta)

    @Query("SELECT * FROM ventas ORDER BY fecha DESC")
    fun obtenerTodasLasVentas(): Flow<List<Venta>>

    @Query("SELECT * FROM ventas WHERE fecha >= :fechaInicio ORDER BY fecha DESC")
    fun obtenerVentasDelDia(fechaInicio: Long): Flow<List<Venta>>

    @Query("SELECT SUM(total) FROM ventas")
    fun obtenerTotalVentas(): Flow<Double?>

    // ← AGREGAR ESTAS DOS CONSULTAS
    @Query("SELECT SUM(total) FROM ventas WHERE fecha >= :fechaInicio AND fecha <= :fechaFin")
    suspend fun obtenerTotalVentasPorFecha(fechaInicio: Long, fechaFin: Long): Double?

    @Query("""
        SELECT producto as nombreProducto, SUM(cantidad) as totalVendido 
        FROM ventas 
        WHERE fecha >= :fechaInicio 
        GROUP BY producto 
        ORDER BY totalVendido DESC 
        LIMIT 5
    """)
    fun obtenerProductosMasVendidosDelDia(fechaInicio: Long): Flow<List<ProductoVendido>>

    @Delete
    suspend fun eliminarVenta(venta: Venta)

    @Query("DELETE FROM ventas")
    suspend fun eliminarTodasLasVentas()
}

// ← AGREGAR ESTA DATA CLASS
data class ProductoVendido(
    val nombreProducto: String,
    val totalVendido: Int
)