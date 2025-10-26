package com.trabajo01.data.dao

import androidx.room.*
import com.trabajo01.data.entity.Compra
import kotlinx.coroutines.flow.Flow

@Dao
interface CompraDao {

    @Insert
    suspend fun insertarCompra(compra: Compra)

    @Query("SELECT * FROM compras ORDER BY fecha DESC")
    fun obtenerTodasLasCompras(): Flow<List<Compra>>

    @Query("SELECT * FROM compras WHERE fecha >= :fechaInicio ORDER BY fecha DESC")
    fun obtenerComprasDelDia(fechaInicio: Long): Flow<List<Compra>>

    @Query("SELECT SUM(total) FROM compras")
    fun obtenerTotalCompras(): Flow<Double?>

    @Delete
    suspend fun eliminarCompra(compra: Compra)

    @Query("DELETE FROM compras")
    suspend fun eliminarTodasLasCompras()
}