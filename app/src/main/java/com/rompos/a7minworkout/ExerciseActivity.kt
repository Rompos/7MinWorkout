package com.rompos.a7minworkout

import android.app.Dialog
import android.content.Intent
import android.media.MediaPlayer
import android.net.Uri
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.CountDownTimer
import android.speech.tts.TextToSpeech
import android.util.Log
import android.view.View
import android.widget.Toast
import androidx.recyclerview.widget.LinearLayoutManager
import com.rompos.a7minworkout.databinding.ActivityExerciseBinding
import com.rompos.a7minworkout.databinding.DialogCustomBackConfirmationBinding
import java.util.* //the locale is in here
import kotlin.collections.ArrayList

class ExerciseActivity : AppCompatActivity(), TextToSpeech.OnInitListener {

    private var binding : ActivityExerciseBinding? = null
    //variable for timer which will be initialized later.
    private var restTimer: CountDownTimer? = null
    private var restProgress = 0
    private var restTimerDuration : Long = 10 //by default 10
    private var exerciseTimerDuration : Long = 30 //by default 30
    private var exerciseTimer: CountDownTimer? = null
    private var exerciseProgress = 0

    private var exerciseList : ArrayList<ExerciseModel>? = null
    private var currentExercisePosition = -1

    private var tts : TextToSpeech? = null
    private var player : MediaPlayer? = null

    private var exerciseAdapter : ExerciseStatusAdapter? = null


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityExerciseBinding.inflate(layoutInflater)
        setContentView(binding?.root)//set the root as the main content view

        tts = TextToSpeech(this,this)
        setSupportActionBar(binding?.toolbarExercise)//it is the toolbar we created

        /*binding?.btnSpeak?.setOnClickListener { view ->
            if(binding?tvUpcomingExerciseName?.text!!.isEmpty()){
                Toast.makeText(this,"Please enter something to say!",Toast.LENGTH_LONG).show()
            }else{
                speakOut(binding?.tvUpcomingExerciseName?.text.toString())
            }

        }*/

        //----------- Back Button---------------------------
        if(supportActionBar != null){
            supportActionBar?.setDisplayHomeAsUpEnabled(true)
        }

        exerciseList = Constants.defaultExerciseList()//calls tha Constants object and starts the fun

        binding?.toolbarExercise?.setNavigationOnClickListener{
            //onBackPressed() ////since i use custom Dialog for back button
            customDialogForBackButton()
        }
        //--------------------------------------------------

