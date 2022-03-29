package com.dbhong.ch03

import android.content.Context
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.util.Log
import android.widget.EditText
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.edit
import androidx.core.widget.addTextChangedListener

class DiaryActivity : AppCompatActivity() {

    private val handler = Handler(Looper.getMainLooper()) //Main Thread에 연결된 handler 하나 생성
    
    private val etDiary : EditText by lazy {
        findViewById(R.id.etDiary)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_diary)

        val detailPreference = getSharedPreferences("diary", Context.MODE_PRIVATE)
        etDiary.setText(detailPreference.getString("detail", ""))

        val runnable = Runnable {
            getSharedPreferences("diary", Context.MODE_PRIVATE).edit{
                putString("detail", etDiary.text.toString())
            }
            Log.d("DiaryAcitivity", "SAVE!!! ${etDiary.text.toString()}")
        }

//        val t = Thread(Runnable {
//
//            handler.post { //MainThread로 post 블럭을 던져줌
//
//            }
//
//            Log.e("DiaryActivity", "aa")
//        }).start()

        etDiary.addTextChangedListener {
            Log.d("DiaryActivity", "TextChanged :: $it")
            handler.removeCallbacks(runnable)
            handler.postDelayed(runnable, 500)
        }
    }
}