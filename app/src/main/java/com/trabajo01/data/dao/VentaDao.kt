package com.trabajo01.data.dao

import androidx.room.*
import com.trabajo01.data.entity.Venta
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

    @Delete
    suspend fun eliminarVenta(venta: Venta)

    @Query("DELETE FROM ventas")
    suspend fun eliminarTodasLasVentas()
}