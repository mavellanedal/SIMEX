package com.mygdx.primelogistics.android

import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.ImageButton
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import android.text.method.HideReturnsTransformationMethod
import android.text.method.PasswordTransformationMethod
import android.widget.Toast
import com.mygdx.primelogistics.R
import com.mygdx.primelogistics.android.api.RetrofitClient
import com.mygdx.primelogistics.android.models.LoginRequest
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class LoginActivity : AppCompatActivity() {
    private lateinit var etUsername: EditText
    private lateinit var etPassword: EditText
    private lateinit var btnLogin: Button
    private lateinit var btnVisible: ImageButton
    private var isPasswordVisible = false

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_login)
        defineComponent()

        btnVisible.setOnClickListener {
            updatePasswordVisibility()
        }
        btnLogin.setOnClickListener {
            if (checkFieldsNotEmpty()) {
                login()
            }
        }
    }

    override fun onDestroy() {
        super.onDestroy()
    }

    private fun defineComponent() {
        etUsername = findViewById(R.id.etUsernameLogin)
        etPassword = findViewById(R.id.etPasswordLogin)
        btnLogin = findViewById(R.id.btnLogin)
        btnVisible = findViewById(R.id.btnVisible)
    }


    private fun updatePasswordVisibility(){
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

        CoroutineScope(Dispatchers.IO).launch {
            try {
                val response = RetrofitClient.api.login(loginRequest)

                withContext(Dispatchers.Main) {
                    if (response.isSuccessful) {
                        Toast.makeText(this@LoginActivity, "Login correcto", Toast.LENGTH_SHORT).show()
                    } else {
                        Toast.makeText(this@LoginActivity, "Error al iniciar sesión", Toast.LENGTH_SHORT).show()
                    }
                }
            } catch (e: Exception) {
                withContext(Dispatchers.Main) {
                    Toast.makeText(this@LoginActivity, "Error de conexión", Toast.LENGTH_SHORT).show()
                }
            }
        }
    }

    private fun checkFieldsNotEmpty(): Boolean {
        var usernameEmpty = etUsername.text.isNullOrBlank()
        var passwordEmpty = etPassword.text.isNullOrBlank()
        var result = false

        if (usernameEmpty && passwordEmpty){
            Toast.makeText(this, "Introduce el nombre de usuario y la contraseña!", Toast.LENGTH_SHORT).show()
        } else if (usernameEmpty) {
            Toast.makeText(this, "Introduce el nombre de usuario!", Toast.LENGTH_SHORT).show()
        } else if (passwordEmpty) {
            Toast.makeText(this, "Introduce la contraseña!", Toast.LENGTH_SHORT).show()
        } else {
            result = true
        }

        return result
    }
}
