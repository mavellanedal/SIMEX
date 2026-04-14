package com.mygdx.primelogistics.android

<<<<<<< HEAD
<<<<<<< HEAD
<<<<<<< HEAD
<<<<<<< HEAD
import android.app.Notification
import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.ImageButton
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import android.text.method.HideReturnsTransformationMethod
import android.text.method.PasswordTransformationMethod
import android.widget.TextView
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
    private lateinit var tvNotification: TextView
    private lateinit var btnLogin: Button
    private lateinit var btnVisible: ImageButton
    private var isPasswordVisible = false

=======
=======
import android.app.Notification
>>>>>>> aa3483a (feat: remove toast and add tvNotification)
import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.ImageButton
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import android.text.method.HideReturnsTransformationMethod
import android.text.method.PasswordTransformationMethod
import android.widget.TextView
import android.widget.Toast
import com.mygdx.primelogistics.R
import com.mygdx.primelogistics.android.api.RetrofitClient
import com.mygdx.primelogistics.android.models.LoginRequest
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class LoginActivity : AppCompatActivity() {
<<<<<<< HEAD
<<<<<<< HEAD
>>>>>>> 9001d2c (chore: fix activity theme configuration and modify AndoridManifest.xml)
=======
    lateinit var etUsername: EditText
    lateinit var etPassword: EditText
    lateinit var btnLogin: Button
=======
    private lateinit var etUsername: EditText
    private lateinit var etPassword: EditText
    private lateinit var tvNotification: TextView
    private lateinit var btnLogin: Button
    private lateinit var btnVisible: ImageButton
    private var isPasswordVisible = false
>>>>>>> 005e885 (feat: update loginActivity)

>>>>>>> 6d1497b (feat: update login activity)
=======
=======
import android.app.Notification
>>>>>>> aa3483a (feat: remove toast and add tvNotification)
import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.ImageButton
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import android.text.method.HideReturnsTransformationMethod
import android.text.method.PasswordTransformationMethod
import android.widget.TextView
import android.widget.Toast
import com.mygdx.primelogistics.R
import com.mygdx.primelogistics.android.api.RetrofitClient
import com.mygdx.primelogistics.android.models.LoginRequest
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class LoginActivity : AppCompatActivity() {
<<<<<<< HEAD
<<<<<<< HEAD
>>>>>>> 9001d2c (chore: fix activity theme configuration and modify AndoridManifest.xml)
=======
    lateinit var etUsername: EditText
    lateinit var etPassword: EditText
    lateinit var btnLogin: Button
=======
    private lateinit var etUsername: EditText
    private lateinit var etPassword: EditText
    private lateinit var tvNotification: TextView
    private lateinit var btnLogin: Button
    private lateinit var btnVisible: ImageButton
    private var isPasswordVisible = false
>>>>>>> 005e885 (feat: update loginActivity)

>>>>>>> 6d1497b (feat: update login activity)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_login)
<<<<<<< HEAD
<<<<<<< HEAD
<<<<<<< HEAD
<<<<<<< HEAD
<<<<<<< HEAD
<<<<<<< HEAD
<<<<<<< HEAD
<<<<<<< HEAD
=======
>>>>>>> 005e885 (feat: update loginActivity)
=======
>>>>>>> 005e885 (feat: update loginActivity)
        defineComponent()

        btnVisible.setOnClickListener {
            updatePasswordVisibility()
        }
<<<<<<< HEAD
<<<<<<< HEAD
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
        tvNotification = findViewById(R.id.tvNotification)
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

        btnLogin.isEnabled = false

        CoroutineScope(Dispatchers.IO).launch {
            try {
                val response = RetrofitClient.api.login(loginRequest)

                withContext(Dispatchers.Main) {
                    if (response.isSuccessful) {
                        tvNotification.text = "Login correcto"
                    } else {
                        tvNotification.text = "Error de usuario o contraseña."
                        btnLogin.isEnabled = true
                    }
                }
            } catch (e: Exception) {
                withContext(Dispatchers.Main) {
                    tvNotification.text = "Error de conexión"
                    btnLogin.isEnabled = true
                }
            }
        }
    }

    private fun checkFieldsNotEmpty(): Boolean {
        var usernameEmpty = etUsername.text.isNullOrBlank()
        var passwordEmpty = etPassword.text.isNullOrBlank()
        var result = false

        if (usernameEmpty && passwordEmpty){
            etUsername.error = "Introduce el nombre de usuario."
            etPassword.error = "Introduce la contraseña."
            tvNotification.text = "Introduce el nombre de usuario y la contraseña."
<<<<<<< HEAD
        } else if (usernameEmpty) {
            etUsername.error = "Introduce el nombre de usuario."
            tvNotification.text = "Introduce el nombre de usuario."
        } else if (passwordEmpty) {
            etPassword.error = "Introduce la contraseña."
            tvNotification.text = "Introduce la contraseña."
        } else {
            result = true
        }
        return result
    }
}
=======
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
=======
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.activity_login)) { v, insets ->
>>>>>>> f2191cf (feat: create activity login)
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
=======
        definirComponente()
        btnLogin.setOnClickListener {
>>>>>>> 6d1497b (feat: update login activity)
=======
        btnLogin.setOnClickListener {
=======
        btnLogin.setOnClickListener {
>>>>>>> 005e885 (feat: update loginActivity)
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
<<<<<<< HEAD
        tvNotification = findViewById(R.id.tvNotification)
=======
>>>>>>> 005e885 (feat: update loginActivity)
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

<<<<<<< HEAD
        btnLogin.isEnabled = false

=======
>>>>>>> 005e885 (feat: update loginActivity)
        CoroutineScope(Dispatchers.IO).launch {
            try {
                val response = RetrofitClient.api.login(loginRequest)

                withContext(Dispatchers.Main) {
                    if (response.isSuccessful) {
<<<<<<< HEAD
                        tvNotification.text = "Login correcto"
                    } else {
                        tvNotification.text = "Error de usuario o contraseña."
                        btnLogin.isEnabled = true
=======
                        Toast.makeText(this@LoginActivity, "Login correcto", Toast.LENGTH_SHORT).show()
                    } else {
                        Toast.makeText(this@LoginActivity, "Error al iniciar sesión", Toast.LENGTH_SHORT).show()
>>>>>>> 005e885 (feat: update loginActivity)
                    }
                }
            } catch (e: Exception) {
                withContext(Dispatchers.Main) {
<<<<<<< HEAD
                    tvNotification.text = "Error de conexión"
                    btnLogin.isEnabled = true
=======
                    Toast.makeText(this@LoginActivity, "Error de conexión", Toast.LENGTH_SHORT).show()
>>>>>>> 005e885 (feat: update loginActivity)
                }
            }
        }
    }

    private fun checkFieldsNotEmpty(): Boolean {
        var usernameEmpty = etUsername.text.isNullOrBlank()
        var passwordEmpty = etPassword.text.isNullOrBlank()
        var result = false

        if (usernameEmpty && passwordEmpty){
<<<<<<< HEAD
            etUsername.error = "Introduce el nombre de usuario."
            etPassword.error = "Introduce la contraseña."
            tvNotification.text = "Introduce el nombre de usuario y la contraseña."
        } else if (usernameEmpty) {
            etUsername.error = "Introduce el nombre de usuario."
            tvNotification.text = "Introduce el nombre de usuario."
        } else if (passwordEmpty) {
            etPassword.error = "Introduce la contraseña."
            tvNotification.text = "Introduce la contraseña."
        } else {
            result = true
>>>>>>> 005e885 (feat: update loginActivity)
        }
        return result
    }
}
<<<<<<< HEAD
>>>>>>> 9001d2c (chore: fix activity theme configuration and modify AndoridManifest.xml)
=======
>>>>>>> f2191cf (feat: create activity login)
=======
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
=======
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.activity_login)) { v, insets ->
>>>>>>> f2191cf (feat: create activity login)
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
=======
        definirComponente()
        btnLogin.setOnClickListener {
>>>>>>> 6d1497b (feat: update login activity)
=======
            Toast.makeText(this, "Introduce el nombre de usuario y la contraseña!", Toast.LENGTH_SHORT).show()
=======
>>>>>>> aa3483a (feat: remove toast and add tvNotification)
        } else if (usernameEmpty) {
            etUsername.error = "Introduce el nombre de usuario."
            tvNotification.text = "Introduce el nombre de usuario."
        } else if (passwordEmpty) {
            etPassword.error = "Introduce la contraseña."
            tvNotification.text = "Introduce la contraseña."
        } else {
            result = true
>>>>>>> 005e885 (feat: update loginActivity)
        }
        return result
    }
}
<<<<<<< HEAD
>>>>>>> 9001d2c (chore: fix activity theme configuration and modify AndoridManifest.xml)
=======
>>>>>>> f2191cf (feat: create activity login)
