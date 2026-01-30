package com.geovanny.appgestorgastos.ui.screens

import androidx.compose.foundation.Canvas
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.StrokeCap
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.unit.dp
import com.geovanny.appgestorgastos.viewmodel.GastoViewModel
import java.text.NumberFormat
import java.util.*
import kotlin.math.min
import androidx.compose.material.icons.filled.AccountBalance

@Composable
fun ResumenScreen(viewModel: GastoViewModel) {
    val gastos by viewModel.allGastos.collectAsState(initial = emptyList())
    val totalGastos by viewModel.totalGastos.collectAsState(initial = 0.0)

    // Calcular gastos por categoría
    val gastosPorCategoria = gastos.groupBy { it.categoria }
        .mapValues { entry -> entry.value.sumOf { it.monto } }
        .toList()
        .sortedByDescending { it.second }

    val colores = listOf(
        Color(0xFFE57373), // Rojo
        Color(0xFF81C784), // Verde
        Color(0xFF64B5F6), // Azul
        Color(0xFFFFB74D), // Naranja
        Color(0xFFBA68C8), // Morado
        Color(0xFF4DB6AC), // Verde azulado
        Color(0xFFFFD54F), // Amarillo
        Color(0xFF90A4AE)  // Gris
    )

    LazyColumn(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp),
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        item {
            Text(
                text = "Resumen de Gastos",
                style = MaterialTheme.typography.headlineMedium
            )
        }

        // Card con total
        item {
            Card(
                modifier = Modifier.fillMaxWidth(),
                colors = CardDefaults.cardColors(
                    containerColor = MaterialTheme.colorScheme.primaryContainer
                )
            ) {
                Column(
                    modifier = Modifier.padding(24.dp),
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    Icon(
                        Icons.Filled.AccountBalance,
                        contentDescription = null,
                        modifier = Modifier.size(48.dp),
                        tint = MaterialTheme.colorScheme.primary
                    )
                    Spacer(modifier = Modifier.height(8.dp))
                    Text(
                        text = "Total Acumulado",
                        style = MaterialTheme.typography.titleMedium
                    )
                    Text(
                        text = formatMoney(totalGastos ?: 0.0),
                        style = MaterialTheme.typography.headlineLarge,
                        color = MaterialTheme.colorScheme.primary
                    )
                    Text(
                        text = "${gastos.size} transacciones",
                        style = MaterialTheme.typography.bodyMedium,
                        color = MaterialTheme.colorScheme.outline
                    )
                }
            }
        }

        // Gráfico de dona (circular)
        if (gastosPorCategoria.isNotEmpty()) {
            item {
                Card(modifier = Modifier.fillMaxWidth()) {
                    Column(
                        modifier = Modifier.padding(16.dp),
                        horizontalAlignment = Alignment.CenterHorizontally
                    ) {
                        Text(
                            text = "Distribución por Categorías",
                            style = MaterialTheme.typography.titleLarge
                        )
                        Spacer(modifier = Modifier.height(16.dp))

                        DonutChart(
                            data = gastosPorCategoria,
                            colors = colores,
                            modifier = Modifier
                                .size(200.dp)
                                .padding(16.dp)
                        )
                    }
                }
            }

            // Lista de categorías con colores
            item {
                Text(
                    text = "Desglose por Categoría",
                    style = MaterialTheme.typography.titleMedium
                )
            }

            items(gastosPorCategoria) { (categoria, monto) ->
                val index = gastosPorCategoria.indexOf(categoria to monto)
                val porcentaje = if (totalGastos != null && totalGastos!! > 0) {
                    (monto / totalGastos!!) * 100
                } else 0.0

                Card(modifier = Modifier.fillMaxWidth()) {
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(16.dp),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Row(
                            verticalAlignment = Alignment.CenterVertically,
                            modifier = Modifier.weight(1f)
                        ) {
                            Canvas(modifier = Modifier.size(16.dp)) {
                                drawCircle(color = colores[index % colores.size])
                            }
                            Spacer(modifier = Modifier.width(12.dp))
                            Column {
                                Text(
                                    text = categoria,
                                    style = MaterialTheme.typography.titleMedium
                                )
                                Text(
                                    text = String.format("%.1f%%", porcentaje),
                                    style = MaterialTheme.typography.bodySmall,
                                    color = MaterialTheme.colorScheme.outline
                                )
                            }
                        }
                        Text(
                            text = formatMoney(monto),
                            style = MaterialTheme.typography.titleMedium,
                            color = MaterialTheme.colorScheme.error
                        )
                    }
                }
            }
        } else {
            item {
                Card(
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(200.dp)
                ) {
                    Box(
                        modifier = Modifier.fillMaxSize(),
                        contentAlignment = Alignment.Center
                    ) {
                        Column(horizontalAlignment = Alignment.CenterHorizontally) {
                            Icon(
                                Icons.Filled.PieChart,
                                contentDescription = null,
                                modifier = Modifier.size(64.dp),
                                tint = MaterialTheme.colorScheme.outline
                            )
                            Spacer(modifier = Modifier.height(16.dp))
                            Text(
                                "No hay datos para mostrar",
                                style = MaterialTheme.typography.bodyLarge,
                                color = MaterialTheme.colorScheme.outline
                            )
                        }
                    }
                }
            }
        }
    }
}

@Composable
fun DonutChart(
    data: List<Pair<String, Double>>,
    colors: List<Color>,
    modifier: Modifier = Modifier
) {
    val total = data.sumOf { it.second }

    Canvas(modifier = modifier) {
        val canvasSize = min(size.width, size.height)
        val radius = canvasSize / 2
        val strokeWidth = radius * 0.3f
        val innerRadius = radius - strokeWidth

        var startAngle = -90f

        data.forEachIndexed { index, (_, value) ->
            val sweepAngle = (value / total * 360).toFloat()
            val color = colors[index % colors.size]

            drawArc(
                color = color,
                startAngle = startAngle,
                sweepAngle = sweepAngle,
                useCenter = false,
                topLeft = Offset(
                    (size.width - canvasSize) / 2 + strokeWidth / 2,
                    (size.height - canvasSize) / 2 + strokeWidth / 2
                ),
                size = Size(canvasSize - strokeWidth, canvasSize - strokeWidth),
                style = Stroke(width = strokeWidth, cap = StrokeCap.Round)
            )

            startAngle += sweepAngle
        }
    }
}

private fun formatMoney(amount: Double): String {
    val format = NumberFormat.getCurrencyInstance(Locale.US)
    return format.format(amount)
}