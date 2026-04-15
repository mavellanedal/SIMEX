package com.mygdx.primelogistics.android

import android.content.Intent
import android.os.Bundle
import android.widget.ImageButton
import android.widget.TextView
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.mygdx.primelogistics.R
import com.mygdx.primelogistics.android.adapters.OpAdapter
import com.mygdx.primelogistics.android.api.RetrofitClient
import com.mygdx.primelogistics.android.models.OperationsViewModel
import com.mygdx.primelogistics.android.models.User
import com.mygdx.primelogistics.android.utils.SessionManager
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class ClientHomeActivity : AppCompatActivity() {
    private lateinit var sessionManager: SessionManager

    private lateinit var btnUser: ImageButton
    private lateinit var tvUserName: TextView
    private lateinit var recyclerRecent: RecyclerView
    private lateinit var adapterRecent: OpAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_client_home)

        sessionManager = SessionManager(this)

        btnUser = findViewById(R.id.btnUser)
        btnUser.setOnClickListener {
            startActivity(Intent(this@ClientHomeActivity, UsuarioActivity::class.java))
        }

        tvUserName = findViewById(R.id.txtUserName)

        recyclerRecent = findViewById(R.id.rvRecent)

        adapterRecent = OpAdapter(mutableListOf()) { _ ->
            // card click
        }

        recyclerRecent.layoutManager = LinearLayoutManager(this)
        recyclerRecent.adapter = adapterRecent

        val viewModel = ViewModelProvider(this).get(OperationsViewModel::class.java)

        viewModel.fetchOperations()

        viewModel.operationsList.observe(this) { lista ->
            adapterRecent.updateData(lista)
        }

        loadCurrentUser()
    }

    private fun loadCurrentUser() {
        val token = sessionManager.getAccessToken()

        if (!token.isNullOrBlank()) {
            CoroutineScope(Dispatchers.IO).launch {
                try {
                    val response = RetrofitClient.api.getMe("Bearer $token")

                    withContext(Dispatchers.Main) {
                        if (response.isSuccessful && response.body() != null) {
                            bindUserName(response.body()!!)
                        } else {
                            sessionManager.clearSession()
                            startActivity(
                                Intent(
                                    this@ClientHomeActivity,
                                    LoginActivity::class.java
                                )
                            )
                            finish()
                        }
                    }
                } catch (e: Exception) {
                    withContext(Dispatchers.Main) {
                        tvUserName.text = "Error al cargar el usuario."
                    }
                }
            }
        }
    }

    private fun bindUserName(user: User) {
        tvUserName.text = user.nombre
    }
}
