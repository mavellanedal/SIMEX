package com.mygdx.primelogistics.android

import android.content.Intent
import android.graphics.BitmapFactory
import android.os.Bundle
import android.os.Environment
import android.widget.Button
import android.widget.ImageButton
import android.widget.ImageView
import android.widget.TextView
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.lifecycle.lifecycleScope
import com.mygdx.primelogistics.R
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import java.io.ByteArrayOutputStream
import java.io.DataInputStream
import java.io.DataOutputStream
import java.io.File
import java.io.FileOutputStream
import java.net.Socket

class DescargarDniActivity : AppCompatActivity() {

    private lateinit var btnHomeUsuario: ImageButton
    private lateinit var btnDescargarDNI: Button
    private lateinit var btnReuploadDNI: Button
    private lateinit var btnVolver: Button
    private lateinit var tvUserName: TextView
    private lateinit var tvCompanyName: TextView
    private lateinit var tvArchivoDni: TextView
    private lateinit var ivDniPreview: ImageView

    private var currentUserId: Int = -1
    private var currentUserName: String = "Usuario"
    private var currentCompanyName: String = "Sin empresa"
    private var identificationCardPath: String = ""
    private var previewBytes: ByteArray? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_descargar_dni)

        definirComponentes()
        leerDatosIntent()
        configurarWindowInsets()
        cargarDatosUsuario()
        configurarEventos()

        if (identificationCardPath.isNotBlank()) {
            cargarVistaPrevia()
        } else {
            tvArchivoDni.text = "No hay documento disponible"
        }
    }

    private fun definirComponentes() {
        btnHomeUsuario = findViewById(R.id.btnHomeUsuario)
        btnDescargarDNI = findViewById(R.id.btnDescargarDNI)
        btnReuploadDNI = findViewById(R.id.btnReuploadDNI)
        btnVolver = findViewById(R.id.btnVolver)
        tvUserName = findViewById(R.id.tvUserName)
        tvCompanyName = findViewById(R.id.tvCompanyName)
        tvArchivoDni = findViewById(R.id.tvArchivoDni)
        ivDniPreview = findViewById(R.id.ivDniPreview)
    }

    private fun leerDatosIntent() {
        currentUserId = intent.getIntExtra("user_id", -1)
        identificationCardPath = intent.getStringExtra("identification_card_path") ?: ""
        currentUserName = intent.getStringExtra("user_name") ?: "Usuario"
        currentCompanyName = intent.getStringExtra("company_name") ?: "Sin empresa"
    }

    private fun configurarWindowInsets() {
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.mainActivityDescargarDni)) { view, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            view.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }
    }

    private fun cargarDatosUsuario() {
        tvUserName.text = currentUserName
        tvCompanyName.text = currentCompanyName

        if (identificationCardPath.isBlank()) {
            tvArchivoDni.text = "No hay documento disponible"
        } else {
            tvArchivoDni.text = identificationCardPath
        }
    }

    private fun configurarEventos() {
        btnHomeUsuario.setOnClickListener {
            volverAUsuario()
        }

        btnVolver.setOnClickListener {
            volverAUsuario()
        }

        btnReuploadDNI.setOnClickListener {
            volverASubirDni()
        }

        btnDescargarDNI.setOnClickListener {
            descargarDni()
        }
    }

    private fun volverAUsuario() {
        val intent = Intent(this, UsuarioActivity::class.java)
        startActivity(intent)
        finish()
    }

    private fun volverASubirDni() {
        val intent = Intent(this, SubirDniActivity::class.java)
        intent.putExtra("user_id", currentUserId)
        intent.putExtra("user_name", currentUserName)
        intent.putExtra("company_name", currentCompanyName)
        startActivity(intent)
        finish()
    }

    private fun cargarVistaPrevia() {
        tvArchivoDni.text = "Cargando vista previa..."

        lifecycleScope.launch {
            val fileBytes = descargarBytesServidor(identificationCardPath)
            previewBytes = fileBytes

            if (fileBytes == null) {
                tvArchivoDni.text = "No se pudo cargar la vista previa"
            } else {
                val bitmap = BitmapFactory.decodeByteArray(fileBytes, 0, fileBytes.size)

                if (bitmap != null) {
                    ivDniPreview.setImageBitmap(bitmap)
                    ivDniPreview.visibility = ImageView.VISIBLE
                    tvArchivoDni.text = identificationCardPath
                } else {
                    tvArchivoDni.text = "Vista previa no disponible"
                }
            }
        }
    }

    private fun descargarDni() {
        if (identificationCardPath.isBlank()) {
            tvArchivoDni.text = "No hay documento disponible"
        } else {
            tvArchivoDni.text = "Descargando documento..."

            lifecycleScope.launch {
                val fileBytes = previewBytes ?: descargarBytesServidor(identificationCardPath)
                val downloadedFile = guardarArchivoDescargado(identificationCardPath, fileBytes)

                if (downloadedFile != null) {
                    tvArchivoDni.text = "Documento descargado: ${downloadedFile.name}"
                } else {
                    tvArchivoDni.text = "Error al descargar el documento"
                }
            }
        }
    }

    private suspend fun descargarBytesServidor(storedFileName: String): ByteArray? {
        return withContext(Dispatchers.IO) {
            try {
                Socket("10.0.2.2", 5000).use { socket ->
                    val input = DataInputStream(socket.getInputStream())
                    val output = DataOutputStream(socket.getOutputStream())

                    output.writeUTF("DOWNLOAD")
                    output.writeUTF(storedFileName)
                    output.flush()

                    val exists = input.readBoolean()

                    if (!exists) {
                        null
                    } else {
                        val fileSize = input.readLong()
                        val buffer = ByteArrayOutputStream()
                        var remaining = fileSize

                        while (remaining > 0) {
                            val byteRead = input.read()

                            if (byteRead == -1) {
                                throw IllegalStateException("La descarga se ha interrumpido antes de tiempo")
                            }

                            buffer.write(byteRead)
                            remaining--
                        }

                        buffer.toByteArray()
                    }
                }
            } catch (e: Exception) {
                null
            }
        }
    }

    private suspend fun guardarArchivoDescargado(storedFileName: String, fileBytes: ByteArray?): File? {
        return withContext(Dispatchers.IO) {
            if (fileBytes == null) {
                null
            } else {
                try {
                    val downloadsDir = getExternalFilesDir(Environment.DIRECTORY_DOWNLOADS) ?: filesDir
                    val targetFile = File(downloadsDir, storedFileName)

                    FileOutputStream(targetFile).use { outputStream ->
                        outputStream.write(fileBytes)
                        outputStream.flush()
                    }

                    targetFile
                } catch (e: Exception) {
                    null
                }
            }
        }
    }
}
