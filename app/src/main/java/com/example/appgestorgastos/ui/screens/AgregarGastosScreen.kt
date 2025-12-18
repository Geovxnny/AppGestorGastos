package com.example.appgestorgastos.ui.screens

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.example.appgestorgastos.data.model.Categorias
import com.example.appgestorgastos.data.model.Gasto
import com.example.appgestorgastos.viewmodel.GastoViewModel
import androidx.compose.material.icons.filled.AttachMoney
import androidx.compose.material.icons.filled.Category
import androidx.compose.material.icons.filled.Description
import androidx.compose.material.icons.filled.Note
import androidx.compose.material.icons.filled.Save

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AgregarGastoScreen(viewModel: GastoViewModel, navController: NavController) {
    var descripcion by remember { mutableStateOf("") }
    var monto by remember { mutableStateOf("") }
    var categoriaSeleccionada by remember { mutableStateOf(Categorias.TODAS[0]) }
    var notas by remember { mutableStateOf("") }
    var expandido by remember { mutableStateOf(false) }
    var mostrarError by remember { mutableStateOf(false) }
    var mensajeError by remember { mutableStateOf("") }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp)
            .verticalScroll(rememberScrollState()),
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        Text(
            text = "Agregar Nuevo Gasto",
            style = MaterialTheme.typography.headlineMedium
        )

        // Campo Descripción
        OutlinedTextField(
            value = descripcion,
            onValueChange = { descripcion = it },
            label = { Text("Descripción") },
            placeholder = { Text("Ej: Almuerzo") },
            modifier = Modifier.fillMaxWidth(),
            leadingIcon = {
                Icon(Icons.Filled.Description, contentDescription = null)
            },
            singleLine = true
        )

        // Campo Monto
        OutlinedTextField(
            value = monto,
            onValueChange = {
                if (it.isEmpty() || it.matches(Regex("^\\d*\\.?\\d{0,2}$"))) {
                    monto = it
                }
            },
            label = { Text("Monto") },
            placeholder = { Text("0.00") },
            modifier = Modifier.fillMaxWidth(),
            leadingIcon = {
                Icon(Icons.Filled.AttachMoney, contentDescription = null)
            },
            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Decimal),
            singleLine = true
        )

        // Selector de Categoría
        ExposedDropdownMenuBox(
            expanded = expandido,
            onExpandedChange = { expandido = !expandido }
        ) {
            OutlinedTextField(
                value = categoriaSeleccionada,
                onValueChange = {},
                readOnly = true,
                label = { Text("Categoría") },
                trailingIcon = {
                    ExposedDropdownMenuDefaults.TrailingIcon(expanded = expandido)
                },
                modifier = Modifier
                    .fillMaxWidth()
                    .menuAnchor(),
                leadingIcon = {
                    Icon(Icons.Filled.Category, contentDescription = null)
                }
            )

            ExposedDropdownMenu(
                expanded = expandido,
                onDismissRequest = { expandido = false }
            ) {
                Categorias.TODAS.forEach { categoria ->
                    DropdownMenuItem(
                        text = { Text(categoria) },
                        onClick = {
                            categoriaSeleccionada = categoria
                            expandido = false
                        }
                    )
                }
            }
        }

        // Campo Notas (Opcional)
        OutlinedTextField(
            value = notas,
            onValueChange = { notas = it },
            label = { Text("Notas (opcional)") },
            placeholder = { Text("Agrega detalles adicionales") },
            modifier = Modifier
                .fillMaxWidth()
                .height(120.dp),
            leadingIcon = {
                Icon(Icons.Filled.Note, contentDescription = null)
            },
            maxLines = 4
        )

        Spacer(modifier = Modifier.weight(1f))

        // Botón Guardar
        Button(
            onClick = {
                when {
                    descripcion.isBlank() -> {
                        mensajeError = "Por favor ingresa una descripción"
                        mostrarError = true
                    }
                    monto.isBlank() || monto.toDoubleOrNull() == null -> {
                        mensajeError = "Por favor ingresa un monto válido"
                        mostrarError = true
                    }
                    monto.toDouble() <= 0 -> {
                        mensajeError = "El monto debe ser mayor a 0"
                        mostrarError = true
                    }
                    else -> {
                        val nuevoGasto = Gasto(
                            descripcion = descripcion.trim(),
                            monto = monto.toDouble(),
                            categoria = categoriaSeleccionada,
                            notas = notas.trim()
                        )
                        viewModel.insertGasto(nuevoGasto)

                        // Limpiar formulario
                        descripcion = ""
                        monto = ""
                        categoriaSeleccionada = Categorias.TODAS[0]
                        notas = ""

                        // Navegar a lista
                        navController.navigate("lista") {
                            popUpTo("lista") { inclusive = true }
                        }
                    }
                }
            },
            modifier = Modifier
                .fillMaxWidth()
                .height(56.dp)
        ) {
            Icon(Icons.Filled.Save, contentDescription = null)
            Spacer(modifier = Modifier.width(8.dp))
            Text("Guardar")
        }

        // Botón Cancelar
        OutlinedButton(
            onClick = {
                descripcion = ""
                monto = ""
                categoriaSeleccionada = Categorias.TODAS[0]
                notas = ""
            },
            modifier = Modifier.fillMaxWidth()
        ) {
            Text("Limpiar Formulario")
        }
    }

    // Snackbar para errores
    if (mostrarError) {
        Snackbar(
            modifier = Modifier.padding(16.dp),
            action = {
                TextButton(onClick = { mostrarError = false }) {
                    Text("OK")
                }
            }
        ) {
            Text(mensajeError)
        }
    }
}