package com.geovanny.appgestorgastos.viewmodel

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import com.geovanny.appgestorgastos.data.database.AppDatabase
import com.geovanny.appgestorgastos.data.model.Gasto
import com.geovanny.appgestorgastos.data.repository.GastoRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch

class GastoViewModel(application: Application) : AndroidViewModel(application) {

    private val repository: GastoRepository
    val allGastos: Flow<List<Gasto>>
    val totalGastos: Flow<Double?>

    private val _categoriaSeleccionada = MutableStateFlow<String?>(null)
    val categoriaSeleccionada: StateFlow<String?> = _categoriaSeleccionada

    init {
        val gastoDao = AppDatabase.getDatabase(application).gastoDao()
        repository = GastoRepository(gastoDao)
        allGastos = repository.allGastos
        totalGastos = repository.totalGastos
    }

    fun insertGasto(gasto: Gasto) = viewModelScope.launch {
        repository.insertGasto(gasto)
    }

    fun deleteGasto(gasto: Gasto) = viewModelScope.launch {
        repository.deleteGasto(gasto)
    }

    fun updateGasto(gasto: Gasto) = viewModelScope.launch {
        repository.updateGasto(gasto)
    }

    fun filtrarPorCategoria(categoria: String?) {
        _categoriaSeleccionada.value = categoria
    }
}