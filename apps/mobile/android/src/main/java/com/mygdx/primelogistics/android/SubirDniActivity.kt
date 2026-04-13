package com.mygdx.primelogistics.android

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.provider.OpenableColumns
import android.widget.Button
import android.widget.ImageButton
import android.widget.TextView
import androidx.activity.enableEdgeToEdge
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.mygdx.primelogistics.R

class SubirDniActivity : AppCompatActivity() {
    private lateinit var btnHomeUsuario: ImageButton
    private lateinit var btnVolver: Button
    private lateinit var btnSubirDNI: ImageButton
    private lateinit var tvSelectedFile: TextView
    private var selectedUri: Uri? = null



    private val pickDocument = registerForActivityResult(
        ActivityResultContracts.OpenDocument()
    ) { uri ->
        if (uri != null) {
            selectedUri = uri
            tvSelectedFile.text = getFileName(uri) ?: "Archivo seleccionado"

        }
    }


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_subir_dni)
        val currentUser = intent.getIntExtra("user_id", -1)
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

        btnSubirDNI.setOnClickListener {
            pickDocument.launch(arrayOf("image/*"))
        }
    }

    private fun defineComponents() {
        btnHomeUsuario = findViewById(R.id.btnHomeUsuario)
        btnVolver = findViewById(R.id.btnVolve)
        btnSubirDNI = findViewById(R.id.btnSubirDNI)
        tvSelectedFile = findViewById(R.id.tvNombreArchivo)

    }

    private fun volverAUsuario() {
        startActivity(Intent(this, UsuarioActivity::class.java))
        finish()
    }

    private fun getFileName(uri: Uri): String? {
        var fileName: String? = null

        val cursor = contentResolver.query(uri, null, null, null, null)
        cursor?.use {
            val nameIndex = it.getColumnIndex(OpenableColumns.DISPLAY_NAME)
            if (it.moveToFirst() && nameIndex >= 0) {
                fileName = it.getString(nameIndex)
            }
        }

        return fileName
    }

}
