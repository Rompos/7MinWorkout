package com.rompos.a7minworkout

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.activity.OnBackPressedDispatcher
import com.rompos.a7minworkout.databinding.ActivityBmiBinding
import java.math.BigDecimal
import java.math.RoundingMode

class BMIActivity : AppCompatActivity() {

    companion object{
        private const val METRIC_UNITS_VIEW = "METRIC_UNIT_VIEW" //Metric Unit View
        private const val US_UNITS_VIEW = "US_UNIT_VIEW" //US Unit View
    }
    private var binding: ActivityBmiBinding? = null

    //which of the view is currently visible
    private var currentVisibleView : String = METRIC_UNITS_VIEW //A variable to hold a value to make a selected view visible

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityBmiBinding.inflate(layoutInflater)
        setContentView(binding?.root)
        setSupportActionBar(binding?.toolbarBmiActivity)

        if (supportActionBar != null) {
            supportActionBar?.setDisplayHomeAsUpEnabled(true)
            supportActionBar?.title = "Calculate Body Mass Index (BMI)"
        }

        binding?.toolbarBmiActivity?.setNavigationOnClickListener {
            //onBackPressed() ////since i use custom Dialog for back button
            onBackPressedDispatcher.onBackPressed()
        }

        ////we make the default button to be the metric system
        makeVisibleMetricUnitsView()

        //better implementation for radio groups (the _ is the rgUnit id)
        binding?.rgUnits?.setOnCheckedChangeListener { _, checkedId: Int ->
            if (checkedId == R.id.rbMetricUnits) {
                makeVisibleMetricUnitsView()
            } else {
                makeVisibleUsUnitsView()
            }
        }

