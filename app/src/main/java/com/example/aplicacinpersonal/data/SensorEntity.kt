package com.example.aplicacinpersonal.data

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "lecturas_sensores")
data class SensorEntity(
    @PrimaryKey(autoGenerate = true) val id: Int = 0,
    val tipo: String,
    val valor: Float,
    val timestamp: Long
)
