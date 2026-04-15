package com.mygdx.primelogistics.android.models

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.mygdx.primelogistics.android.api.RetrofitClient
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.launch

class OperationsViewModel : ViewModel() {
    val operationsList = MutableLiveData<List<Operation>>()
    val isLoading = MutableLiveData<Boolean>()
    val errorMsg = MutableLiveData<String?>()

    fun fetchOperations() {
        isLoading.value = true
        viewModelScope.launch(Dispatchers.IO) {
            try {
                val response = RetrofitClient.api.getUserOperations()
                withContext(Dispatchers.Main) {
                    if (response.isSuccessful) {
                        operationsList.value = response.body() ?: emptyList()
                    } else {
                        errorMsg.value = "Error al cargar operaciones."
                    }
                    isLoading.value = false
                }
            } catch (e: Exception) {
                withContext(Dispatchers.Main) {
                    errorMsg.value = "Fallo de conexión: ${e.message}."
                    isLoading.value = false
                }
            }
        }
    }
}