        binding?.btnCalculateUnits?.setOnClickListener{
            calculateUnits()
        }
    }

    private fun makeVisibleMetricUnitsView(){
        currentVisibleView = METRIC_UNITS_VIEW
        binding?.tilMetricUnitWeight?.visibility = View.VISIBLE //Metric Weight UNITS visible
        binding?.tilMetricUnitHeight?.visibility = View.VISIBLE //Metric Height UNITS visible
        binding?.tilUsMetricUnitWeight?.visibility = View.GONE //Make weight view gone
        binding?.tilMetricUsUnitHeightFeet?.visibility = View.GONE //Make height feet gone
        binding?.tilMetricUsUnitHeightInch?.visibility = View.GONE //Make height inch gone

        binding?.etMetricUnitHeight?.text!!.clear() // height value is cleared if it is added
        binding?.etMetricUnitWeight?.text!!.clear() // weight value is cleared if it is added

        binding?.llDiplayBMIResult?.visibility = View.INVISIBLE // Linear layout becomes invisible
    }

    private fun makeVisibleUsUnitsView(){
        currentVisibleView = US_UNITS_VIEW
        binding?.tilMetricUnitWeight?.visibility = View.INVISIBLE //Metric Weight UNITS INVISIBLE
        binding?.tilMetricUnitHeight?.visibility = View.INVISIBLE //Metric Height UNITS INVISIBLE
        binding?.tilUsMetricUnitWeight?.visibility = View.VISIBLE //Make weight view VISIBLE
        binding?.tilMetricUsUnitHeightFeet?.visibility = View.VISIBLE //Make height feet VISIBLE
        binding?.tilMetricUsUnitHeightInch?.visibility = View.VISIBLE //Make height inch VISIBLE

        binding?.etUsMetricUnitHeightInch?.text!!.clear() //height Inch value is cleared
        binding?.etUsMetricUnitHeightFeet?.text!!.clear() // height Feet value is cleared
        binding?.etUsMetricUnitWeight?.text!!.clear() // weight value is cleared

        binding?.llDiplayBMIResult?.visibility = View.INVISIBLE // Linear layout becomes invisible
    }

    private fun displayBMIResults(bmi:Float){
        val bmiLabel : String
        val bmiDescription : String

        if(bmi.compareTo(15f)<=0){
            bmiLabel = "Very severely underweight"
            bmiDescription = "Oops! You really need to take better care of yourself!Eat more!"
        }else if (bmi.compareTo(15f) > 0 && bmi.compareTo(16f) <= 0){
            bmiLabel = "Severely underweight"
            bmiDescription = "Oops! You really need to take better care of yourself!Eat more!"
        }else if (bmi.compareTo(16f) > 0 && bmi.compareTo(18.5f) <= 0){
            bmiLabel = "Underweight"
            bmiDescription = "Oops! You really need to take better care of yourself!Eat more!"
        }else if (bmi.compareTo(18.5f) > 0 && bmi.compareTo(25f) <= 0){
            bmiLabel = "Normal"
            bmiDescription = "Congratulations!You are in a good shape!"
        }else if (bmi.compareTo(25f) > 0 && bmi.compareTo(30f) <= 0){
            bmiLabel = "Overweight"
            bmiDescription = "Oops! You really need to take better care of yourself!Workout more!"
        }else if (bmi.compareTo(30f) > 0 && bmi.compareTo(35f) <= 0){
            bmiLabel = "Obese Class | (Moderately obese)"
            bmiDescription = "Oops! You really need to take better care of yourself!Workout more!"
        }else if (bmi.compareTo(35f) > 0 && bmi.compareTo(40f) <= 0){
            bmiLabel = "Obese Class | (Severely obese)"
            bmiDescription = "OMG! You really need to take better care of yourself!Act now!"
        }else{
            bmiLabel = "Obese Class | (Very Severely obese)"
            bmiDescription = "OMG! You really need to take better care of yourself!Act now!"
        }

        val bmiValue = BigDecimal(bmi.toDouble()).setScale(2,RoundingMode.HALF_EVEN).toString()

        binding?.llDiplayBMIResult?.visibility = View.VISIBLE
        binding?.tvBMIValue?.text = bmiValue
        binding?.tvBMIType?.text = bmiLabel
        binding?.tvBMIDescription?.text = bmiDescription

    }

    private fun calculateUnits(){
        if(currentVisibleView == METRIC_UNITS_VIEW){
            if(validateMetricUnits()){
                val heightValue : Float = binding?.etMetricUnitHeight?.text.toString().toFloat() / 100 //because we use centimeters(cm) and want to get it in meters

                val weightValue : Float = binding?.etMetricUnitWeight?.text.toString().toFloat()

                val bmi = weightValue / (heightValue*heightValue)

                displayBMIResults(bmi)

            }else{
                Toast.makeText(this,"Please enter a valid values.",Toast.LENGTH_LONG).show()
            }
        }else{
            if(validateUsUnits()) {
                val usUnitHeightValueFeet : String = binding?.etUsMetricUnitHeightFeet?.text.toString() //Height Feet value entered in EditText component
                val usUnitHeightValueInch : String = binding?.etUsMetricUnitHeightInch?.text.toString() //Height Inch value entered in EditText component
                val usUnitWeightValue : Float = binding?.etUsMetricUnitWeight?.text.toString().toFloat() //Weight value entered in EditText component

                ////Here the Height Feet and Inch values are merged and multiplied by 12 for conversion
                val heightValue = usUnitHeightValueInch.toFloat() + usUnitHeightValueFeet.toFloat() * 12

                val bmi = 703 * (usUnitWeightValue / (heightValue * heightValue))

                displayBMIResults(bmi)

            }else{
                Toast.makeText(this,"Please enter a valid values.",Toast.LENGTH_LONG).show()
            }

        }
    }


    private fun validateMetricUnits(): Boolean {
        var isValid = true

        if (binding?.etMetricUnitWeight?.text.toString().isNullOrBlank()) {
            isValid = false
        } else if (binding?.etMetricUnitHeight?.text.toString().isNullOrBlank()) {
            isValid = false
        }

        return isValid
    }

    private fun validateUsUnits(): Boolean {
        var isValid = true
    //if any of the 3 text inputs is empty
        when {
            binding?.etUsMetricUnitWeight?.text.toString().isNullOrBlank()-> {
                isValid = false
            }
            binding?.etUsMetricUnitHeightFeet?.text.toString().isNullOrBlank() -> {
                isValid = false
            }
            binding?.etUsMetricUnitHeightInch?.text.toString().isNullOrBlank() -> {
                isValid = false
            }
        }

        return isValid
    }
}