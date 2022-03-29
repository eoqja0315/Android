package com.dbhong.ch02

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.Button
import android.widget.NumberPicker
import android.widget.TextView
import android.widget.Toast
import androidx.core.content.ContextCompat
import androidx.core.view.isVisible
import org.w3c.dom.Text
import retrofit2.Call
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.Callback
import retrofit2.Response

class MainActivity : AppCompatActivity() {

    private val clearButton : Button by lazy {
        findViewById<Button>(R.id.clearButton)
    }

    private val addButton : Button by lazy {
        findViewById<Button>(R.id.addButton)
    }

    private val runButton : Button by lazy {
        findViewById<Button>(R.id.runButton)
    }

    private val numberPicker : NumberPicker by lazy {
        findViewById<NumberPicker>(R.id.numberPicker)
    }

    private val numberTvList : List<TextView> by lazy {
        listOf<TextView>(findViewById<TextView>(R.id.tvNumber1),
                findViewById<TextView>(R.id.tvNumber2),
                findViewById<TextView>(R.id.tvNumber3),
                findViewById<TextView>(R.id.tvNumber4),
                findViewById<TextView>(R.id.tvNumber5),
                findViewById<TextView>(R.id.tvNumber6))
    }

        private var didRun = false

        private val pickNumberSet = hashSetOf<Int>()

        override fun onCreate(savedInstanceState: Bundle?) {
            super.onCreate(savedInstanceState)
            setContentView(R.layout.activity_main)

            numberPicker.minValue = 1
            numberPicker.maxValue = 45

            initRunButton()
            initAddButton()
            initClearButton()
        }

        private fun initRunButton() {
            runButton.setOnClickListener {
                val list = getRandomNumber()
                println(list)
                didRun = true;

                list.forEachIndexed { index, number ->
                    val textView = numberTvList[index]

                    textView.text = number.toString()
                    textView.isVisible = true
                    setNumberBackground(number, textView)
                }
            }
        }

        private fun  initAddButton() {
            addButton.setOnClickListener{
                if (didRun) {
                    Toast.makeText(this, "초기화 후에 시도해주세요.  ",Toast.LENGTH_SHORT).show()
                    return@setOnClickListener //setOnClickListener 만 return
                }

                if (pickNumberSet.size > 5) {
                    Toast.makeText(this, "번호는 여섯 개까지만 선택할 수 있습니다.", Toast.LENGTH_SHORT).show()
                    return@setOnClickListener
                }

                if(pickNumberSet.contains(numberPicker.value)) {
                    Toast.makeText(this, "이미 선택된 번호입니다.", Toast.LENGTH_SHORT).show()
                    return@setOnClickListener
                }

                val textView = numberTvList[pickNumberSet.size]
                textView.isVisible = true
                textView.text = numberPicker.value.toString()

                setNumberBackground(numberPicker.value, textView)
                pickNumberSet.add(numberPicker.value)

            }
        }

        private fun setNumberBackground(number : Int, textView : TextView) {
            when(number) {
                in 1..10 ->  textView.background = ContextCompat.getDrawable(this, R.drawable.circle_yellow)
                in 11..20 ->  textView.background = ContextCompat.getDrawable(this, R.drawable.circle_blue)
                in 21..30 ->  textView.background = ContextCompat.getDrawable(this, R.drawable.circle_red)
                in 31..40 ->  textView.background = ContextCompat.getDrawable(this, R.drawable.circle_gray)
                else -> textView.background = ContextCompat.getDrawable(this, R.drawable.circle_green)
            }
        }

        private fun initClearButton() {
            clearButton.setOnClickListener{
                pickNumberSet.clear()

                numberTvList.forEach {
                    it.isVisible = false
                }

                didRun = false
            }
        }

        private fun getRandomNumber() : List<Int> {
            val numberList = mutableListOf<Int>().apply{
                for(i in 1..45) {
                    if (pickNumberSet.contains(i)) {
                        continue;
                    }
                    this.add(i)
                }
            }
            numberList.shuffle()
            return pickNumberSet.toList() + numberList.subList(0, 6 - pickNumberSet.size).sorted()
        }
    }
