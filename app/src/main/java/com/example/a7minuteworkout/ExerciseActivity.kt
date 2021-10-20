package com.example.a7minuteworkout

import android.app.Dialog
import android.content.Intent
import android.media.MediaPlayer
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.CountDownTimer
import android.speech.tts.TextToSpeech
import android.util.Log
import android.view.View
import android.widget.LinearLayout
import android.widget.Toast
import androidx.recyclerview.widget.LinearLayoutManager
import kotlinx.android.synthetic.main.activity_exercise.*
import kotlinx.android.synthetic.main.dialog_custom_back_confirmation.*
import java.lang.Exception
import java.util.*


class ExerciseActivity : AppCompatActivity(),TextToSpeech.OnInitListener {

    private var  restTimer: CountDownTimer? = null
    private var restProgress = 0
    private var exerciseTimer: CountDownTimer? = null
    private var excerciseProgress = 0
    private var exerciseList: ArrayList<ExerciseModel>? = null
    private var currentExercisePosition = -1
    private var exerciseTimerDuration: Long = 30
    private var restTimerDuration: Long = 10
    private var tts:TextToSpeech? = null
    private  var player: MediaPlayer? = null
    private var exerciseAdapter: ExcerciseStatusAdapter? = null
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_exercise)

        setSupportActionBar(toolbar_exercise_activity)
        val actionBar = supportActionBar
        if(actionBar != null){
            actionBar.setDisplayHomeAsUpEnabled(true)
        }
        toolbar_exercise_activity.setNavigationOnClickListener {
            customDialogForBackBtn()
        }
        setupRestView()
        exerciseList = Constants.defaultExerciseList()
        tts = TextToSpeech(this, this)
        setupExerciseStatusRecyclerView()

    }

    public override fun onDestroy() {

        if(restTimer!= null){
            restTimer!!.cancel()
            restProgress = 0
        }
        if(tts!= null){
            tts!!.stop()
            tts!!.shutdown()
        }
        if (exerciseTimer != null) {
            exerciseTimer!!.cancel()
            excerciseProgress= 0
        }
        if(player != null){
            player!!.stop()
        }
        super.onDestroy()
    }
    private fun setResProgressBar(){
            progressBar.progress = restProgress
        restTimer = object: CountDownTimer(restTimerDuration*1000, 1000 ){
            override fun onTick(millisUntilFinished: Long) {
                restProgress++
                progressBar.progress=10-restProgress
                tvTimer.text = (10-restProgress).toString()

                tv_get_up_coming_exercise.text = exerciseList!![currentExercisePosition+1]?.getName()

            }

            override fun onFinish() {
                currentExercisePosition++
                exerciseList!![currentExercisePosition].setIsSelected(true)
                exerciseAdapter!!.notifyDataSetChanged()
                setupExerciseView()

            }

        }.start()
    }
    private fun setExerciseProgressBar(){
        llRestView.visibility = View.GONE
        llExerciseView.visibility = View.VISIBLE
        progressBar2.progress = excerciseProgress
        exerciseTimer = object: CountDownTimer(exerciseTimerDuration*1000, 1000 ){
            override fun onTick(millisUntilFinished: Long) {

                excerciseProgress++
                progressBar2.progress=30-excerciseProgress
                tvTimer2.text = (30-excerciseProgress).toString()

            }

            override fun onFinish() {
                if(currentExercisePosition< exerciseList?.size!!-1)
                {
                    exerciseList!![currentExercisePosition].setIsSelected(false)
                    exerciseList!![currentExercisePosition].setIsCompleted(true)
                    exerciseAdapter!!.notifyDataSetChanged()
                    setupRestView()

                }
                else{
                finish()
                    val intent = Intent(this@ExerciseActivity, FinishActivity::class.java)
                    startActivity(intent)

                }

            }

        }.start()
    }
    private  fun setupRestView(){
        try {
            player = MediaPlayer.create(applicationContext, R.raw.press_start)
            player!!.isLooping = false
            player!!.start()
        }catch (e:Exception){
           e.printStackTrace()
        }

        llRestView.visibility = View.VISIBLE
        llExerciseView.visibility =View.GONE
        if(restTimer != null)
        {
            restTimer!!.cancel()
            restProgress = 0
        }
        setResProgressBar()
    }

    private  fun setupExerciseView(){

        if(exerciseTimer != null)
        {
            exerciseTimer!!.cancel()
            excerciseProgress = 0
        }
        speakOut(exerciseList!![currentExercisePosition].getName())
        iv_image.setImageResource(exerciseList!![currentExercisePosition].getImage())
        tv_exercise_name.text = (exerciseList!![currentExercisePosition].getName())

        setExerciseProgressBar()


    }

    override fun onInit(status: Int) {
        if(status==TextToSpeech.SUCCESS){
            val result = tts!!.setLanguage(Locale.US)
            if(result ==TextToSpeech.LANG_NOT_SUPPORTED ||result ==TextToSpeech.LANG_MISSING_DATA)
            {
                Log.e("TTS", "the language specified is not supported")
            }
        }
        else{
            Log.e("TTS", "initialization Failed")
        }

    }
    private fun speakOut(text: String){
        tts!!.speak(text, TextToSpeech.QUEUE_FLUSH, null, "")
    }
    private fun setupExerciseStatusRecyclerView()
    {
        rvExerciseStatus.layoutManager = LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false)
        exerciseAdapter = ExcerciseStatusAdapter(exerciseList!!, this)
        rvExerciseStatus.adapter = exerciseAdapter
    }

    private  fun customDialogForBackBtn()
    {
        val customDialog = Dialog(this)
        customDialog.setContentView(R.layout.dialog_custom_back_confirmation)
        customDialog.btnYes.setOnClickListener {
            finish()
            customDialog.dismiss()
        }
        customDialog.btnCancel.setOnClickListener {

            customDialog.dismiss()
        }
        customDialog.show()

    }
}