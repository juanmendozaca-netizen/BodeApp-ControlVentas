package com.trabajo01.data.repository

import com.trabajo01.data.dao.CompraDao
import com.trabajo01.model.Compra
import kotlinx.coroutines.flow.Flow

class CompraRepository(private val compraDao: CompraDao) {
    val todasLasCompras: Flow<List<Compra>> = compraDao.obtenerTodasLasCompras()
    val totalCompras: Flow<Double?> = compraDao.obtenerTotalCompras()

    suspend fun insertarCompra(compra: Compra) {
        compraDao.insertarCompra(compra)
    }

    fun obtenerComprasDelDia(fechaInicio: Long) =
        compraDao.obtenerComprasDelDia(fechaInicio)

    suspend fun obtenerTotalComprasPorFecha(fechaInicio: Long, fechaFin: Long) =
        compraDao.obtenerTotalComprasPorFecha(fechaInicio, fechaFin) ?: 0.0

    suspend fun eliminarCompra(compra: Compra) {
        compraDao.eliminarCompra(compra)
    }
}