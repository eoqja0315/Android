package com.dbhong.ch03

import android.content.Context
import android.content.Intent
import android.graphics.Color
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button
import android.widget.NumberPicker
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.core.content.edit

class MainActivity : AppCompatActivity() {

    private val numberPicker1 : NumberPicker by lazy {
        findViewById<NumberPicker>(R.id.numberPicker1).apply{
            minValue = 0
            maxValue = 9
        }
    }

    private val numberPicker2 : NumberPicker by lazy {
        findViewById<NumberPicker>(R.id.numberPicker2).apply{
            minValue = 0
            maxValue = 9
        }
    }

    private val numberPicker3 : NumberPicker by lazy {
        findViewById<NumberPicker>(R.id.numberPicker3).apply{
            minValue = 0
            maxValue = 9
        }
    }

    private val changePasswordButton : Button by lazy {
        findViewById(R.id.changePasswordButton)
    }

    private val openButton : Button by lazy {
        findViewById(R.id.openButton)
    }

    private var changePasswordMode = false

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        numberPicker1
        numberPicker2
        numberPicker3

        val passwordPreferences = getSharedPreferences("password", Context.MODE_PRIVATE)
        val passwordFromUser = "${numberPicker1.value}${numberPicker2.value}${numberPicker3.value}"

        openButton.setOnClickListener{
            if( changePasswordMode) {
                Toast.makeText(this, "비밀번호 변경 중", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }

            if (passwordPreferences.getString("password", "000").equals(passwordFromUser)) {
                startActivity(Intent(this, DiaryActivity::class.java))
            } else {
                showErrorAlertDialog()
            }
        }

        changePasswordButton.setOnClickListener{
            if (changePasswordMode) {
               passwordPreferences.edit(true) { //commit : 저장하는 동안 UI 멈춤, apply : 스레드 실행
                   putString("password", passwordFromUser)
               }

                changePasswordMode = false
                changePasswordButton.setBackgroundColor(Color.BLACK)

            } else {

                if (passwordPreferences.getString("password", "000").equals(passwordFromUser)) {
                    changePasswordMode = true
                    Toast.makeText(this, "변경할 패스워드를 입력해주세요", Toast.LENGTH_SHORT).show()

                    changePasswordButton.setBackgroundColor(Color.RED)

                } else {
                    showErrorAlertDialog()
                }
            }
        }
    }

    private fun showErrorAlertDialog() {
        AlertDialog.Builder(this)
                .setTitle("Failed")
                .setMessage("Incorrect password")
                .setPositiveButton("Confirm"){ _ , _ -> } //Nothing
                .create()
                .show()
    }
}