package com.mygdx.primelogistics.android

import android.content.Intent
import android.os.Bundle
import android.widget.Button
import android.widget.ImageButton
import android.widget.TextView
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.github.mikephil.charting.charts.PieChart
import com.github.mikephil.charting.data.PieData
import com.github.mikephil.charting.data.PieDataSet
import com.github.mikephil.charting.data.PieEntry
import com.github.mikephil.charting.utils.ColorTemplate
import com.mygdx.primelogistics.R
import com.mygdx.primelogistics.android.api.RetrofitClient
import com.mygdx.primelogistics.android.utils.HomeNavigator
import com.mygdx.primelogistics.android.utils.SessionManager
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class AgentHomeActivity : AppCompatActivity() {
    private lateinit var sessionManager: SessionManager
    private lateinit var tvUserName: TextView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_agent_home)
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.mainActivityAgentHome)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

        sessionManager = SessionManager(this)
        RetrofitClient.init { sessionManager.getAccessToken() }
        tvUserName = findViewById(R.id.tvUserName)

        bindActions()
        setupChart()
    }

    override fun onResume() {
        super.onResume()
        loadCurrentUser()
    }

    private fun bindActions() {
        findViewById<ImageButton>(R.id.btnUser).setOnClickListener {
            startActivity(Intent(this, UsuarioActivity::class.java))
        }

        findViewById<ImageButton>(R.id.btnHome).setOnClickListener {
            HomeNavigator.navigateToHome(this, HomeNavigator.LOGISTICS_OPERATOR_ROLE_ID, finishCurrent = false)
        }

        findViewById<Button>(R.id.btnSeeAll).setOnClickListener {
            startActivity(Intent(this, AllOperationsActivity::class.java))
        }
    }

    private fun loadCurrentUser() {
        CoroutineScope(Dispatchers.IO).launch {
            try {
                val response = RetrofitClient.api.getMe()

                withContext(Dispatchers.Main) {
                    if (response.isSuccessful && response.body() != null) {
                        val user = response.body()!!
                        sessionManager.saveRoleId(user.rol.id)
                        tvUserName.text = user.nombre
                    } else if (response.code() == 401) {
                        logout()
                    } else {
                        tvUserName.text = "Usuario"
                    }
                }
            } catch (_: Exception) {
                withContext(Dispatchers.Main) {
                    Toast.makeText(
                        this@AgentHomeActivity,
                        "Sin conexion: verificar internet o estado del servidor.",
                        Toast.LENGTH_LONG
                    ).show()
                }
            }
        }
    }

    private fun setupChart() {
        val pieChart = findViewById<PieChart>(R.id.pieChart)

        val entries = arrayListOf(
            PieEntry(40f, "Android"),
            PieEntry(30f, "iOS"),
            PieEntry(20f, "Web"),
            PieEntry(10f, "Otros")
        )

        val dataSet = PieDataSet(entries, "Categorias").apply {
            colors = ColorTemplate.MATERIAL_COLORS.toList()
            valueTextSize = 14f
        }

        pieChart.data = PieData(dataSet)
        pieChart.description.isEnabled = false
        pieChart.centerText = "Uso de Plataformas"
        pieChart.animateY(1400)
        pieChart.invalidate()

        findViewById<Button>(R.id.btnSeeAll).setOnClickListener {
            startActivity(Intent(this, AllOperationsActivity::class.java))
            finish()
        }
    }

    private fun logout() {
        sessionManager.clearSession()
        val intent = Intent(this, LoginActivity::class.java).apply {
            flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
        }
        startActivity(intent)
        finish()
    }
}
