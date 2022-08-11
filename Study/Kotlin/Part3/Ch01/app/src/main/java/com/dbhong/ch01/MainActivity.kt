package com.dbhong.ch01

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.TextView
import com.google.firebase.messaging.FirebaseMessaging

class MainActivity : AppCompatActivity() {

    private val resultTextView : TextView by lazy {
        findViewById(R.id.resultTextView)
    }

    private val firebaseTokenTextView : TextView by lazy {
        findViewById(R.id.firebaseTokenTextView)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        initViews()
        bindViews()
        initFirebase()
        updateResult()
    }

    private fun initViews() {

    }

    private fun bindViews() {

    }

    override fun onNewIntent(intent: Intent?) {
        super.onNewIntent(intent)

        setIntent(intent)
        updateResult(true)
    }

    private fun initFirebase() {
        FirebaseMessaging.getInstance().token.addOnCompleteListener { task ->
            if(task.isSuccessful) {
                firebaseTokenTextView.text = task.result
                Log.e(TAG, "Token : ${firebaseTokenTextView.text}")
            }
        }
    }

    private fun updateResult(isOnNewIntent : Boolean = false) {
        resultTextView.text  = (intent.getStringExtra("notificationType") ?: "앱 런처") +
                if(isOnNewIntent) {
                "(으)로 갱신했습니다."
                } else {
                    "(으)로 실행했습니다."
                }
    }

    companion object {
        const val TAG = "MainActivity"
    }
}