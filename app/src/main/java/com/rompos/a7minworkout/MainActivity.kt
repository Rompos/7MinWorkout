package com.rompos.a7minworkout

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.rompos.a7minworkout.databinding.ActivityMainBinding
import android.graphics.drawable.AnimatedVectorDrawable


class MainActivity : AppCompatActivity() {

    private var binding:ActivityMainBinding?=null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding?.root)//we do this in order to avoid findViewById element

////        redundant with binding
////        we use binding because find view with id works through out all files not only with this particular xml file
////        and if we have 2 files with the same id our application will crush
//        val flStartButton : FrameLayout = findViewById(R.id.flStart)

        binding?.flStart?.setOnClickListener{
            //flStartButton.setOnClickListener{  //redundant with binding
            //Toast.makeText(this@MainActivity,"Here we will start the exercise.",Toast.LENGTH_SHORT).show()
            val intent = Intent(this,ExerciseActivity::class.java)
            startActivity(intent)
        }

        binding?.flBMI?.setOnClickListener{
            //flStartButton.setOnClickListener{  //redundant with binding
            //Toast.makeText(this@MainActivity,"Here we will start the exercise.",Toast.LENGTH_SHORT).show()
            val intent = Intent(this,BMIActivity::class.java)
            startActivity(intent)
        }

        binding?.flHistory?.setOnClickListener{
            //flStartButton.setOnClickListener{  //redundant with binding
            //Toast.makeText(this@MainActivity,"Here we will start the exercise.",Toast.LENGTH_SHORT).show()
            val intent = Intent(this,HistoryActivity::class.java)
            startActivity(intent)
        }

        var isChecked = false
        binding?.avdImage!!.setOnClickListener {
            if (isChecked){
                checkToClose()
                this.finish()
            }
            else{
                closeToCheck()
            }
            isChecked = !isChecked
        }
    }

    override fun onDestroy() { // if we use binding we must also use on destroy to set it to null to avoid memory leak
        super.onDestroy()

        binding = null
    }

    private fun closeToCheck() {

        binding?.avdImage!!.setImageResource(R.drawable.avd_close_to_check)
        val avdCheckToClose = binding?.avdImage!!.drawable as AnimatedVectorDrawable
        avdCheckToClose.start()
    }

    private fun checkToClose() {

        binding?.avdImage!!.setImageResource(R.drawable.avd_check_to_close)
        val avdCheckToClose = binding?.avdImage!!.drawable as AnimatedVectorDrawable
        avdCheckToClose.start()
    }
}