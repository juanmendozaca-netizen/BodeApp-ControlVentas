package com.trabajo01.ui.viewmodel

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import com.trabajo01.data.database.AppDatabase
import com.trabajo01.data.repository.CompraRepository
import com.trabajo01.model.Compra
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.launch

class CompraViewModel(application: Application) : AndroidViewModel(application) {
    private val repository: CompraRepository
    val todasLasCompras: Flow<List<Compra>>
    val totalCompras: Flow<Double?>

    init {
        val compraDao = AppDatabase.getDatabase(application).compraDao()
        repository = CompraRepository(compraDao)
        todasLasCompras = repository.todasLasCompras
        totalCompras = repository.totalCompras
    }

    fun insertarCompra(compra: Compra) = viewModelScope.launch {
        repository.insertarCompra(compra)
    }

    fun obtenerComprasDelDia(fechaInicio: Long) =
        repository.obtenerComprasDelDia(fechaInicio)

    suspend fun obtenerTotalComprasPorFecha(fechaInicio: Long, fechaFin: Long): Double {
        return repository.obtenerTotalComprasPorFecha(fechaInicio, fechaFin)
    }

    fun eliminarCompra(compra: Compra) = viewModelScope.launch {
        repository.eliminarCompra(compra)
    }
}