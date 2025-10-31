package mx.edu.utng.scv.gestionestudiantes.ui.screens

import android.graphics.Paint
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material3.*
import androidx.compose.material3.HorizontalDivider
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.input.pointer.stylusHoverIcon
import androidx.compose.ui.layout.ModifierLocalBeyondBoundsLayout
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.intl.Locale
import androidx.compose.ui.unit.dp
import mx.edu.utng.scv.gestionestudiantes.data.model.Estudiante
import java.text.SimpleDateFormat
import java.util.*

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun DetalleEstudianteScreen(
    estudiante: Estudiante?,
    isLoading: Boolean,
    onBackClick: () -> Unit,
    onEditarClick: (Int) -> Unit
){
    Scaffold(topBar = {
        TopAppBar(
            title = {Text("Detalle del Estudiante")},
            navigationIcon = {
                IconButton(onClick = onBackClick) {
                    Icon(
                        imageVector = Icons.Default.ArrowBack,
                        contentDescription = "Volver"
                    )
                }
            },
            actions = {
                if(estudiante != null){
                    IconButton(onClick = {onEditarClick(estudiante.id)}) {
                        Icon(
                            imageVector = Icons.Default.Edit,
                            contentDescription = "Editar"
                        )
                    }
                }
            },
            colors = TopAppBarDefaults.topAppBarColors(
                containerColor = MaterialTheme.colorScheme.primaryContainer,
                titleContentColor = MaterialTheme.colorScheme.onPrimaryContainer
            )
        )
    }
    ) { paddingValues ->
        Box(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues)
        ){
            when {
                isLoading -> {
                    CircularProgressIndicator(
                        modifier = Modifier.align(Alignment.Center)
                    )
                }

                estudiante == null -> {
                    Text(
                        text = "Estudiante no encontrado",
                        modifier = Modifier.align(Alignment.Center),
                        style = MaterialTheme.typography.bodyLarge
                    )
                }

                else -> {
                    Column(modifier = Modifier
                        .fillMaxSize()
                        .padding(24.dp),
                        verticalArrangement = Arrangement.spacedBy(16.dp)
                    ) {
                        Card(modifier = Modifier.fillMaxWidth(),
                            elevation = CardDefaults.cardElevation(defaultElevation = 4.dp)
                        ) {
                            Column(modifier = Modifier.padding(20.dp),
                                verticalArrangement = Arrangement.spacedBy(12.dp)
                            ) {
                                DetalleItem(
                                    label = "ID",
                                    valor = estudiante.id.toString()
                                )

                                HorizontalDivider(
                                    Modifier,
                                    DividerDefaults.Thickness,
                                    DividerDefaults.color
                                )

                                DetalleItem(
                                    label = "Nombre Completo",
                                    valor = estudiante.nombre
                                )

                                HorizontalDivider(
                                    Modifier,
                                    DividerDefaults.Thickness,
                                    DividerDefaults.color
                                )

                                DetalleItem(
                                    label = "Edad",
                                    valor = "${estudiante.edad} años"
                                )

                                HorizontalDivider(
                                    Modifier,
                                    DividerDefaults.Thickness,
                                    DividerDefaults.color
                                )

                                DetalleItem(
                                    label = "Promedio Académico",
                                    valor = String.format("%.2f", estudiante.promedio),
                                    colorValor = when {
                                        estudiante.promedio >= 90 -> MaterialTheme.colorScheme.primary
                                        estudiante.promedio >= 70 -> MaterialTheme.colorScheme.tertiary
                                        else -> MaterialTheme.colorScheme.error
                                    }
                                )

                                HorizontalDivider(
                                    Modifier,
                                    DividerDefaults.Thickness,
                                    DividerDefaults.color
                                )

                                DetalleItem(
                                    label = "Fecha de Registro",
                                    valor = formatearFecha(estudiante.fechaRegistro)
                                )
                            }

                            Card(modifier = Modifier.fillMaxWidth(),
                                colors = CardDefaults.cardColors(
                                    containerColor = MaterialTheme.colorScheme.secondaryContainer
                                )
                            ) {
                                Column(
                                    modifier = Modifier.padding(20.dp)
                                ) {
                                    Text(
                                        text = "Rendimiento Académico",
                                        style = MaterialTheme.typography.titleMedium,
                                        fontWeight = FontWeight.Bold
                                    )

                                    Spacer(modifier = Modifier.height(12.dp))

                                    val clasificacion = when {
                                        estudiante.promedio >= 90 -> "Excelente"
                                        estudiante.promedio >= 80 -> "Muy Bueno"
                                        estudiante.promedio >= 70 -> "Bueno"
                                        estudiante.promedio >= 60 -> "Suficiente"
                                        else -> "Necesita mejorar"
                                    }

                                    Text(
                                        text = "Clasificación: $clasificacion",
                                        style = MaterialTheme.typography.bodyLarge
                                    )

                                }
                            }
                        }
                    }
                }
            }
        }

    }
}

@Composable
fun DetalleItem(
    label: String,
    valor: String,
    colorValor: androidx.compose.ui.graphics.Color = MaterialTheme.colorScheme.onSurface
){
    Column {
        Text(
            text = label,
            style = MaterialTheme.typography.labelMedium,
            color = MaterialTheme.colorScheme.onSurfaceVariant
        )

        Spacer(modifier = Modifier.height(4.dp))

        Text(
            text = valor,
            style = MaterialTheme.typography.bodyLarge,
            fontWeight = FontWeight.Medium,
            color = colorValor
        )
    }
}

fun formatearFecha(fecha: String): String{
    return try{
        val inputFormat = SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss", java.util.Locale.getDefault())
        val outputFormat = SimpleDateFormat("dd/MM/yyyy HH:mm", java.util.Locale.getDefault())
        val date = inputFormat.parse(fecha)
        date?.let { outputFormat.format(it) } ?: fecha
    } catch (e: Exception){
        fecha
    }
}

