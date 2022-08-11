package com.dbhong.ch06

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.CountDownTimer
import android.widget.SeekBar
import android.widget.TextView

class MainActivity : AppCompatActivity() {

    private val remainMinutesTextView : TextView by lazy {
        findViewById(R.id.remainMinutesTextView)
    }

    private val remainSecondsTextView : TextView by lazy {
        findViewById(R.id.remainSecondsTextView)
    }

    private val seekBar : SeekBar by lazy {
        findViewById(R.id.seekBar)
    }

    private var currentCountDownTimer : CountDownTimer? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        bindViews()
        updateRemainTimes(0)
    }

    private fun bindViews() {
        seekBar.setOnSeekBarChangeListener(
            object : SeekBar.OnSeekBarChangeListener {
                override fun onProgressChanged(seekBar: SeekBar?, progress : Int, fromUser: Boolean) {
                    if(fromUser)
                        updateRemainTimes(progress * 60 * 1000L)
                }

                override fun onStartTrackingTouch(seekBar: SeekBar?) {
                    stopTimer()
                }

                override fun onStopTrackingTouch(seekBar : SeekBar?) {
                    seekBar ?: return //if(seekBar == null) return
                    if(seekBar.progress == 0) {
                        stopTimer()
                    } else {
                        startTimer()
                    }
                }
            }
        )
    }

    private fun stopTimer() {
        currentCountDownTimer?.cancel()
        currentCountDownTimer = null
    }

    private fun startTimer() {
        currentCountDownTimer = createCountDownTimer(seekBar.progress * 60 * 1000L)
        currentCountDownTimer?.start()
    }


    private fun createCountDownTimer(initialMillis : Long) =
        object : CountDownTimer(initialMillis, 1000L) {
            override fun onTick(millisUntilFinished: Long) {
                updateRemainTimes(millisUntilFinished)
                updateSeekBar(millisUntilFinished)
            }

            override fun onFinish() {
                updateRemainTimes(0)
                updateSeekBar(0)
            }
        }

    private fun updateRemainTimes(remainMillis : Long) {
        val remainSeconds = remainMillis / 1000

        remainMinutesTextView.text = "%02d'".format(remainSeconds / 60)
        remainSecondsTextView.text = "%02d".format(remainSeconds % 60)
    }

    private fun updateSeekBar(remainMillis: Long) {
        seekBar.progress = (remainMillis / 60000).toInt()
    }
}