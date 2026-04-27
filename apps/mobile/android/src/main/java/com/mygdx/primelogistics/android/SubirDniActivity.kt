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
import androidx.lifecycle.lifecycleScope
import com.mygdx.primelogistics.R
import com.mygdx.primelogistics.android.api.RetrofitClient
import com.mygdx.primelogistics.android.models.UpdateIdentificationCardPathRequest
import com.mygdx.primelogistics.android.utils.DataSecurity
import com.mygdx.primelogistics.android.utils.HomeNavigator
import com.mygdx.primelogistics.android.utils.SessionManager
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import java.io.DataInputStream
import java.io.DataOutputStream
import java.net.Socket

class SubirDniActivity : AppCompatActivity() {

    private lateinit var btnHomeUsuario: ImageButton
    private lateinit var btnVolver: Button
    private lateinit var btnSubirDNI: ImageButton
    private lateinit var sessionManager: SessionManager
    private lateinit var tvUserName: TextView
    private lateinit var tvCompanyName: TextView
    private lateinit var tvSelectedFile: TextView

    private var selectedUri: Uri? = null
    private var currentUserId: Int = -1
    private var currentUserName: String = "Usuario"
    private var currentCompanyName: String = "Sin empresa"

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_subir_dni)

        sessionManager = SessionManager(this)
        RetrofitClient.init { sessionManager.getAccessToken() }

        definirComponentes()
        leerDatosIntent()
        configurarWindowInsets()
        cargarDatosUsuario()
        configurarEventos()
    }

    private fun definirComponentes() {
        btnHomeUsuario = findViewById(R.id.btnHomeUsuario)
        btnVolver = findViewById(R.id.btnVolve)
        btnSubirDNI = findViewById(R.id.btnSubirDNI)
        tvUserName = findViewById(R.id.tvUserName)
        tvCompanyName = findViewById(R.id.tvCompanyName)
        tvSelectedFile = findViewById(R.id.tvNombreArchivo)
    }

    private fun leerDatosIntent() {
        currentUserId = intent.getIntExtra("user_id", -1)
        currentUserName = intent.getStringExtra("user_name") ?: "Usuario"
        currentCompanyName = intent.getStringExtra("company_name") ?: "Sin empresa"
    }

    private fun configurarWindowInsets() {
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.mainActivitySubirDni)) { view, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            view.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }
    }

    private fun cargarDatosUsuario() {
        tvUserName.text = currentUserName
        tvCompanyName.text = currentCompanyName
    }

    private fun configurarEventos() {
        btnHomeUsuario.setOnClickListener {
            HomeNavigator.navigateToHome(this)
        }

        btnVolver.setOnClickListener {
            volverAUsuario()
        }

        btnSubirDNI.setOnClickListener {
            pickDocument.launch(arrayOf("image/*"))
        }
    }

    private fun volverAUsuario() {
        val intent = Intent(this, UsuarioActivity::class.java)
        startActivity(intent)
        finish()
    }

    private val pickDocument = registerForActivityResult(
        ActivityResultContracts.OpenDocument()
    ) { uri ->
        if (uri != null) {
            selectedUri = uri
            subirArchivoSeleccionado(uri)
        }
    }

    private fun subirArchivoSeleccionado(uri: Uri) {
        val fileName = obtenerNombreArchivo(uri) ?: "archivo"

        if (currentUserId == -1) {
            tvSelectedFile.text = "Usuario invalido"
        } else {
            tvSelectedFile.text = "Subiendo $fileName..."

            lifecycleScope.launch {
                val savedFileName = subirArchivoServidor(currentUserId, uri)

                if (savedFileName == null) {
                    tvSelectedFile.text = "Error al subir archivo"
                } else {
                    val updated = actualizarRutaDni(savedFileName)

                    if (updated) {
                        tvSelectedFile.text = "Archivo subido correctamente"
                        volverAUsuario()
                    } else {
                        tvSelectedFile.text = "Archivo subido, pero no se pudo guardar la ruta"
                    }
                }
            }
        }
    }

    private suspend fun subirArchivoServidor(userId: Int, uri: Uri): String? {
            val fileBytes = leerBytesArchivo(uri) ?: return null
            val fileName = obtenerNombreArchivo(uri) ?: return null
            val encryptedData = DataSecurity.encriptarDatos(fileBytes, currentUserId)

            return withContext(Dispatchers.IO) {
                try {
                    Socket("10.0.2.2", 5000).use { socket ->
                        val input = DataInputStream(socket.getInputStream())
                        val output = DataOutputStream(socket.getOutputStream())

                        output.writeUTF("UPLOAD")
                        output.writeInt(userId)
                        output.writeUTF("$fileName.enc")
                        output.writeLong(encryptedData.size.toLong())
                        output.write(encryptedData)
                        output.flush()

                        val response = input.readUTF()
                        val savedFileName = input.readUTF()

                        if (response == "OK") savedFileName else null
                    }
                } catch (e: Exception) {
                    e.printStackTrace()
                    null
                }
            }
        }

    private fun leerBytesArchivo(uri: Uri): ByteArray? {
        return try {
            contentResolver.openInputStream(uri)?.use { input ->
                input.readBytes()
            }
        } catch (e: Exception) {
            null
        }
    }

    private suspend fun actualizarRutaDni(savedFileName: String): Boolean {
        val token = sessionManager.getAccessToken() ?: return false

        return try {
            val response = RetrofitClient.api.updateIdentificationCardPath(
                request = UpdateIdentificationCardPathRequest(savedFileName)
            )

            response.isSuccessful
        } catch (e: Exception) {
            false
        }
    }

    fun obtenerNombreArchivo(uri: Uri): String? {
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
