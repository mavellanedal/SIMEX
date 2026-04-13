package com.mygdx.primelogistics.android

import android.content.Intent
import android.os.Bundle
import android.widget.Button
import android.widget.ImageButton
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.mygdx.primelogistics.R

class SubirDniActivity : AppCompatActivity() {
    private lateinit var btnHomeUsuario: ImageButton
    private lateinit var btnVolver: Button

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_subir_dni)
        var currentUser = intent.getSerializableExtra("user_id")
        defineComponents()

        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.mainActivitySubirDni)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

        btnHomeUsuario.setOnClickListener {
            volverAUsuario()
        }

        btnVolver.setOnClickListener {
            volverAUsuario()
        }
    }

    private fun defineComponents() {
        btnHomeUsuario = findViewById(R.id.btnHomeUsuario)
        btnVolver = findViewById(R.id.btnVolve)
    }

    private fun volverAUsuario() {
        startActivity(Intent(this, UsuarioActivity::class.java))
        finish()
    }
}
