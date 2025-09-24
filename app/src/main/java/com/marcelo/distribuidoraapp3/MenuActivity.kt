package com.marcelo.distribuidoraapp3

import android.Manifest
import android.content.pm.PackageManager
import android.os.Bundle
import android.widget.Button
import android.widget.TextView
import android.widget.Toast
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import com.google.android.gms.location.FusedLocationProviderClient
import com.google.android.gms.location.LocationServices
import com.google.android.gms.location.Priority
import com.google.android.gms.tasks.CancellationTokenSource
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.FirebaseDatabase

data class LastLocation(val lat: Double, val lng: Double, val timestamp: Long)

class MenuActivity : AppCompatActivity() {

    private lateinit var fused: FusedLocationProviderClient
    private lateinit var tvSaludo: TextView
    private lateinit var tvUbicacion: TextView
    private lateinit var btnGuardar: Button

    private val reqPerms = registerForActivityResult(
        ActivityResultContracts.RequestMultiplePermissions()
    ) { result ->
        val granted = (result[Manifest.permission.ACCESS_FINE_LOCATION] == true) ||
                (result[Manifest.permission.ACCESS_COARSE_LOCATION] == true)
        if (granted) obtenerYGuardarUbicacion()
        else toast("Se requieren permisos de ubicación")
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_menu)

        fused = LocationServices.getFusedLocationProviderClient(this)

        tvSaludo = findViewById(R.id.tvSaludo)
        tvUbicacion = findViewById(R.id.tvUbicacion)
        btnGuardar = findViewById(R.id.btnGuardarUbicacion)

        val email = FirebaseAuth.getInstance().currentUser?.email ?: "(sin correo)"
        tvSaludo.text = "Bienvenido: $email"

        btnGuardar.setOnClickListener { solicitarPermisosYGuardar() }

        // Guardar automáticamente al abrir
        solicitarPermisosYGuardar()
    }

    private fun solicitarPermisosYGuardar() {
        val fineOk = ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED
        val coarseOk = ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) == PackageManager.PERMISSION_GRANTED
        if (fineOk || coarseOk) obtenerYGuardarUbicacion()
        else reqPerms.launch(arrayOf(
            Manifest.permission.ACCESS_FINE_LOCATION,
            Manifest.permission.ACCESS_COARSE_LOCATION
        ))
    }

    private fun obtenerYGuardarUbicacion() {
        try {
            // 1) Intentamos lastLocation (rápido y barato)
            fused.lastLocation
                .addOnSuccessListener { loc ->
                    if (loc != null) {
                        actualizarYSubir(loc.latitude, loc.longitude)
                    } else {
                        // 2) Si viene null, pedimos una lectura actual
                        val cts = CancellationTokenSource()
                        fused.getCurrentLocation(
                            Priority.PRIORITY_BALANCED_POWER_ACCURACY,
                            cts.token
                        ).addOnSuccessListener { cur ->
                            if (cur != null) actualizarYSubir(cur.latitude, cur.longitude)
                            else toast("Ubicación no disponible. Verifica GPS o fija ubicación en el emulador.")
                        }.addOnFailureListener { e ->
                            toast("Error obteniendo ubicación actual: ${e.message}")
                        }
                    }
                }
                .addOnFailureListener { e ->
                    toast("Error ubic.: ${e.message}")
                }
        } catch (se: SecurityException) {
            toast("Faltan permisos de ubicación")
        }
    }

    private fun actualizarYSubir(lat: Double, lng: Double) {
        tvUbicacion.text = "Ubicación: $lat, $lng"
        val uid = FirebaseAuth.getInstance().currentUser?.uid ?: return
        val ref = FirebaseDatabase.getInstance()
            .getReference("users")
            .child(uid)
            .child("lastLocation")

        val data = LastLocation(lat, lng, System.currentTimeMillis())
        ref.setValue(data)
            .addOnSuccessListener { toast("Ubicación guardada en la nube") }
            .addOnFailureListener { e -> toast("Error guardando: ${e.message}") }
    }

    private fun toast(msg: String) = Toast.makeText(this, msg, Toast.LENGTH_SHORT).show()
}
