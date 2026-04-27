package com.mygdx.primelogistics.android

import android.content.Context
import android.content.Intent
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.os.Bundle
import android.view.ViewGroup
import android.widget.Button
import android.widget.CheckBox
import android.widget.EditText
import android.widget.ImageButton
import android.widget.TextView
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.mygdx.primelogistics.R
import com.mygdx.primelogistics.android.api.RetrofitClient
import com.mygdx.primelogistics.android.models.Operation
import com.mygdx.primelogistics.android.models.RejectOperationRequest
import com.mygdx.primelogistics.android.utils.SessionManager
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import java.text.NumberFormat
import java.time.OffsetDateTime
import java.time.format.DateTimeFormatter
import java.util.Locale

class PropuestaActivity : AppCompatActivity() {
    private lateinit var sessionManager: SessionManager
    private lateinit var tvOperationReference: TextView
    private lateinit var tvOrigen: TextView
    private lateinit var tvDestination: TextView
    private lateinit var tvIncoterm: TextView
    private lateinit var tvBultos: TextView
    private lateinit var tvEtd: TextView
    private lateinit var tvEta: TextView
    private lateinit var tvPesoTotal: TextView
    private lateinit var tvCoste: TextView
    private lateinit var btnAceptar: Button
    private lateinit var btnRechazar: Button

