package com.geovanny.appgestorgastos.data.model

import androidx.room.Entity
import androidx.room.PrimaryKey
import java.util.Date

@Entity(tableName = "gastos")
data class Gasto(
    @PrimaryKey(autoGenerate = true)
    val id: Int = 0,
    val descripcion: String,
    val monto: Double,
    val categoria: String,
    val fecha: Long = Date().time,
    val notas: String = ""
)

// Categorías predefinidas
object Categorias {
    val TODAS = listOf(
        "Alimentación",
        "Transporte",
        "Entretenimiento",
        "Salud",
        "Educación",
        "Servicios",
        "Compras",
        "Otros"
    )
}