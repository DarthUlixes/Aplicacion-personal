package com.example.aplicacinpersonal.data

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query

@Dao
interface SensorDao {
    @Insert
    suspend fun insertarLectura(lectura: SensorEntity)

    @Query("SELECT * FROM lecturas_sensores ORDER BY timestamp DESC")
    suspend fun obtenerTodas(): List<SensorEntity>
}
