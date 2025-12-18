package com.example.appgestorgastos.ui.components

import androidx.compose.foundation.Canvas
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.size
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp

@Composable
fun PieChart(
    data: Map<String, Double>,
    colors: List<Color>,
    modifier: Modifier = Modifier,
    chartSize: Dp = 200.dp,
    strokeWidth: Dp = 30.dp
) {
    val total = data.values.sum()
    val proportions = data.values.map {
        if (total == 0.0) 0f else (it / total).toFloat() * 360f
    }

    var startAngle = -90f

    Box(
        modifier = modifier.size(chartSize),
        contentAlignment = Alignment.Center
    ) {
        Canvas(modifier = Modifier.size(chartSize)) {
            proportions.forEachIndexed { index, sweepAngle ->
                val color = colors.getOrElse(index) { Color.Gray }
                drawArc(
                    color = color,
                    startAngle = startAngle,
                    sweepAngle = sweepAngle,
                    useCenter = false,
                    style = Stroke(width = strokeWidth.toPx())
                )
                startAngle += sweepAngle
            }
        }
    }
}