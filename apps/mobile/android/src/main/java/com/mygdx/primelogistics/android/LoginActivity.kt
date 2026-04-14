package com.mygdx.primelogistics.android

import android.content.Intent
import android.os.Bundle
import android.text.method.HideReturnsTransformationMethod
import android.text.method.PasswordTransformationMethod
import android.widget.Button
import android.widget.EditText
import android.widget.ImageButton
import android.widget.TextView
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import com.mygdx.primelogistics.R
import com.mygdx.primelogistics.android.api.RetrofitClient
import com.mygdx.primelogistics.android.models.LoginRequest
import com.mygdx.primelogistics.android.utils.SessionManager
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class LoginActivity : AppCompatActivity() {
    private lateinit var etUsername: EditText
    private lateinit var etPassword: EditText
    private lateinit var tvNotification: TextView
    private lateinit var btnLogin: Button
    private lateinit var btnVisible: ImageButton
    private lateinit var btnBoatGame: ImageButton
    private lateinit var sessionManager: SessionManager
    private var isPasswordVisible = false

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_login)

        sessionManager = SessionManager(this)
        defineComponent()

        btnVisible.setOnClickListener {
            updatePasswordVisibility()
        }
        btnBoatGame.setOnClickListener {
            startActivity(Intent(this, AndroidLauncher::class.java))
        }
        btnLogin.setOnClickListener {
            if (checkFieldsNotEmpty()) {
                login()
            }
        }
    }

    private fun defineComponent() {
        etUsername = findViewById(R.id.etUsernameLogin)
        etPassword = findViewById(R.id.etPasswordLogin)
        btnLogin = findViewById(R.id.btnLogin)
        btnVisible = findViewById(R.id.btnVisible)
        btnBoatGame = findViewById(R.id.boatLogin)
        tvNotification = findViewById(R.id.tvNotification)
    }

    private fun updatePasswordVisibility() {
        if (isPasswordVisible) {
            etPassword.transformationMethod = HideReturnsTransformationMethod.getInstance()
            isPasswordVisible = false
            btnVisible.setImageResource(R.drawable.invisible)
        } else {
            etPassword.transformationMethod = PasswordTransformationMethod.getInstance()
            isPasswordVisible = true
            btnVisible.setImageResource(R.drawable.visible)
        }
    }

    private fun login() {
        val username = etUsername.text.toString().trim()
        val password = etPassword.text.toString()

        val loginRequest = LoginRequest(username, password)

        btnLogin.isEnabled = false
        btnLogin.text="Entrando..."

        CoroutineScope(Dispatchers.IO).launch {
            try {
                val response = RetrofitClient.api.login(loginRequest)

                withContext(Dispatchers.Main) {
                    if (response.isSuccessful && response.body() != null) {
                        val auth = response.body()!!
                        sessionManager.saveAccessToken(auth.accessToken)
                        tvNotification.text = "Login correcto: ${auth.user.nombre}"
                        startActivity(Intent(this@LoginActivity, UsuarioActivity::class.java))
                        finish()
                    } else {
                        tvNotification.text = "Error de usuario o contrasena."
                        btnLogin.isEnabled = true
                        btnLogin.text="Login"
                    }
                }
            } catch (e: Exception) {
                withContext(Dispatchers.Main) {
                    tvNotification.text = "Error de conexion"
                    btnLogin.isEnabled = true
                    btnLogin.text="Login"
                }
            }
        }
    }

    private fun checkFieldsNotEmpty(): Boolean {
        val usernameEmpty = etUsername.text.isNullOrBlank()
        val passwordEmpty = etPassword.text.isNullOrBlank()

        if (usernameEmpty && passwordEmpty) {
            etUsername.error = "Introduce el nombre de usuario."
            etPassword.error = "Introduce la contrasena."
            tvNotification.text = "Introduce el nombre de usuario y la contrasena."
            return false
        }

        if (usernameEmpty) {
            etUsername.error = "Introduce el nombre de usuario."
            tvNotification.text = "Introduce el nombre de usuario."
            return false
        }

        if (passwordEmpty) {
            etPassword.error = "Introduce la contrasena."
            tvNotification.text = "Introduce la contrasena."
            return false
        }

        return true
    }
}
