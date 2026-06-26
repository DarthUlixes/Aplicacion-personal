package com.example.aplicacinpersonal

import android.os.Bundle
import android.widget.Button
import android.widget.TextView
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.lifecycle.lifecycleScope
import com.example.aplicacinpersonal.data.AppDatabase
import com.example.aplicacinpersonal.data.SensorEntity
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_main)
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

        val tvStatus = findViewById<TextView>(R.id.tvStatus)
        val tvReadings = findViewById<TextView>(R.id.tvReadings)
        val btnSaveManual = findViewById<Button>(R.id.btnSaveManual)

        val db = AppDatabase.getDatabase(this@MainActivity)

        // Botón para guardar manualmente una lectura de prueba
        btnSaveManual.setOnClickListener {
            lifecycleScope.launch {
                db.sensorDao().insertarLectura(
                    SensorEntity(
                        tipo = "Manual",
                        valor = (60..100).random().toFloat(),
                        timestamp = System.currentTimeMillis()
                    )
                )
            }
        }

        // Consultar la base de datos periódicamente para mostrar las últimas lecturas
        lifecycleScope.launch {
            while (true) {
                val lecturas = db.sensorDao().obtenerTodas()
                if (lecturas.isNotEmpty()) {
                    tvStatus.text = "¡Datos recibidos y guardados!"
                    val ultima = lecturas.first()
                    tvReadings.text = "Última: ${ultima.tipo} - ${ultima.valor}\nTotal en BD: ${lecturas.size}"
                }
                delay(2000) // Actualizar cada 2 segundos
            }
        }
    }
}
