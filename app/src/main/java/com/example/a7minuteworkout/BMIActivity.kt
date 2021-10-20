package com.example.a7minuteworkout

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.Toast
import kotlinx.android.synthetic.main.activity_b_m_i.*
import java.math.BigDecimal
import java.math.RoundingMode
import java.nio.channels.FileLock

class BMIActivity : AppCompatActivity() {
    val METRIC_UNITS_VIEW = "METRIC_UNIT_VIEW"
    val US_UNIT_VIEW = "US_UNIT_VIEW"

    var currentVisibleView: String = METRIC_UNITS_VIEW
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_b_m_i)
        setSupportActionBar(toolbar_bmi_activity)

        val actionBar = supportActionBar
        if(actionBar!= null)
        {
            actionBar.setDisplayHomeAsUpEnabled(true)
            actionBar.title = "calculate BMI"

        }
        toolbar_bmi_activity.setNavigationOnClickListener {
            onBackPressed()
        }
btnCalculateUnits.setOnClickListener {
    if(currentVisibleView ==METRIC_UNITS_VIEW)
    {
        if(validateMetricUnit())
        {
            val heightValue: Float = etMetricUnitHeight.text.toString().toFloat()/100
            val weightValue: Float = etMetricUnitWeight.text.toString().toFloat()

            val bmi =    weightValue/(heightValue*heightValue)
            displayBmiResult(bmi)
        }
        else{
            Toast.makeText(this@BMIActivity, "please enter a valid number", Toast.LENGTH_LONG).show()
        }
    }
    else{
        if(validateUsUnit())
        {
            val usUnitWeightValue : Float = etUsUnitWeight.text.toString().toFloat()
            val usUnitHeightValueFeet: String = etUsUnitHeightFeet.text.toString()
            val usUnitHeightValueInch: String = etUsUnitHeightIche.text.toString()
            val heightValue = usUnitHeightValueFeet.toFloat()*12+ usUnitHeightValueInch.toFloat()

            val bmi = 703 *  usUnitWeightValue/(heightValue*heightValue)
            displayBmiResult(bmi)

        }
        else
        {
            Toast.makeText(this@BMIActivity, "please enter a valid number", Toast.LENGTH_LONG).show()
        }
    }


}
        
        makeVisibleMetricUnitsView()
        
        rdUnits.setOnCheckedChangeListener {group, checkedId ->
            if(checkedId==R.id.rbMetricUnits)
            {
                makeVisibleMetricUnitsView()
            }
            else
            {
                makeVisibleUsUnitsView()
            }
        }
    }
    private fun makeVisibleUsUnitsView()
    {
        currentVisibleView = US_UNIT_VIEW

        tilMetricUnitHeight.visibility = View.GONE
        tilMetricUnitWeight.visibility = View.GONE


        etMetricUnitHeight.text!!.clear()
        etUsUnitHeightFeet.text!!.clear()
        etUsUnitHeightIche.text!!.clear()

        tilUsUnitWeight.visibility = View.VISIBLE
        llUsUnitsHeight.visibility = View.VISIBLE

        lldisplayBMIResult.visibility = View.INVISIBLE
    }
    private fun makeVisibleMetricUnitsView()
    {
        currentVisibleView = METRIC_UNITS_VIEW

        tilMetricUnitHeight.visibility = View.VISIBLE
        tilMetricUnitWeight.visibility = View.VISIBLE


        etMetricUnitHeight.text!!.clear()
        etUsUnitWeight.text!!.clear()
        

        tilUsUnitWeight.visibility = View.GONE
        llUsUnitsHeight.visibility = View.GONE

        lldisplayBMIResult.visibility = View.INVISIBLE
    }
    private fun displayBmiResult(bmi: Float){
        val bmiLabel: String
        val bmiDescription:String
        if(bmi.compareTo(15f)<=0){
            bmiLabel = "very Severely underweight"
            bmiDescription= " you really need to take care of you better! Eat more"
        }
        else if(bmi.compareTo(15f)> 0 && bmi.compareTo(16f)<=0)
        {
            bmiLabel = "Severely Underweight"
            bmiDescription ="you really need to take care of your better! eat more"
        }
        else if(bmi.compareTo(16f)> 0 && bmi.compareTo(18.5f)<=0)
        {
            bmiLabel = " Underweight"
            bmiDescription ="you really need to take care of your better! eat more"
        }
        else if(bmi.compareTo(18.5f)> 0 && bmi.compareTo(25f)<=0)
        {
            bmiLabel = "Normal"
            bmiDescription ="congrats you are in good shape"
        }
        else if(bmi.compareTo(25f)> 0 && bmi.compareTo(30f)<=0)
        {
            bmiLabel = "Overweight"
            bmiDescription ="you really need to take care of your better, work out more"
        }
        else if(bmi.compareTo(30f)> 0 && bmi.compareTo(35f)<=0)
        {
            bmiLabel = "Obese Class| (Moderately close)"
            bmiDescription ="you really need to take care of your better, work out more"
        }
        else if(bmi.compareTo(35f)> 0 && bmi.compareTo(40f)<=0)
        {
            bmiLabel = "Obese Class|| (Severely Obese)"
            bmiDescription ="you really need to take care of your better, work out more"
        }
        else
        {
            bmiLabel = "Obese Class||| (Very Severely Obese)"
            bmiDescription ="you really need to take care of your better, work out more"
        }

      lldisplayBMIResult.visibility = View.VISIBLE
        val   bmiValue = BigDecimal(bmi.toDouble()).setScale(2, RoundingMode.HALF_EVEN).toString()
        tvYourBMIValue.text = bmiValue
        tvYourBMIType.text = bmiLabel
        tvYourBMIDescription.text = bmiDescription

    }

    private fun validateMetricUnit():Boolean
    {
        var isValid = true
        if(etMetricUnitWeight.text.toString() == "")
            isValid = false
        else if(etMetricUnitHeight.text.toString() == "")
            isValid = false
        return  isValid
    }

    private fun validateUsUnit():Boolean
    {
        var isValid = true
        if(etUsUnitWeight.text.toString() == "")
            isValid = false
        else if(etUsUnitHeightFeet.text.toString() == "")
            isValid = false
        else if(etUsUnitHeightIche.text.toString() == "")
            isValid = false
        return  isValid
    }
}