    private var currentOperation: Operation? = null
    private var operationId: Int = -1

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_propuesta)

        sessionManager = SessionManager(this)
        RetrofitClient.init { sessionManager.getAccessToken() }

        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.mainPropuesta)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

        bindViews()
        bindActions()
        loadOperationFromIntent()
    }

    private fun bindViews() {
        tvOperationReference = findViewById(R.id.tvOperationReference)
        tvOrigen = findViewById(R.id.tvOrigen)
        tvDestination = findViewById(R.id.tvDestination)
        tvIncoterm = findViewById(R.id.tvIncoterm)
        tvBultos = findViewById(R.id.tvbultos)
        tvEtd = findViewById(R.id.tvETD)
        tvEta = findViewById(R.id.tvETA)
        tvPesoTotal = findViewById(R.id.tvPesoTotal)
        tvCoste = findViewById(R.id.tvCoste)
        btnAceptar = findViewById(R.id.btnAceptar)
        btnRechazar = findViewById(R.id.btnRechazar)
    }

    private fun bindActions() {
        findViewById<ImageButton>(R.id.btnHome).setOnClickListener { finish() }
        findViewById<ImageButton>(R.id.btnUser).setOnClickListener {
            startActivity(Intent(this, UsuarioActivity::class.java))
        }

        btnAceptar.setOnClickListener {
            if (operationId <= 0) {
                Toast.makeText(this, "No se pudo identificar la operacion.", Toast.LENGTH_SHORT).show()
            } else {
                showAcceptDialog()
            }
        }

        btnRechazar.setOnClickListener {
            if (operationId <= 0) {
                Toast.makeText(this, "No se pudo identificar la operacion.", Toast.LENGTH_SHORT).show()
            } else {
                showRejectDialog()
            }
        }
    }

    private fun loadOperationFromIntent() {
        operationId = intent.getIntExtra(EXTRA_OPERATION_ID, -1)

        val hasFullPayload = intent.hasExtra(EXTRA_ORDER_REFERENCE)
        if (hasFullPayload) {
            currentOperation = Operation(
                id = operationId,
                orderReference = intent.getStringExtra(EXTRA_ORDER_REFERENCE).orEmpty(),
                originPortName = intent.getStringExtra(EXTRA_ORIGIN).orEmpty(),
                destinationPortName = intent.getStringExtra(EXTRA_DESTINATION).orEmpty(),
                totalCost = intent.getDoubleExtra(EXTRA_TOTAL_COST, 0.0),
                etd = intent.getStringExtra(EXTRA_ETD).orEmpty(),
                eta = intent.getStringExtra(EXTRA_ETA).orEmpty(),
                incotermCode = intent.getStringExtra(EXTRA_INCOTERM_CODE).orEmpty(),
                piecesNumber = intent.getIntExtra(EXTRA_PIECES_NUMBER, -1).takeIf { it >= 0 },
                kilograms = intent.getDoubleExtra(EXTRA_KILOGRAMS, 0.0),
                statusName = intent.getStringExtra(EXTRA_STATUS_NAME),
                trackingFlowId = null,
                trackingFlowName = null,
                currentTrackingFlowStepId = null,
                currentTrackingStepName = null,
                currentTrackingStepOrder = null,
                currentTrackingStepUiPercent = null,
                currentTrackingStepArrivedAt = null
            )
            renderOperation(currentOperation)
        } else {
            fetchOperation()
        }
    }

    private fun fetchOperation() {
        if (operationId <= 0) {
            Toast.makeText(this, "No se recibio ninguna operacion.", Toast.LENGTH_SHORT).show()
            finish()
            return
        }

        CoroutineScope(Dispatchers.IO).launch {
            try {
                val response = RetrofitClient.api.getUserOperations()

                withContext(Dispatchers.Main) {
                    when {
                        response.isSuccessful -> {
                            val operation = response.body()?.firstOrNull { it.id == operationId }
                            if (operation == null) {
                                Toast.makeText(
                                    this@PropuestaActivity,
                                    "No se encontro la operacion seleccionada.",
                                    Toast.LENGTH_SHORT
                                ).show()
                                finish()
                            } else {
                                currentOperation = operation
                                renderOperation(operation)
                            }
                        }

                        response.code() == 401 -> {
                            Toast.makeText(
                                this@PropuestaActivity,
                                "Tu sesion ha expirado.",
                                Toast.LENGTH_SHORT
                            ).show()
                            finish()
                        }

                        else -> {
                            Toast.makeText(
                                this@PropuestaActivity,
                                "No se pudo cargar la propuesta.",
                                Toast.LENGTH_SHORT
                            ).show()
                            finish()
                        }
                    }
                }
            } catch (_: Exception) {
                withContext(Dispatchers.Main) {
                    Toast.makeText(
                        this@PropuestaActivity,
                        "Sin conexion: verificar internet o estado del servidor.",
                        Toast.LENGTH_LONG
                    ).show()
                    finish()
                }
            }
        }
    }

    private fun renderOperation(operation: Operation?) {
        if (operation == null) {
            return
        }

        tvOperationReference.text = operation.orderReference.ifBlank { "Sin referencia" }
        tvOrigen.text = operation.originPortName.ifBlank { "Sin origen" }
        tvDestination.text = operation.destinationPortName.ifBlank { "Sin destino" }
        tvIncoterm.text = operation.incotermCode.ifBlank { "Sin incoterm" }
        tvBultos.text = operation.piecesNumber?.toString() ?: "Sin dato"
        tvEtd.text = formatDate(operation.etd)
        tvEta.text = formatDate(operation.eta)
        tvPesoTotal.text = formatWeight(operation.kilograms)
        tvCoste.text = formatCurrency(operation.totalCost)
    }

    private fun showRejectDialog() {
        val dialogView = layoutInflater.inflate(R.layout.dialog_rechazo_propuesta, null)
        val reasonInput = dialogView.findViewById<EditText>(R.id.etMotivoRechazo)
        val confirmCheck = dialogView.findViewById<CheckBox>(R.id.cbConfirmReject)
        val confirmButton = dialogView.findViewById<Button>(R.id.btnConfirmReject)

        val dialog = AlertDialog.Builder(this)
            .setView(dialogView)
            .create()

        dialog.window?.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
        dialog.setCanceledOnTouchOutside(true)

        confirmCheck.setOnCheckedChangeListener { _, isChecked ->
            confirmButton.isEnabled = isChecked
        }

        confirmButton.setOnClickListener {
            val rejectReason = reasonInput.text.toString().trim()

            if (rejectReason.isEmpty()) {
                reasonInput.error = "Escribe el motivo del rechazo"
                return@setOnClickListener
            }

            rejectOperation(
                reason = rejectReason,
                dialog = dialog,
                confirmButton = confirmButton,
                reasonInput = reasonInput,
                confirmCheck = confirmCheck
            )
        }

        dialog.show()
        dialog.window?.setLayout(
            (resources.displayMetrics.widthPixels * 0.82f).toInt(),
            ViewGroup.LayoutParams.WRAP_CONTENT
        )
    }

    private fun rejectOperation(
        reason: String,
        dialog: AlertDialog,
        confirmButton: Button,
        reasonInput: EditText,
        confirmCheck: CheckBox
    ) {
        setRejectButtonsEnabled(false, confirmButton, reasonInput, confirmCheck)

        CoroutineScope(Dispatchers.IO).launch {
            try {
                val response = RetrofitClient.api.rejectOperation(
                    operationId = operationId,
                    request = RejectOperationRequest(reason)
                )

                withContext(Dispatchers.Main) {
                    setRejectButtonsEnabled(true, confirmButton, reasonInput, confirmCheck)

                    when {
                        response.isSuccessful -> {
                            Toast.makeText(
                                this@PropuestaActivity,
                                "Propuesta rechazada correctamente.",
                                Toast.LENGTH_SHORT
                            ).show()
                            setResult(RESULT_OK)
                            dialog.dismiss()
                            finish()
                        }

                        response.code() == 400 -> {
                            reasonInput.error = "El motivo del rechazo no es valido"
                        }

                        response.code() == 401 -> {
                            Toast.makeText(
                                this@PropuestaActivity,
                                "Tu sesion ha expirado.",
                                Toast.LENGTH_SHORT
                            ).show()
                            dialog.dismiss()
                            finish()
                        }

                        response.code() == 404 -> {
                            Toast.makeText(
                                this@PropuestaActivity,
                                "No se encontro la operacion.",
                                Toast.LENGTH_SHORT
                            ).show()
                        }

                        else -> {
                            Toast.makeText(
                                this@PropuestaActivity,
                                "No se pudo rechazar la propuesta.",
                                Toast.LENGTH_SHORT
                            ).show()
                        }
                    }
                }
            } catch (_: Exception) {
                withContext(Dispatchers.Main) {
                    setRejectButtonsEnabled(true, confirmButton, reasonInput, confirmCheck)
                    Toast.makeText(
                        this@PropuestaActivity,
                        "Sin conexion: verificar internet o estado del servidor.",
                        Toast.LENGTH_LONG
                    ).show()
                }
            }
        }
    }

    private fun showAcceptDialog() {
        val dialogView = layoutInflater.inflate(R.layout.dialog_aceptar_propuesta, null)
        val confirmCheck = dialogView.findViewById<CheckBox>(R.id.cbConfirmAccept)
        val confirmButton = dialogView.findViewById<Button>(R.id.btnConfirmAccept)

        val dialog = AlertDialog.Builder(this)
            .setView(dialogView)
            .create()

        dialog.window?.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
        dialog.setCanceledOnTouchOutside(true)

        confirmCheck.setOnCheckedChangeListener { _, isChecked ->
            confirmButton.isEnabled = isChecked
        }

        confirmButton.setOnClickListener {
            acceptOperation(dialog, confirmButton, confirmCheck)
        }

        dialog.show()
        dialog.window?.setLayout(
            (resources.displayMetrics.widthPixels * 0.82f).toInt(),
            ViewGroup.LayoutParams.WRAP_CONTENT
        )
    }

    private fun acceptOperation(
        dialog: AlertDialog,
        confirmButton: Button,
        confirmCheck: CheckBox
    ) {
        setMainButtonsEnabled(false)
        confirmButton.isEnabled = false
        confirmCheck.isEnabled = false

        CoroutineScope(Dispatchers.IO).launch {
            try {
                val response = RetrofitClient.api.acceptOperation(operationId)

                withContext(Dispatchers.Main) {
                    setMainButtonsEnabled(true)
                    confirmCheck.isEnabled = true
                    confirmButton.isEnabled = confirmCheck.isChecked

                    when {
                        response.isSuccessful -> {
                            Toast.makeText(
                                this@PropuestaActivity,
                                "Propuesta aceptada correctamente.",
                                Toast.LENGTH_SHORT
                            ).show()
                            setResult(RESULT_OK)
                            dialog.dismiss()
                            finish()
                        }

                        response.code() == 401 -> {
                            Toast.makeText(
                                this@PropuestaActivity,
                                "Tu sesion ha expirado.",
                                Toast.LENGTH_SHORT
                            ).show()
                            dialog.dismiss()
                            finish()
                        }

                        response.code() == 404 -> {
                            Toast.makeText(
                                this@PropuestaActivity,
                                "No se encontro la operacion.",
                                Toast.LENGTH_SHORT
                            ).show()
                        }

                        else -> {
                            Toast.makeText(
                                this@PropuestaActivity,
                                "No se pudo aceptar la propuesta.",
                                Toast.LENGTH_SHORT
                            ).show()
                        }
                    }
                }
            } catch (_: Exception) {
                withContext(Dispatchers.Main) {
                    setMainButtonsEnabled(true)
                    confirmCheck.isEnabled = true
                    confirmButton.isEnabled = confirmCheck.isChecked
                    Toast.makeText(
                        this@PropuestaActivity,
                        "Sin conexion: verificar internet o estado del servidor.",
                        Toast.LENGTH_LONG
                    ).show()
                }
            }
        }
    }

    private fun setMainButtonsEnabled(enabled: Boolean) {
        btnAceptar.isEnabled = enabled
        btnRechazar.isEnabled = enabled
    }

    private fun setRejectButtonsEnabled(
        enabled: Boolean,
        confirmButton: Button,
        reasonInput: EditText,
        confirmCheck: CheckBox
    ) {
        reasonInput.isEnabled = enabled
        confirmCheck.isEnabled = enabled
        confirmButton.isEnabled = enabled && confirmCheck.isChecked
    }

    private fun formatDate(rawValue: String?): String {
        if (rawValue.isNullOrBlank()) {
            return "Sin fecha"
        }

        return try {
            OffsetDateTime.parse(rawValue).format(DateTimeFormatter.ofPattern("dd/MM/yyyy"))
        } catch (_: Exception) {
            rawValue.substringBefore("T").ifBlank { rawValue }
        }
    }

    private fun formatWeight(weight: Double): String {
        return String.format(Locale.US, "%.2f kg", weight)
    }

    private fun formatCurrency(amount: Double): String {
        return NumberFormat.getCurrencyInstance(Locale("es", "ES")).format(amount)
    }

    companion object {
        private const val EXTRA_OPERATION_ID = "extra_operation_id"
        private const val EXTRA_ORDER_REFERENCE = "extra_order_reference"
        private const val EXTRA_ORIGIN = "extra_origin"
        private const val EXTRA_DESTINATION = "extra_destination"
        private const val EXTRA_TOTAL_COST = "extra_total_cost"
        private const val EXTRA_ETD = "extra_etd"
        private const val EXTRA_ETA = "extra_eta"
        private const val EXTRA_INCOTERM_CODE = "extra_incoterm_code"
        private const val EXTRA_PIECES_NUMBER = "extra_pieces_number"
        private const val EXTRA_KILOGRAMS = "extra_kilograms"
        private const val EXTRA_STATUS_NAME = "extra_status_name"

        fun createIntent(context: Context, operation: Operation): Intent {
            return Intent(context, PropuestaActivity::class.java).apply {
                putExtra(EXTRA_OPERATION_ID, operation.id)
                putExtra(EXTRA_ORDER_REFERENCE, operation.orderReference)
                putExtra(EXTRA_ORIGIN, operation.originPortName)
                putExtra(EXTRA_DESTINATION, operation.destinationPortName)
                putExtra(EXTRA_TOTAL_COST, operation.totalCost)
                putExtra(EXTRA_ETD, operation.etd)
                putExtra(EXTRA_ETA, operation.eta)
                putExtra(EXTRA_INCOTERM_CODE, operation.incotermCode)
                putExtra(EXTRA_PIECES_NUMBER, operation.piecesNumber ?: -1)
                putExtra(EXTRA_KILOGRAMS, operation.kilograms)
                putExtra(EXTRA_STATUS_NAME, operation.statusName)
            }
        }
    }
}
