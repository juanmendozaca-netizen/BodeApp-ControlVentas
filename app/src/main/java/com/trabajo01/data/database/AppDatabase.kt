//database/AppDatabase.kt

package com.trabajo01.data.database

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import com.trabajo01.data.dao.CompraDao
import com.trabajo01.data.dao.ProductoDao
import com.trabajo01.data.dao.VentaDao
import com.trabajo01.model.Compra
import com.trabajo01.model.Producto
import com.trabajo01.model.Venta

@Database(
    entities = [Producto::class, Venta::class, Compra::class],
    version = 2,
    exportSchema = false
)
abstract class AppDatabase : RoomDatabase() {
    abstract fun productoDao(): ProductoDao
    abstract fun ventaDao(): VentaDao
    abstract fun compraDao(): CompraDao

    companion object {
        @Volatile
        private var INSTANCE: AppDatabase? = null

        fun getDatabase(context: Context): AppDatabase {
            return INSTANCE ?: synchronized(this) {
                val instance = Room.databaseBuilder(
                    context.applicationContext,
                    AppDatabase::class.java,
                    "bodeapp_database"
                )
                    .fallbackToDestructiveMigration()
                    .build()
                INSTANCE = instance
                instance
            }
        }
    }
}