package com.geovanny.appgestorgastos.data.repository

import com.geovanny.appgestorgastos.data.database.GastoDao
import com.geovanny.appgestorgastos.data.model.Gasto
import kotlinx.coroutines.flow.Flow

class GastoRepository(private val gastoDao: GastoDao) {

    val allGastos: Flow<List<Gasto>> = gastoDao.getAllGastos()

    val totalGastos: Flow<Double?> = gastoDao.getTotalGastos()

    fun getGastosPorCategoria(categoria: String): Flow<List<Gasto>> {
        return gastoDao.getGastosPorCategoria(categoria)
    }

    suspend fun insertGasto(gasto: Gasto) {
        gastoDao.insertGasto(gasto)
    }

    suspend fun deleteGasto(gasto: Gasto) {
        gastoDao.deleteGasto(gasto)
    }

    suspend fun updateGasto(gasto: Gasto) {
        gastoDao.updateGasto(gasto)
    }
}