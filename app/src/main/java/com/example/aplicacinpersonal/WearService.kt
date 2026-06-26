package com.example.aplicacinpersonal

import com.example.aplicacinpersonal.data.AppDatabase
import com.example.aplicacinpersonal.data.SensorEntity
import com.google.android.gms.wearable.DataEventBuffer
import com.google.android.gms.wearable.DataMapItem
import com.google.android.gms.wearable.WearableListenerService
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class WearService : WearableListenerService() {

    private val scope = CoroutineScope(Dispatchers.IO)

    override fun onDataChanged(dataEvents: DataEventBuffer) {
        for (event in dataEvents) {
            if (event.type == com.google.android.gms.wearable.DataEvent.TYPE_CHANGED) {
                val path = event.dataItem.uri.path
                if (path == "/lectura_sensores") {
                    val dataMap = DataMapItem.fromDataItem(event.dataItem).dataMap
                    val ritmo = dataMap.getFloat("ritmo")
                    val ax = dataMap.getFloat("accel_x")
                    val ay = dataMap.getFloat("accel_y")
                    val az = dataMap.getFloat("accel_z")
                    val timestamp = dataMap.getLong("timestamp")

                    // Guardar en la base de datos
                    guardarDatos(ritmo, ax, ay, az, timestamp)
                }
            }
        }
    }

    private fun guardarDatos(ritmo: Float, ax: Float, ay: Float, az: Float, time: Long) {
        val db = AppDatabase.getDatabase(this)
        scope.launch {
            // Guardamos el ritmo cardíaco
            db.sensorDao().insertarLectura(SensorEntity(tipo = "Ritmo Cardíaco", valor = ritmo, timestamp = time))
            // Guardamos los 3 ejes del acelerómetro
            db.sensorDao().insertarLectura(SensorEntity(tipo = "Accel X", valor = ax, timestamp = time))
            db.sensorDao().insertarLectura(SensorEntity(tipo = "Accel Y", valor = ay, timestamp = time))
            db.sensorDao().insertarLectura(SensorEntity(tipo = "Accel Z", valor = az, timestamp = time))
        }
    }
}
