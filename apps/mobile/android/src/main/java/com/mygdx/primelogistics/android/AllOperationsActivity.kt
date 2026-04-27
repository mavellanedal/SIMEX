package com.mygdx.primelogistics.android

import android.content.Intent
import android.os.Bundle
import android.widget.ImageButton
import android.widget.TextView
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.mygdx.primelogistics.R
import com.mygdx.primelogistics.R.*
import com.mygdx.primelogistics.android.adapters.OpAdapter
import com.mygdx.primelogistics.android.api.RetrofitClient
import com.mygdx.primelogistics.android.models.Operation
import com.mygdx.primelogistics.android.utils.SessionManager
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class AllOperationsActivity : AppCompatActivity() {
    private lateinit var sessionManager: SessionManager
    private lateinit var graficosbtnvolver: ImageButton
    private lateinit var recyclerAllOps: RecyclerView
    private lateinit var adapterAllOps: OpAdapter
    private lateinit var btnHomeAllOps: ImageButton
    private var operations: MutableList<Operation> = mutableListOf()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(layout.activity_all_operations)

        ViewCompat.setOnApplyWindowInsetsListener(findViewById(id.mainAllOperations)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

        sessionManager = SessionManager(this)

        graficosbtnvolver = findViewById(id.btnVolver)

        RetrofitClient.init { sessionManager.getAccessToken() }
        recyclerAllOps = findViewById(id.rvAllOps)
        recyclerAllOps.layoutManager = LinearLayoutManager(this)

        adapterAllOps = OpAdapter(operations) { operation ->
            startActivity(DetalleOperacionActivity.createIntent(this, operation))
        }
        recyclerAllOps.adapter = adapterAllOps

        loadData()

        btnHomeAllOps = findViewById(id.btnHomeAllOps)

        graficosbtnvolver.setOnClickListener {
            startActivity(Intent(this, ClientHomeActivity::class.java))
            finish()
        }

        btnHomeAllOps.setOnClickListener {
            startActivity(Intent(this, ClientHomeActivity::class.java))
            finish()
        }
    }

    private fun loadData() {
        CoroutineScope(Dispatchers.IO).launch {
            try {
                val userResp = RetrofitClient.api.getMe()
                val opsResp = RetrofitClient.api.getUserOperations()

                withContext(Dispatchers.Main) {
                    if (opsResp.isSuccessful) {
                        val lista = opsResp.body() ?: emptyList()
                        adapterAllOps.updateData(lista)
                    } else if (opsResp.code() == 401) {
                        logout()
                    }
                }
            } catch (e: Exception) {
                withContext(Dispatchers.Main) {
                    Toast.makeText(
                        this@AllOperationsActivity,
                        "Sin conexión: Verificar internet o estado del servidor.",
                        Toast.LENGTH_LONG
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
