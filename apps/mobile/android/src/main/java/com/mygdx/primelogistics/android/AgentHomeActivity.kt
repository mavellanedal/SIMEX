package com.mygdx.primelogistics.android

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.github.mikephil.charting.charts.PieChart
import com.github.mikephil.charting.data.PieData
import com.github.mikephil.charting.data.PieDataSet
import com.github.mikephil.charting.data.PieEntry
import com.github.mikephil.charting.utils.ColorTemplate // Missing import
import com.mygdx.primelogistics.R

class AgentHomeActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_agent_home)

        val pieChart = findViewById<PieChart>(R.id.pieChart)

        val entries = ArrayList<PieEntry>()
        entries.add(PieEntry(40f, "Android"))
        entries.add(PieEntry(30f, "iOS"))
        entries.add(PieEntry(20f, "Web"))
        entries.add(PieEntry(10f, "Otros"))

        val dataSet = PieDataSet(entries, "Categorías")
        dataSet.colors = ColorTemplate.MATERIAL_COLORS.toList()
        dataSet.valueTextSize = 14f

        val data = PieData(dataSet)
        pieChart.data = data

        pieChart.description.isEnabled = false
        pieChart.centerText = "Uso de Plataformas"
        pieChart.animateY(1400)
        pieChart.invalidate()
    }
}
