package mx.edu.utng.scv.gestionestudiantes.ui.viewmodel

import androidx.compose.runtime.State
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.launch
import mx.edu.utng.scv.gestionestudiantes.data.model.Estudiante
import mx.edu.utng.scv.gestionestudiantes.data.model.EstudianteRequest
import mx.edu.utng.scv.gestionestudiantes.data.repository.EstudianteRepository

class EstudianteViewModel: ViewModel() {
    private val repository = EstudianteRepository()
    private val _estudiantes = mutableStateOf<List<Estudiante>>(emptyList())
    private val _estudianteSeleccionado = mutableStateOf<Estudiante?>(null)
    private val _isLoading = mutableStateOf(false)
    private val _error = mutableStateOf<String?>(null)
    private val _operacionExitosa = mutableStateOf(false)

    val estudiantes: State<List<Estudiante>> = _estudiantes
    val estudianteSeleccionado: State<Estudiante?> = _estudianteSeleccionado
    val isLoading: State<Boolean> = _isLoading
    val error: State<String?> = _error
    val operacionExitosa: State<Boolean> = _operacionExitosa

    init {
        cargarEstudiantes()
    }

    fun cargarEstudiantes() {
        viewModelScope.launch {
            _isLoading.value = true
            _error.value = null

            repository.obtenerEstudiantes()
                .onSuccess { lista ->
                    _estudiantes.value = lista
                }
                .onFailure { exception ->
                    _error.value = exception.message ?: "Error al cargar estudiantes"
                }
            _isLoading.value = false
        }
    }

    fun cargarEstudiante(id: Int) {
        viewModelScope.launch {
            _isLoading.value = true
            _error.value = null

            repository.obtenerEstudiante(id)
                .onSuccess { estudiante ->
                    _estudianteSeleccionado.value = estudiante
                }
                .onFailure { exception ->
                    _error.value = exception.message ?: "Error al cargar estudiante"
                }
            _isLoading.value = false
        }
    }

    fun crearEstudiante(
        nombre: String,
        edad: Int,
        carrera: String,
        promedio: Double
    ) {
        viewModelScope.launch {
            _isLoading.value = true
            _error.value = null
            _operacionExitosa.value = false

            val nuevoEstudiante = EstudianteRequest(
                nombre = nombre,
                edad = edad,
                carrera = carrera,
                promedio = promedio
            )

            repository.crearEstudiante(nuevoEstudiante)
                .onSuccess {
                    _operacionExitosa.value = true
                    cargarEstudiantes()
                }
                .onFailure { exception ->
                    _error.value = exception.message ?: "Error al crear estudiante"
                }
            _isLoading.value = false
        }
    }

    fun actualizarEstudiante(
        id: Int,
        nombre: String,
        edad: Int,
        carrera: String,
        promedio: Double
    ) {
        viewModelScope.launch {
            _isLoading.value = true
            _error.value = null
            _operacionExitosa.value = false

            val estudianteActualizado = EstudianteRequest(
                nombre = nombre,
                edad = edad,
                carrera = carrera,
                promedio = promedio
            )

            repository.actualizarEstudiante(id, estudianteActualizado)
                .onSuccess {
                    _operacionExitosa.value = true
                    cargarEstudiantes()
                }
                .onFailure { exception ->
                    _error.value = exception.message ?: "Error al actualizar estudiante"
                }
            _isLoading.value = false
        }
    }

    fun eliminarEstudiante(id: Int) {
        viewModelScope.launch {
            _isLoading.value = true
            _error.value = null

            repository.eliminarEstudiante(id)
                .onSuccess {
                    cargarEstudiantes()
                }
                .onFailure { exception ->
                    _error.value = exception.message ?:"Error al eliminar estudiante"
                }
            _isLoading.value = false
        }
    }

    fun limpiarError() {
        _error.value = null
    }

    fun respetarOperacionExitosa() {
        _operacionExitosa.value = false
    }

    fun limpiarEstudianteSeleccionado() {
        _estudianteSeleccionado.value = null
    }

}