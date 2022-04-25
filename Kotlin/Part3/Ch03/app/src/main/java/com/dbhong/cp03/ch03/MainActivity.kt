package com.dbhong.cp03.ch03

import android.app.AlarmManager
import android.app.PendingIntent
import android.app.TimePickerDialog
import android.content.Context
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button
import android.widget.TextView
import java.util.*

class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        initViews()

        val model = fetchDataFromSharedPreferences()
        renderView(model)
    }

    private fun initViews() {
        initOnOffButton()
        initChangeAlarmTimeButton()
    }

    private fun initOnOffButton() {
        val onOffButton = findViewById<Button>(R.id.onOffButton)

        onOffButton.setOnClickListener {
            val model = it.tag as? AlarmDisplayModel ?: return@setOnClickListener
            val newModel = saveAlarmModel(model.hour, model.minute, model.onOff.not())
            renderView(newModel)

            if(newModel.onOff) {
                val calendar = Calendar.getInstance().apply {
                    set(Calendar.HOUR_OF_DAY, newModel.hour)
                    set(Calendar.MINUTE, newModel.minute)

                    if(before(Calendar.getInstance())) {
                        add(Calendar.DATE, 1)
                    }
                }

                val alarmManager = getSystemService(ALARM_SERVICE) as AlarmManager
                val intent = Intent(this, AlarmReceiver::class.java)
                val pendingIntent = PendingIntent.getBroadcast(
                    this,
                    ALARM_REQUEST_CODE,
                    intent,
                    PendingIntent.FLAG_UPDATE_CURRENT
                )

                alarmManager.setInexactRepeating(
                    AlarmManager.RTC_WAKEUP,
                    calendar.timeInMillis,
                    AlarmManager.INTERVAL_DAY,
                    pendingIntent
                )

            } else {
                cancleAlarm()
            }
        }
    }

    private fun cancleAlarm() {
        val pendingIntent = PendingIntent.getBroadcast(this, ALARM_REQUEST_CODE, Intent(this, AlarmReceiver::class.java), PendingIntent.FLAG_NO_CREATE)
        pendingIntent?.cancel()
    }
    private fun initChangeAlarmTimeButton(){
        val changeAlarmTimeButton = findViewById<Button>(R.id.changeAlarmTime)
        changeAlarmTimeButton.setOnClickListener {

            val calender = Calendar.getInstance()

            TimePickerDialog(this, {picker, hour, minute ->
                val model = saveAlarmModel(hour, minute, false)
                renderView(model)
                cancleAlarm()

            }, calender.get(Calendar.HOUR_OF_DAY), calender.get(Calendar.MINUTE), false)
                .show()
        }
    }

    private fun saveAlarmModel(
        hour : Int,
        minute : Int,
        onOff : Boolean
    ) : AlarmDisplayModel{
        val model = AlarmDisplayModel(
            hour = hour,
            minute = minute,
            onOff = onOff
        )

        val sharedPreferences = getSharedPreferences("time", Context.MODE_PRIVATE)

        with(sharedPreferences.edit()) {
            putString("alarm", model.makeDataForDB())
            putBoolean("onOff", model.onOff)
            commit()
        }

        return model
    }

    private fun fetchDataFromSharedPreferences() : AlarmDisplayModel{
        val sharedPreferences = getSharedPreferences(SHARED_PREFERENCE_NAME, Context.MODE_PRIVATE)

        val timeDBValue = sharedPreferences.getString(ALARM_KEY, "9:30") ?: "9:30"
        val onOffDBValue = sharedPreferences.getBoolean(ON_OFF_KEY, false)
        val alarmData = timeDBValue.split(":")

        val alarmDisplayModel = AlarmDisplayModel(
            hour = alarmData[0].toInt(),
            minute = alarmData[1].toInt(),
            onOff = onOffDBValue
        )

        val pendingIntent = PendingIntent.getBroadcast(this, ALARM_REQUEST_CODE, Intent(this, AlarmReceiver::class.java), PendingIntent.FLAG_NO_CREATE)

        if ((pendingIntent == null) and alarmDisplayModel.onOff) {
            //알람은 실제로 꺼져있고 데이터는 켜져있음
            alarmDisplayModel.onOff = false
        } else if ((pendingIntent != null) and !alarmDisplayModel.onOff) {
            //알람은 켜져 있는데 데이터는  꺼져있음
            //알람을 취소함
            pendingIntent.cancel()
        }

        return alarmDisplayModel
    }

    private fun renderView(alarmDisplayModel: AlarmDisplayModel) {
        findViewById<TextView>(R.id.ampmTextView).apply {
            text = alarmDisplayModel.ampmText
        }

        findViewById<TextView>(R.id.timeTextView).apply {
            text = alarmDisplayModel.timeText
        }

        findViewById<Button>(R.id.onOffButton).apply {
            text = alarmDisplayModel.onOffText
            tag = alarmDisplayModel
        }
    }
    companion object {
        private const val SHARED_PREFERENCE_NAME = "time"
        private const val ALARM_KEY = "alarm"
        private const val ON_OFF_KEY = "onOff"

        private const val ALARM_REQUEST_CODE = 1000
    }
}