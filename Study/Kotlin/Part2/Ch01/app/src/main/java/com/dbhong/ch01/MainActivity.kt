package com.dbhong.ch01

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.Button
import android.widget.EditText
import android.widget.Toast

class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        val heightEditText : EditText = findViewById(R.id.etHeight)
        val weightEditText : EditText = findViewById(R.id.etWeight)

        val resultButton : Button = findViewById(R.id.button)
        resultButton.setOnClickListener {
            if (heightEditText.text.isEmpty() || weightEditText.text.isEmpty()) {
                Toast.makeText(this, "Data is empty", Toast.LENGTH_SHORT).show()
                return@setOnClickListener //어떤 메소드에서 return 할지 명시.
            }

            val height : Int = heightEditText.text.toString().toInt()
            val weight : Int = weightEditText.text.toString().toInt()

            val intent : Intent = Intent(this, ResultActivity::class.java)

            intent.putExtra("height", height)
            intent.putExtra("weight", weight)
            startActivity(intent)
        }

    }
}