package com.mygdx.primelogistics.android

import android.content.Intent
import android.graphics.BitmapFactory
import android.os.Bundle
import android.os.Environment
import android.util.Log
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
import com.mygdx.primelogistics.android.utils.DataSecurity
import com.mygdx.primelogistics.android.utils.HomeNavigator
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
        tvArchivoDni.text = identificationCardPath.ifBlank { "No hay documento" }
    }

    private fun configurarEventos() {
        btnHomeUsuario.setOnClickListener { HomeNavigator.navigateToHome(this) }
        btnVolver.setOnClickListener { volverAUsuario() }
        btnReuploadDNI.setOnClickListener { volverASubirDni() }
        btnDescargarDNI.setOnClickListener { descargarDni() }
    }

    private fun volverAUsuario() {
        startActivity(Intent(this, UsuarioActivity::class.java))
        finish()
    }

    private fun volverASubirDni() {
        val intent = Intent(this, SubirDniActivity::class.java).apply {
            putExtra("user_id", currentUserId)
            putExtra("user_name", currentUserName)
            putExtra("company_name", currentCompanyName)
        }
        startActivity(intent)
        finish()
    }

    private fun cargarVistaPrevia() {
        tvArchivoDni.text = "Descifrando vista previa..."

        lifecycleScope.launch {
            val encryptedData = descargarBytesServidor(identificationCardPath)

            if (encryptedData == null) {
                tvArchivoDni.text = "Error de conexión con el servidor"
            } else {
                try {
                    val decryptedData = withContext(Dispatchers.Default) {
                        DataSecurity.desencriptarDatos(encryptedData, currentUserId)
                    }
                    previewBytes = decryptedData

                    val bitmap = BitmapFactory.decodeByteArray(decryptedData, 0, decryptedData.size)
                    if (bitmap != null) {
                        ivDniPreview.setImageBitmap(bitmap)
                        ivDniPreview.visibility = ImageView.VISIBLE
                        tvArchivoDni.text = identificationCardPath.removeSuffix(".enc")
                    } else {
                        tvArchivoDni.text = "El archivo descifrado no es una imagen válida"
                    }
                } catch (e: Exception) {
                    Log.e("CRYPTO", "Error decodificando AES", e)
                    tvArchivoDni.text = "Error de seguridad: Clave inválida"
                }
            }
        }
    }

    private fun descargarDni() {
        if (identificationCardPath.isBlank()) {
            tvArchivoDni.text = "No hay documento disponible"
            return
        }

        lifecycleScope.launch {
            tvArchivoDni.text = "Guardando en el dispositivo..."

            val bytesParaGuardar = previewBytes ?: descargarBytesServidor(identificationCardPath)?.let {
                try {
                    DataSecurity.desencriptarDatos(it, currentUserId) } catch (e: Exception) { null }
            }

            val downloadedFile = guardarArchivoDescargado(identificationCardPath, bytesParaGuardar)

            if (downloadedFile != null) {
                tvArchivoDni.text = "Guardado en: ${downloadedFile.name}"
            } else {
                tvArchivoDni.text = "Error al guardar el archivo"
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

                    if (!input.readBoolean()) return@withContext null

                    val fileSize = input.readLong()
                    val buffer = ByteArrayOutputStream()
                    var remaining = fileSize

                    while (remaining > 0) {
                        val byteRead = input.read()
                        if (byteRead == -1) break
                        buffer.write(byteRead)
                        remaining--
                    }
                    buffer.toByteArray()
                }
            } catch (e: Exception) {
                null
            }
        }
    }

    private suspend fun guardarArchivoDescargado(storedFileName: String, fileBytes: ByteArray?): File? {
        if (fileBytes == null) return null
        return withContext(Dispatchers.IO) {
            try {
                val downloadsDir = getExternalFilesDir(Environment.DIRECTORY_DOWNLOADS) ?: filesDir
                val finalName = storedFileName.removeSuffix(".enc")
                val targetFile = File(downloadsDir, finalName)

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
