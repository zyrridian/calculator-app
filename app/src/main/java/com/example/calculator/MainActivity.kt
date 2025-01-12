package com.example.calculator

import android.os.Bundle
import android.widget.Button
import android.widget.TextView
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.google.android.material.button.MaterialButton

class MainActivity : AppCompatActivity() {

    private lateinit var tvDisplay: TextView
    private var currentInput = ""
    private var operator = ""
    private var lastOperator = ""
    private var result = 0.0
    private var isNewInput = false

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_main)
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

        tvDisplay = findViewById(R.id.tvDisplay)

        // Number buttons
        val numberButtons = listOf<MaterialButton>(
            findViewById(R.id.btn0),
            findViewById(R.id.btn1),
            findViewById(R.id.btn2),
            findViewById(R.id.btn3),
            findViewById(R.id.btn4),
            findViewById(R.id.btn5),
            findViewById(R.id.btn6),
            findViewById(R.id.btn7),
            findViewById(R.id.btn8),
            findViewById(R.id.btn9)
        )

        numberButtons.forEach { button ->
            button.setOnClickListener { appendToDisplay(button.text.toString()) }
        }

        // Operation buttons
        findViewById<MaterialButton>(R.id.btnPlus).setOnClickListener { setOperator("+") }
        findViewById<MaterialButton>(R.id.btnMinus).setOnClickListener { setOperator("-") }
        findViewById<MaterialButton>(R.id.btnMultiply).setOnClickListener { setOperator("×") }
        findViewById<MaterialButton>(R.id.btnDivide).setOnClickListener { setOperator("÷") }
        findViewById<MaterialButton>(R.id.btnPercent).setOnClickListener { calculatePercentage() }
        findViewById<MaterialButton>(R.id.btnSignChange).setOnClickListener { toggleSign() }

        // Clear and backspace
        findViewById<MaterialButton>(R.id.btnClear).setOnClickListener { clearDisplay() }
        findViewById<MaterialButton>(R.id.btnBackspace).setOnClickListener { backspace() }

        // Decimal and equals
        findViewById<MaterialButton>(R.id.btnComma).setOnClickListener { appendToDisplay(".") }
        findViewById<MaterialButton>(R.id.btnEqual).setOnClickListener { calculateResult() }
    }

    private fun appendToDisplay(value: String) {
        if (isNewInput) {
            currentInput = ""
            isNewInput = false
        }
        if (currentInput == "0" && value != ".") {
            currentInput = value
        } else {
            currentInput += value
        }
        updateDisplay(currentInput)
    }

    private fun setOperator(op: String) {
        if (currentInput.isNotEmpty()) {
            if (operator.isNotEmpty()) {
                calculateIntermediateResult()
            } else {
                result = currentInput.toDouble()
            }
            operator = op
            lastOperator = operator
            currentInput = ""
            isNewInput = true
            updateDisplay("$result $operator")
        }
    }

    private fun calculateIntermediateResult() {
        if (operator.isNotEmpty() && currentInput.isNotEmpty()) {
            val secondOperand = currentInput.toDouble()
            result = when (operator) {
                "+" -> result + secondOperand
                "-" -> result - secondOperand
                "×" -> result * secondOperand
                "÷" -> if (secondOperand != 0.0) result / secondOperand else Double.NaN
                else -> result
            }
            operator = ""
        }
    }

    private fun calculateResult() {
        if (currentInput.isNotEmpty()) {
            calculateIntermediateResult()
            currentInput = result.toString()
            operator = ""
            updateDisplay(currentInput)
        }
    }

    private fun calculatePercentage() {
        if (currentInput.isNotEmpty()) {
            currentInput = (currentInput.toDouble() / 100).toString()
            updateDisplay(currentInput)
        }
    }

    private fun toggleSign() {
        if (currentInput.isNotEmpty()) {
            currentInput = if (currentInput.startsWith("-")) {
                currentInput.substring(1)
            } else {
                "-$currentInput"
            }
            updateDisplay(currentInput)
        }
    }

    private fun clearDisplay() {
        currentInput = ""
        operator = ""
        result = 0.0
        updateDisplay("0")
    }

    private fun backspace() {
        if (currentInput.isNotEmpty()) {
            currentInput = currentInput.dropLast(1)
            updateDisplay(if (currentInput.isEmpty()) "0" else currentInput)
        }
    }

    private fun updateDisplay(value: String) {
        tvDisplay.text = value
    }

}