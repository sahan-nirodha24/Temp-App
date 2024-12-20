package com.example.tempapp

import android.os.Bundle
import android.view.View
import android.widget.*
import androidx.appcompat.app.AppCompatActivity

class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        // UI Components
        val inputTemp = findViewById<EditText>(R.id.inputTemp)
        val convertButton = findViewById<Button>(R.id.convertButton)
        val resultText = findViewById<TextView>(R.id.resultText)
        val unitFrom = findViewById<Spinner>(R.id.unitFrom)
        val unitTo = findViewById<Spinner>(R.id.unitTo)

        // Populate spinners with unit options
        val units = resources.getStringArray(R.array.unit_array)
        val adapter = ArrayAdapter(this, android.R.layout.simple_spinner_item, units)
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
        unitFrom.adapter = adapter
        unitTo.adapter = adapter

        // Convert Button Click Listener
        convertButton.setOnClickListener {
            val tempInput = inputTemp.text.toString()
            if (tempInput.isEmpty()) {
                // Show error message if input is empty
                resultText.visibility = View.VISIBLE
                resultText.text = getString(R.string.error_message)
                return@setOnClickListener
            }

            val tempValue = tempInput.toDoubleOrNull()
            if (tempValue == null) {
                // Handle invalid numeric input
                resultText.visibility = View.VISIBLE
                resultText.text = getString(R.string.error_message)
                return@setOnClickListener
            }

            val fromUnit = unitFrom.selectedItem.toString()
            val toUnit = unitTo.selectedItem.toString()

            // Perform temperature conversion
            val result = convertTemperature(tempValue, fromUnit, toUnit)
            resultText.visibility = View.VISIBLE
            resultText.text = getString(R.string.converted_temp) + " $result $toUnit"
        }
    }

    /**
     * Converts temperature from one unit to another.
     *
     * @param temp The input temperature value
     * @param fromUnit The original unit of the temperature
     * @param toUnit The target unit of the temperature
     * @return The converted temperature value
     */
    private fun convertTemperature(temp: Double, fromUnit: String, toUnit: String): Double {
        return when (fromUnit) {
            "Celsius" -> {
                when (toUnit) {
                    "Fahrenheit" -> temp * 9 / 5 + 32
                    "Kelvin" -> temp + 273.15
                    else -> temp
                }
            }
            "Fahrenheit" -> {
                when (toUnit) {
                    "Celsius" -> (temp - 32) * 5 / 9
                    "Kelvin" -> (temp - 32) * 5 / 9 + 273.15
                    else -> temp
                }
            }
            "Kelvin" -> {
                when (toUnit) {
                    "Celsius" -> temp - 273.15
                    "Fahrenheit" -> (temp - 273.15) * 9 / 5 + 32
                    else -> temp
                }
            }
            else -> temp
        }
    }
}
