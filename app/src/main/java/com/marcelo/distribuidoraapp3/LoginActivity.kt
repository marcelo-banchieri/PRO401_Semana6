package com.marcelo.distribuidoraapp3

import android.content.Intent
import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.google.firebase.auth.FirebaseAuth

class LoginActivity : AppCompatActivity() {

    private lateinit var auth: FirebaseAuth

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_login) // usa tu XML

        auth = FirebaseAuth.getInstance()

        val etEmail: EditText = findViewById(R.id.etEmail)
        val etPass: EditText  = findViewById(R.id.etPass)
        val btnLogin: Button = findViewById(R.id.btnLogin)
        val btnRegistro: Button = findViewById(R.id.btnRegistro)

        btnRegistro.setOnClickListener {
            val email = etEmail.text.toString().trim()
            val pass  = etPass.text.toString().trim()
            if (email.isEmpty() || pass.isEmpty()) {
                toast("Completa email y contraseña"); return@setOnClickListener
            }
            auth.createUserWithEmailAndPassword(email, pass)
                .addOnSuccessListener {
                    toast("Cuenta creada")
                    goToMenu()
                }
                .addOnFailureListener { e -> toast("Error registro: ${e.message}") }
        }

        btnLogin.setOnClickListener {
            val email = etEmail.text.toString().trim()
            val pass  = etPass.text.toString().trim()
            if (email.isEmpty() || pass.isEmpty()) {
                toast("Completa email y contraseña"); return@setOnClickListener
            }
            auth.signInWithEmailAndPassword(email, pass)
                .addOnSuccessListener { goToMenu() }
                .addOnFailureListener { e -> toast("Error login: ${e.message}") }
        }
    }

    private fun goToMenu() {
        startActivity(Intent(this, MenuActivity::class.java))
        finish()
    }

    private fun toast(msg: String) =
        Toast.makeText(this, msg, Toast.LENGTH_SHORT).show()
}
