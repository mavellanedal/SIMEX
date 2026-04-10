package com.mygdx.primelogistics.android

import android.os.Bundle
import android.widget.TextView
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.mygdx.primelogistics.R
import com.mygdx.primelogistics.android.models.Operation
import com.mygdx.primelogistics.android.adapters.OpAdapter

class ClientHomeActivity  : AppCompatActivity() {

    private lateinit var recyclerProposals: RecyclerView
    private lateinit var recyclerRecent: RecyclerView
    private lateinit var adapterProposals: PropAdapter
    private lateinit var adapterRecent: RecAdapter
    private val operations: MutableList<Operation> = mutableListOf()
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_client_home)

        recyclerProposals = findViewById(R.id.rvProposals)
        recyclerProposals.layoutManager = LinearLayoutManager(this)

        recyclerRecent = findViewById(R.id.rvRecent)
        recyclerRecent.layoutManager = LinearLayoutManager(this)

        val userName: TextView = findViewById(R.id.txtUserName)

        userName.text = user.Name

        adapterProposals = PropAdapter(
        )
        recyclerProposals.adapter = adapterProposals

        adapterRecent = OpAdapter(
        )
        recyclerRecent.adapter = adapterRecent

        refrescarLista()
    }

}
