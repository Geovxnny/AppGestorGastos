package com.example.appgestorgastos

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.viewModels
import androidx.compose.foundation.layout.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.example.appgestorgastos.ui.screens.*
import com.example.appgestorgastos.ui.theme.GestorGastosTheme
import com.example.appgestorgastos.viewmodel.GastoViewModel
import com.example.appgestorgastos.ui.components.PieChart


class MainActivity : ComponentActivity() {
    private val viewModel: GastoViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            GestorGastosTheme {
                GestorGastosApp(viewModel)
            }
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun GestorGastosApp(viewModel: GastoViewModel) {
    val navController = rememberNavController()
    var selectedItem by remember { mutableStateOf(0) }

    Scaffold(
        bottomBar = {
            NavigationBar {
                NavigationBarItem(
                    icon = { Icon(Icons.Filled.List, contentDescription = "Lista") },
                    label = { Text("Gastos") },
                    selected = selectedItem == 0,
                    onClick = {
                        selectedItem = 0
                        navController.navigate("lista") {
                            popUpTo("lista") { inclusive = true }
                        }
                    }
                )
                NavigationBarItem(
                    icon = { Icon(Icons.Filled.Add, contentDescription = "Agregar") },
                    label = { Text("Agregar") },
                    selected = selectedItem == 1,
                    onClick = {
                        selectedItem = 1
                        navController.navigate("agregar")
                    }
                )
                NavigationBarItem(
                    icon = { Icon(Icons.Filled.PieChart, contentDescription = "Resumen") },
                    label = { Text("Resumen") },
                    selected = selectedItem == 2,
                    onClick = {
                        selectedItem = 2
                        navController.navigate("resumen")
                    }
                )
            }
        }
    ) { paddingValues ->
        NavHost(
            navController = navController,
            startDestination = "lista",
            modifier = Modifier.padding(paddingValues)
        ) {
            composable("lista") {
                ListaGastosScreen(viewModel, navController)
            }
            composable("agregar") {
                AgregarGastoScreen(viewModel, navController)
            }
            composable("resumen") {
                ResumenScreen(viewModel)
            }
        }
    }
}