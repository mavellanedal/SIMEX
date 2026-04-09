package com.mygdx.primelogistics.android

import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.RecyclerView

class ClientHomeActivity  : AppCompatActivity() {

    private lateinit var recyclerOperations: RecyclerView
    private lateinit var adapter: operationAdapter
    private val operations: MutableList<Operation> = mutableListOf()

}
