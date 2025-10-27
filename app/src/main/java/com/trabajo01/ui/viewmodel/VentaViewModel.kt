package com.trabajo01.ui.viewmodel

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import com.trabajo01.data.database.AppDatabase
import com.trabajo01.data.repository.VentaRepository
import com.trabajo01.model.Venta
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.launch

class VentaViewModel(application: Application) : AndroidViewModel(application) {
    private val repository: VentaRepository
    val todasLasVentas: Flow<List<Venta>>
    val totalVentas: Flow<Double?>

    init {
        val ventaDao = AppDatabase.getDatabase(application).ventaDao()
        repository = VentaRepository(ventaDao)
        todasLasVentas = repository.todasLasVentas
        totalVentas = repository.totalVentas
    }

    fun insertarVenta(venta: Venta) = viewModelScope.launch {
        repository.insertarVenta(venta)
    }

    fun obtenerVentasDelDia(fechaInicio: Long) =
        repository.obtenerVentasDelDia(fechaInicio)

    suspend fun obtenerTotalVentasPorFecha(fechaInicio: Long, fechaFin: Long): Double {
        return repository.obtenerTotalVentasPorFecha(fechaInicio, fechaFin)
    }

    fun obtenerProductosMasVendidosDelDia(fechaInicio: Long) =
        repository.obtenerProductosMasVendidosDelDia(fechaInicio)

    fun eliminarVenta(venta: Venta) = viewModelScope.launch {
        repository.eliminarVenta(venta)
    }
}