package com.rompos.a7minworkout

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import androidx.activity.OnBackPressedDispatcher
import androidx.lifecycle.lifecycleScope
import com.rompos.a7minworkout.databinding.ActivityFinishBinding
import com.rompos.a7minworkout.databinding.ActivityExerciseBinding
import kotlinx.coroutines.launch
import java.text.SimpleDateFormat
import java.util.*


class FinishActivity : AppCompatActivity() {
    //Create a binding variable
    private var binding: ActivityFinishBinding? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    //inflate the layout
        binding = ActivityFinishBinding.inflate(layoutInflater)
    //bind the layout to this Activity
        setContentView(binding?.root)
    // attach the layout to this activity
        setSupportActionBar(binding?.toolbarFinishActivity)
        if (supportActionBar != null) {
            supportActionBar?.setDisplayHomeAsUpEnabled(true)
        }
        binding?.toolbarFinishActivity?.setNavigationOnClickListener {
            onBackPressedDispatcher.onBackPressed()
        }

        //Adding a click event to the Finish Button
        binding?.btnFinish?.setOnClickListener {
            finish()
        }

        val historyDao = (application as WorkOutApp).db.historyDao()
        addDateToDatabase(historyDao)
    }

    private fun addDateToDatabase(historyDao: HistoryDao){

        //we have to prepare the date ------------------
        val myCalendar = Calendar.getInstance()
        val dateTime = myCalendar.time
        Log.e("Date : "," " + dateTime)

        val sdf = SimpleDateFormat("dd MMM yyyy HH:mm:ss",Locale.getDefault())
        val date = sdf.format(dateTime)
        Log.e("Formatted Date : ","" + date)
        //finished preparing the date -----------------
        if (date.isNotEmpty()) {
            lifecycleScope.launch {
                historyDao.insert(HistoryEntity(date = date))
                Log.e("Date : ", "Added...")

            }
        }
    }
}