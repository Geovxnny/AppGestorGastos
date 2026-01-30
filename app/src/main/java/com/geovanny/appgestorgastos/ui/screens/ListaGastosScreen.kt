package com.geovanny.appgestorgastos.ui.screens

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.geovanny.appgestorgastos.data.model.Categorias
import com.geovanny.appgestorgastos.data.model.Gasto
import com.geovanny.appgestorgastos.viewmodel.GastoViewModel
import java.text.NumberFormat
import java.text.SimpleDateFormat
import java.util.*
import androidx.compose.material.icons.filled.FilterList
import androidx.compose.material.icons.filled.Receipt

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ListaGastosScreen(viewModel: GastoViewModel, navController: NavController) {
    val gastos by viewModel.allGastos.collectAsState(initial = emptyList())
    val totalGastos by viewModel.totalGastos.collectAsState(initial = 0.0)
    var mostrarFiltros by remember { mutableStateOf(false) }
    var categoriaFiltro by remember { mutableStateOf<String?>(null) }

    val gastosFiltrados = if (categoriaFiltro != null) {
        gastos.filter { it.categoria == categoriaFiltro }
    } else {
        gastos
    }

    Column(modifier = Modifier.fillMaxSize()) {
        // Header con total
        Card(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp),
            colors = CardDefaults.cardColors(
                containerColor = MaterialTheme.colorScheme.primaryContainer
            )
        ) {
            Column(
                modifier = Modifier.padding(16.dp),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Text(
                    text = "Total Gastos",
                    style = MaterialTheme.typography.titleMedium
                )
                Text(
                    text = formatMoney(totalGastos ?: 0.0),
                    style = MaterialTheme.typography.headlineMedium,
                    color = MaterialTheme.colorScheme.primary
                )
            }
        }

        // Filtros
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 16.dp),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text(
                text = if (categoriaFiltro != null) "Filtrado: $categoriaFiltro" else "Todos los gastos",
                style = MaterialTheme.typography.titleMedium
            )
            IconButton(onClick = { mostrarFiltros = true }) {
                Icon(Icons.Filled.FilterList, contentDescription = "Filtrar")
            }
        }

        // Lista de gastos
        if (gastosFiltrados.isEmpty()) {
            Box(
                modifier = Modifier.fillMaxSize(),
                contentAlignment = Alignment.Center
            ) {
                Column(horizontalAlignment = Alignment.CenterHorizontally) {
                    Icon(
                        Icons.Filled.Receipt,
                        contentDescription = null,
                        modifier = Modifier.size(64.dp),
                        tint = MaterialTheme.colorScheme.outline
                    )
                    Spacer(modifier = Modifier.height(16.dp))
                    Text(
                        "No hay gastos registrados",
                        style = MaterialTheme.typography.bodyLarge,
                        color = MaterialTheme.colorScheme.outline
                    )
                }
            }
        } else {
            LazyColumn(
                modifier = Modifier.fillMaxSize(),
                contentPadding = PaddingValues(16.dp),
                verticalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                items(gastosFiltrados, key = { it.id }) { gasto ->
                    GastoItem(
                        gasto = gasto,
                        onDelete = { viewModel.deleteGasto(gasto) }
                    )
                }
            }
        }
    }

    // Diálogo de filtros
    if (mostrarFiltros) {
        AlertDialog(
            onDismissRequest = { mostrarFiltros = false },
            title = { Text("Filtrar por categoría") },
            text = {
                Column {
                    TextButton(
                        onClick = {
                            categoriaFiltro = null
                            mostrarFiltros = false
                        }
                    ) {
                        Text("Todas las categorías")
                    }
                    Categorias.TODAS.forEach { categoria ->
                        TextButton(
                            onClick = {
                                categoriaFiltro = categoria
                                mostrarFiltros = false
                            }
                        ) {
                            Text(categoria)
                        }
                    }
                }
            },
            confirmButton = {
                TextButton(onClick = { mostrarFiltros = false }) {
                    Text("Cerrar")
                }
            }
        )
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun GastoItem(gasto: Gasto, onDelete: () -> Unit) {
    var mostrarDialogo by remember { mutableStateOf(false) }

    Card(
        modifier = Modifier.fillMaxWidth(),
        onClick = { mostrarDialogo = true }
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Column(modifier = Modifier.weight(1f)) {
                Text(
                    text = gasto.descripcion,
                    style = MaterialTheme.typography.titleMedium
                )
                Text(
                    text = gasto.categoria,
                    style = MaterialTheme.typography.bodySmall,
                    color = MaterialTheme.colorScheme.outline
                )
                Text(
                    text = formatDate(gasto.fecha),
                    style = MaterialTheme.typography.bodySmall,
                    color = MaterialTheme.colorScheme.outline
                )
            }
            Text(
                text = formatMoney(gasto.monto),
                style = MaterialTheme.typography.titleLarge,
                color = MaterialTheme.colorScheme.error
            )
        }
    }

    if (mostrarDialogo) {
        AlertDialog(
            onDismissRequest = { mostrarDialogo = false },
            title = { Text(gasto.descripcion) },
            text = {
                Column {
                    Text("Monto: ${formatMoney(gasto.monto)}")
                    Text("Categoría: ${gasto.categoria}")
                    Text("Fecha: ${formatDate(gasto.fecha)}")
                    if (gasto.notas.isNotEmpty()) {
                        Text("Notas: ${gasto.notas}")
                    }
                }
            },
            confirmButton = {
                TextButton(
                    onClick = {
                        onDelete()
                        mostrarDialogo = false
                    }
                ) {
                    Text("Eliminar", color = MaterialTheme.colorScheme.error)
                }
            },
            dismissButton = {
                TextButton(onClick = { mostrarDialogo = false }) {
                    Text("Cerrar")
                }
            }
        )
    }
}

private fun formatMoney(amount: Double): String {
    val format = NumberFormat.getCurrencyInstance(Locale.US)
    return format.format(amount)
}

private fun formatDate(timestamp: Long): String {
    val sdf = SimpleDateFormat("dd/MM/yyyy", Locale.getDefault())
    return sdf.format(Date(timestamp))
}