        //binding?.flProgressBar?.visibility = View.GONE
        //binding?.flProgressBar?.visibility = View.INVISIBLE
        setupRestView()
        setupExerciseStatusRecyclerView()
    }

    override fun onBackPressed() {
        customDialogForBackButton()
        //super.onBackPressed()
    }

    private fun customDialogForBackButton(){
        val customDialog = Dialog(this)
        val dialogBinding = DialogCustomBackConfirmationBinding.inflate(layoutInflater)
        customDialog.setContentView(dialogBinding.root)
        customDialog.setCanceledOnTouchOutside(false)//i cannot cancel the customDialog unless i press yes or no
        dialogBinding.btnYes.setOnClickListener{
            this@ExerciseActivity.finish()
            customDialog.dismiss()
        }
        dialogBinding.btnNo.setOnClickListener{
            customDialog.dismiss()
        }
        customDialog.show()
    }

    private fun setupExerciseStatusRecyclerView(){
        binding?.rvExerciseStatus?.layoutManager = LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL,false)

        exerciseAdapter = ExerciseStatusAdapter(exerciseList!!)
        binding?.rvExerciseStatus?.adapter = exerciseAdapter
    }

    private fun setupRestView(){
        try{
            val soundURI = Uri.parse("android.resource://com.rompos.a7minworkout/"+R.raw.press_start)
            player = MediaPlayer.create(applicationContext,soundURI)
            player?.isLooping = false
            player?.start()
        }catch(e: Exception){
            e.printStackTrace()
        }

        binding?.flRestView?.visibility = View.VISIBLE//it is still there and takes the space it requires
        binding?.tvTitle?.visibility = View.VISIBLE //"Exercise Name" instead of hard coding the name we should draw it from our list
        binding?.tvExerciseName?.visibility = View.INVISIBLE
        binding?.flExerciseView?.visibility = View.INVISIBLE
        binding?.viewGifExercise?.visibility = View.INVISIBLE
        binding?.tvUpcomingLabel?.visibility = View.VISIBLE
        binding?.tvUpcomingExerciseName?.visibility = View.VISIBLE
        binding?.tvUpcomingExerciseName?.text = exerciseList!![currentExercisePosition + 1].getName()

        if(restTimer != null){
            restTimer?.cancel()
            restProgress = 0
        }

        speakOut("Upcoming exercise ${binding?.tvUpcomingExerciseName?.text.toString()}")
        //speakOut(exerciseList!![currentExercisePosition].getName())

        setRestProgressBar()//if while i was running the count down i go back then reset the counter
    }

    private fun setupExerciseView() {
        binding?.flRestView?.visibility = View.INVISIBLE//it is still there and takes the space it requires
        binding?.tvTitle?.visibility = View.INVISIBLE //"GET READY FOR" instead of hard coding the name we should draw it from our list
        binding?.tvExerciseName?.visibility = View.VISIBLE//"Exercise Name"
        binding?.flExerciseView?.visibility = View.VISIBLE
        binding?.viewGifExercise?.visibility = View.VISIBLE
        binding?.viewGifExercise?.setImageResource(exerciseList!![currentExercisePosition].getImage())
        binding?.tvExerciseName?.text = exerciseList!![currentExercisePosition].getName()
        binding?.tvUpcomingLabel?.visibility = View.INVISIBLE
        binding?.tvUpcomingExerciseName?.visibility = View.INVISIBLE

        if(exerciseTimer != null) {
            exerciseTimer?.cancel()
            exerciseProgress = 0
        }

        speakOut(binding?.tvUpcomingExerciseName?.text.toString())
        //speakOut(exerciseList!![currentExercisePosition].getName())

        setExerciseProgressBar()
    }

    private fun setRestProgressBar() { //Timers
        //how far have we been is the progress of the progress bar
        binding?.progressBar?.progress = restProgress
        restTimer = object : CountDownTimer(restTimerDuration * 1000, 1000) {

            override fun onTick(millisUntilFinished: Long) {
                restProgress++
                binding?.progressBar?.progress = 10 - restProgress
                binding?.tvTimer?.text = (10 - restProgress).toString()
            }

            override fun onFinish() {
            //Toast.makeText(this@ExerciseActivity,"Here now we will start the exercise.",Toast.LENGTH_LONG).show()
                currentExercisePosition++

                exerciseList!![currentExercisePosition].setIsSelected(true)
                exerciseAdapter!!.notifyDataSetChanged()

                setupExerciseView()
            }
        }.start()
    }

    private fun setExerciseProgressBar() { //Timers
        //how far have we been is the progress of the progress bar
        binding?.progressBarExercise?.progress = exerciseProgress
        exerciseTimer = object : CountDownTimer(exerciseTimerDuration * 1000, 1000) {

            override fun onTick(millisUntilFinished: Long) {
                exerciseProgress++
                binding?.progressBarExercise?.progress = 30 - exerciseProgress
                binding?.tvTimerExercise?.text = (30 - exerciseProgress).toString()
            }

            override fun onFinish() {
                //Toast.makeText(this@ExerciseActivity,"30 sec are over , let's go to the rest view.",Toast.LENGTH_LONG).show()
                if(currentExercisePosition < exerciseList?.size!! - 1){
                    exerciseList!![currentExercisePosition].setIsSelected(false)
                    exerciseList!![currentExercisePosition].setIsComplete(true)
                    exerciseAdapter!!.notifyDataSetChanged()
                    setupRestView()
                }else{
                    Toast.makeText(this@ExerciseActivity,"Congratulations! You have completed the 7 Minutes workout.",Toast.LENGTH_SHORT).show()
                    val intent = Intent(this@ExerciseActivity,FinishActivity::class.java)//we need more information for <<this>> argument because it thinks it is the countdown timer
                    //with this keyword we are referring in the object we are creating which is the count down timer
                    startActivity(intent)
                    finish()
                }
            }
        }.start()
    }

    override fun onDestroy() {
        super.onDestroy()

        if(restTimer != null) {
            restTimer?.cancel()
            restProgress = 0
        }

        if(exerciseTimer != null) {
            exerciseTimer?.cancel()
            exerciseProgress = 0
        }

        //Shitting down the Text to Speech feature when activity is destroyed
        //Start
        if(tts != null){
            tts!!.stop()
            tts!!.shutdown()
        }

        if(player != null){
            player!!.stop()
        }

        binding = null
    }

    override fun onInit(status : Int){
        if(status == TextToSpeech.SUCCESS){
            //Set Us English as language for tts
            val result = tts?.setLanguage(Locale.US)
            speakOut("Upcoming exercise ${binding?.tvUpcomingExerciseName?.text.toString()}")
            if(result == TextToSpeech.LANG_MISSING_DATA || result == TextToSpeech.LANG_NOT_SUPPORTED){
                Log.e("TTS","The Language specified is not supported!")
            }
        }else{
            Log.e("TTS","Initialization Failed!")
        }
    }

    private fun speakOut(text : String){
        if(tts == null){
            Log.e("TTS","Text is Empty!")
        }else{
            tts!!.speak(text,TextToSpeech.QUEUE_FLUSH,null,"")
        }
    }



}