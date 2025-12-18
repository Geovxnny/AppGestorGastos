package com.example.appgestorgastos.data.database

import androidx.room.*
import com.example.appgestorgastos.data.model.Gasto
import kotlinx.coroutines.flow.Flow

@Dao
interface GastoDao {
    @Query("SELECT * FROM gastos ORDER BY fecha DESC")
    fun getAllGastos(): Flow<List<Gasto>>

    @Query("SELECT * FROM gastos WHERE categoria = :categoria ORDER BY fecha DESC")
    fun getGastosPorCategoria(categoria: String): Flow<List<Gasto>>

    @Query("SELECT SUM(monto) FROM gastos")
    fun getTotalGastos(): Flow<Double?>

    // Nota: Para devolver un Map, asegúrate de tener una versión reciente de Room
    // Si te da error, avísame para cambiarlo por una clase simple.
    @MapInfo(keyColumn = "categoria", valueColumn = "total")
    @Query("SELECT categoria, SUM(monto) as total FROM gastos GROUP BY categoria")
    fun getTotalPorCategoria(): Flow<Map<String, Double>>

    @Insert
    suspend fun insertGasto(gasto: Gasto)

    @Delete
    suspend fun deleteGasto(gasto: Gasto)

    @Update
    suspend fun updateGasto(gasto: Gasto)
}