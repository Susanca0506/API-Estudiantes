package mx.edu.utng.scv.gestionestudiantes.data.remote

import mx.edu.utng.scv.gestionestudiantes.data.model.Estudiante
import mx.edu.utng.scv.gestionestudiantes.data.model.EstudianteRequest
import retrofit2.http.*
import retrofit2.Response

interface ApiService {

    @GET("estudiantes/")
    suspend fun obtenerEstudiantes(
        @Query("skip") skip: Int = 0,
        @Query("limit") limit: Int = 100
    ): Response<List<Estudiante>>

    @GET("estudiantes/{id}")
    suspend fun obtenerEstudiante(
        @Path("id") id: Int
    ): Response<Estudiante>

    @POST("estudiantes/")
    suspend fun crearEstudiante(
        @Body estudiante: EstudianteRequest
    ): Response<Estudiante>

     @PUT("estudiantes/{id}")
     suspend fun actualizarEstudiante(
          @Path("id") id: Int,
          @Body estudiante: EstudianteRequest
     ): Response<Estudiante>

     @DELETE("estudiantes/{id}")
     suspend fun eliminarEstudiante(
         @Path("id") id: Int
     ): Response<Unit>
}