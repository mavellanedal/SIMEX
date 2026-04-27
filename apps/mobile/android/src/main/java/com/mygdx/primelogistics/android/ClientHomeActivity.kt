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
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.mygdx.primelogistics.R
import com.mygdx.primelogistics.android.adapters.OpAdapter
import com.mygdx.primelogistics.android.adapters.PropAdapter
import com.mygdx.primelogistics.android.api.RetrofitClient
import com.mygdx.primelogistics.android.models.Operation
import com.mygdx.primelogistics.android.utils.SessionManager
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class ClientHomeActivity : AppCompatActivity() {
    private lateinit var sessionManager: SessionManager
    private lateinit var tvUserName: TextView

    private lateinit var recyclerRecent: RecyclerView
    private lateinit var adapterRecent: OpAdapter

    private lateinit var recyclerProp: RecyclerView
    private lateinit var adapterProp: PropAdapter

    private var operations: MutableList<Operation> = mutableListOf()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_client_home)

        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.mainClientHome)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

        sessionManager = SessionManager(this)
        RetrofitClient.init { sessionManager.getAccessToken() }

        tvUserName = findViewById(R.id.tvUserName)

        recyclerRecent = findViewById(R.id.rvRecent)
        recyclerRecent.layoutManager = LinearLayoutManager(this)
        adapterRecent = OpAdapter(operations) { operation ->
            startActivity(DetalleOperacionActivity.createIntent(this, operation))
        }
        recyclerRecent.adapter = adapterRecent

        recyclerProp = findViewById(R.id.rvProp)
        recyclerProp.layoutManager = LinearLayoutManager(this)
        adapterProp = PropAdapter(operations) { operation ->
            startActivity(PropuestaActivity.createIntent(this, operation))
        }
        recyclerProp.adapter = adapterProp

        findViewById<ImageButton>(R.id.btnUser).setOnClickListener {
            startActivity(Intent(this, UsuarioActivity::class.java))
            finish()
        }

        findViewById<Button>(R.id.btnSeeAll).setOnClickListener {
            startActivity(Intent(this, AllOperationsActivity::class.java))
            finish()
        }
    }

    override fun onResume() {
        super.onResume()
        loadData()
    }

    private fun loadData() {
        CoroutineScope(Dispatchers.IO).launch {
            try {
                val userResp = RetrofitClient.api.getMe()
                val opsResp = RetrofitClient.api.getRecentUserOperations()

                withContext(Dispatchers.Main) {
                    if (userResp.isSuccessful && userResp.body() != null) {
                        tvUserName.text = userResp.body()?.nombre
                    } else {
                        tvUserName.text = "Null"
                    }

                    if (opsResp.isSuccessful) {
                        val lista = opsResp.body() ?: emptyList()
                        adapterRecent.updateData(lista)
                        adapterProp.updateData(lista)
                    } else if (opsResp.code() == 401) {
                        logout()
                    }
                }
            } catch (e: Exception) {
                withContext(Dispatchers.Main) {
                    android.widget.Toast.makeText(
                        this@ClientHomeActivity,
                        "Sin conexión: Verificar internet o estado del servidor.",
                        android.widget.Toast.LENGTH_LONG
                    ).show()
                    e.printStackTrace()
                }
            }
        }
    }

    private fun logout() {
        sessionManager.clearSession()
        val intent = Intent(this, LoginActivity::class.java)
        intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
        startActivity(intent)
        finish()
    }
}
