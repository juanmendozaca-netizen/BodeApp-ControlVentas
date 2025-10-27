package com.trabajo01.data.repository

import com.trabajo01.data.dao.VentaDao
import com.trabajo01.model.Venta
import kotlinx.coroutines.flow.Flow

class VentaRepository(private val ventaDao: VentaDao) {
    val todasLasVentas: Flow<List<Venta>> = ventaDao.obtenerTodasLasVentas()
    val totalVentas: Flow<Double?> = ventaDao.obtenerTotalVentas()

    suspend fun insertarVenta(venta: Venta) {
        ventaDao.insertarVenta(venta)
    }

    fun obtenerVentasDelDia(fechaInicio: Long) =
        ventaDao.obtenerVentasDelDia(fechaInicio)

    suspend fun obtenerTotalVentasPorFecha(fechaInicio: Long, fechaFin: Long) =
        ventaDao.obtenerTotalVentasPorFecha(fechaInicio, fechaFin) ?: 0.0

    fun obtenerProductosMasVendidosDelDia(fechaInicio: Long) =
        ventaDao.obtenerProductosMasVendidosDelDia(fechaInicio)

    suspend fun eliminarVenta(venta: Venta) {
        ventaDao.eliminarVenta(venta)
    }
}