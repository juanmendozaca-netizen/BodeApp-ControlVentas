package com.trabajo01.data.entity

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "compras")
data class Compra(
    @PrimaryKey(autoGenerate = true)
    val id: Int = 0,

    val insumo: String,
    val cantidad: Int,
    val costo: Double,
    val total: Double,
    val fecha: Long = System.currentTimeMillis()
)