package com.mygdx.primelogistics.android

import android.content.Intent
import android.os.Bundle
import android.widget.Button
import android.widget.ImageButton
import android.widget.TextView
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.mygdx.primelogistics.R
import com.mygdx.primelogistics.android.api.RetrofitClient
import com.mygdx.primelogistics.android.models.User
import com.mygdx.primelogistics.android.utils.SessionManager
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class UsuarioActivity : AppCompatActivity() {
    private var currentUser = 0
    private var currentUserName: String = ""
    private var currentCompanyName: String = ""
    private var currentIdentificationCardPath: String = ""
    private lateinit var sessionManager: SessionManager
    private lateinit var tvUserName: TextView
    private lateinit var tvCompanyName: TextView
    private lateinit var tvEmail: TextView
    private lateinit var btnHomeUsuario: ImageButton
    private lateinit var btnLogout: ImageButton
    private lateinit var btnIdentificationCard: Button

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_usuario)


        sessionManager = SessionManager(this)
        defineComponents()

        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.mainActivityUsuario)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

        btnLogout.setOnClickListener {
            sessionManager.clearSession()
            startActivity(Intent(this, LoginActivity::class.java))
            finish()
        }

        btnHomeUsuario.setOnClickListener {
            startActivity(Intent(this, ClientHomeActivity::class.java))
            finish()
        }

        btnIdentificationCard.setOnClickListener {
            val intent = if (currentIdentificationCardPath.isBlank()) {
                Intent(this, SubirDniActivity::class.java)
            } else {
                Intent(this, DescargarDniActivity::class.java).apply {
                    putExtra("identification_card_path", currentIdentificationCardPath)
                }
            }
            intent.putExtra("user_id", currentUser)
            intent.putExtra("user_name", currentUserName)
            intent.putExtra("company_name", currentCompanyName)
            startActivity(intent)
        }

        loadCurrentUser()
    }

    private fun defineComponents() {
        tvUserName = findViewById(R.id.tvUserName)
        tvCompanyName = findViewById(R.id.tvCompanyName)
        tvEmail = findViewById(R.id.tvEmail)
        btnHomeUsuario = findViewById(R.id.btnHomeUsuario)
        btnLogout = findViewById(R.id.btnLogout)
        btnIdentificationCard = findViewById(R.id.btnIdentificationCard)
    }

    private fun loadCurrentUser() {
        val token = sessionManager.getAccessToken()

        if (token.isNullOrBlank()) {
            startActivity(Intent(this, LoginActivity::class.java))
            finish()
            return
        }

        CoroutineScope(Dispatchers.IO).launch {
            try {
                val response = RetrofitClient.api.getMe()

                withContext(Dispatchers.Main) {
                    if (response.isSuccessful && response.body() != null) {
                        bindUser(response.body()!!)
                    } else {
                        sessionManager.clearSession()
                        startActivity(Intent(this@UsuarioActivity, LoginActivity::class.java))
                        finish()
                    }
                }
            } catch (e: Exception) {
                withContext(Dispatchers.Main) {
                    tvUserName.text = "Error al cargar usuario"
                }
            }
        }
    }

    private fun bindUser(user: User) {
        currentUser = user.id
        currentUserName = user.nombre
        currentCompanyName = user.company?.name ?: "Sin empresa"
        currentIdentificationCardPath = user.identificationCardPath ?: ""
        tvUserName.text = currentUserName
        tvCompanyName.text = currentCompanyName
        tvEmail.text = user.email
        btnIdentificationCard.text = if (user.identificationCardPath.isNullOrBlank()) {
            "Subir DNI"
        } else {
            "Descargar DNI"
        }
    }
}
