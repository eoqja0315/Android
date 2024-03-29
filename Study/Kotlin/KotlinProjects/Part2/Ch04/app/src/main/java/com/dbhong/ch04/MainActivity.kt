package com.dbhong.ch04

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.text.Spannable
import android.text.SpannableStringBuilder
import android.text.style.ForegroundColorSpan
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.widget.LinearLayout
import android.widget.TextView
import android.widget.Toast
import androidx.core.view.isVisible
import androidx.room.Room
import com.dbhong.ch04.model.History
import java.lang.NumberFormatException
import kotlin.math.exp
import kotlin.system.exitProcess

class MainActivity : AppCompatActivity() {

    lateinit var db : AppDatabase

    private val historyLinearlayout : LinearLayout by lazy {
        findViewById(R.id.historyLinearLayout)
    }

    private val historyLayout : View by lazy {
        findViewById<View>(R.id.historyLayout)
    }

    private val expressionTextView : TextView by lazy {
        findViewById(R.id.expressionTextView)
    }

    private val resultTextView : TextView by lazy {
        findViewById(R.id.resultTextView)
    }

    private var isOperator = false
    private var hasOperator = false

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        db = Room.databaseBuilder(
            applicationContext,
            AppDatabase::class.java,
            "historyDB"
        ).build()
    }

    fun buttonClicked(v : View) {
        when(v.id){
            R.id.button0-> numberButtonClicked("0")
            R.id.button1-> numberButtonClicked("1")
            R.id.button2 -> numberButtonClicked("2")
            R.id.button3 -> numberButtonClicked("3")
            R.id.button4 -> numberButtonClicked("4")
            R.id.button5 -> numberButtonClicked("5")
            R.id.button6 -> numberButtonClicked("6")
            R.id.button7 -> numberButtonClicked("7")
            R.id.button8 -> numberButtonClicked("8")
            R.id.button9 -> numberButtonClicked("9")
            R.id.buttonPlus -> operationButtonClicked("+")
            R.id.buttonMinus -> operationButtonClicked("-")
            R.id.buttonMulti -> operationButtonClicked("*")
            R.id.buttonDivider -> operationButtonClicked("/")
            R.id.buttonModulo -> operationButtonClicked("%")
        }
    }

    private fun operationButtonClicked(operator : String) {
        if (expressionTextView.text.isEmpty()) {
            return
        }

        when {
            isOperator -> {
                val text = expressionTextView.text.toString()
                expressionTextView.text = text.dropLast(1) + operator //맨 끝에서 한 자리 지워줌
            }
            hasOperator -> {
                Toast.makeText(this, "연산자는 한 번만 사용할 수 있습니다.", Toast.LENGTH_SHORT).show()
                return
            }
            else -> {
                expressionTextView.append(" $operator")
            }
        }

        val ssb = SpannableStringBuilder(expressionTextView.text)
        ssb.setSpan(
                ForegroundColorSpan(getColor(R.color.green)),
                expressionTextView.text.length - 1,
                expressionTextView.text.length,
                Spannable.SPAN_EXCLUSIVE_EXCLUSIVE) // 연산자만 색칠

        expressionTextView.text = ssb

        isOperator = true
        hasOperator = true
    }

    private fun numberButtonClicked(number : String) {

        if(isOperator) {
            expressionTextView.append(" ")
        }

        isOperator = false

        val expressionText = expressionTextView.text.split(" ")

        if (expressionText.isNotEmpty() && expressionText.last().length > 15) {
            Toast.makeText(this, "15 자리까지만 사용할 수 있습니다.", Toast.LENGTH_SHORT).show()
            return
        } else if(expressionText.last().isEmpty() && number == "0") {
            Toast.makeText(this, "0은 제일 앞에 올 수 없습니다.", Toast.LENGTH_SHORT).show()
            return
        }

        expressionTextView.append(number)
        resultTextView.text = calculateExpression()
    }

    private fun calculateExpression() : String {
        val expressionTexts = expressionTextView.text.split(" ")

        if (hasOperator.not() || expressionTexts.size != 3) {
            return ""
        } else if (expressionTexts[0].isNumber().not() && expressionTexts[2].isNumber().not()){
            return ""
        }

        val exp1 = expressionTexts[0].toBigInteger()
        val exp2 = expressionTexts[2].toBigInteger()
        val operator = expressionTexts[1]

        return when(operator) {
            "+" -> (exp1 + exp2).toString()
            "-" -> (exp1 - exp2).toString()
            "*" -> (exp1 * exp2).toString()
            "/" -> (exp1 / exp2).toString()
            "%" -> (exp1 % exp2).toString()
            else -> ""
        }
    }

    fun clearButtonClicked(v : View) {
        expressionTextView.text = ""
        resultTextView.text = ""

        isOperator = false
        hasOperator = false
    }

    fun resultButtonClicked(v : View) {
        val expressionTexts = expressionTextView.text.split(" ")

        if(expressionTextView.text.isEmpty() || expressionTexts.size == 1) {
            return
        }

        if (expressionTexts.size != 3 && hasOperator) {
            Toast.makeText(this, "아직 완성되지 않은 수식입니다.", Toast.LENGTH_SHORT).show()
        }

        if (expressionTexts[0].isNumber().not() && expressionTexts[2].isNumber().not()){
            Toast.makeText(this, "오류가 발생했습니다.", Toast.LENGTH_SHORT).show()
        }

        val expressionText = expressionTextView.text.toString()
        val resultText = calculateExpression()

        Thread {
            db.historyDao().insertHistory(History(null, expressionText, resultText))
        }.start()

        resultTextView.text = ""
        expressionTextView.text = resultText

        isOperator = false
        hasOperator = false
    }

    fun historyButtonClicked(v : View) {
        historyLayout.isVisible = true
        historyLinearlayout.removeAllViews()

        Thread(Runnable {
            db.historyDao().getAll().reversed().forEach{
                runOnUiThread{ //UIthread open
                    val historyView = LayoutInflater.from(this).inflate(R.layout.history_row, null, false)
                    historyView.findViewById<TextView>(R.id.expressionTextView).text = it.expression
                    historyView.findViewById<TextView>(R.id.resultTextView).text = "= ${it.result}"
                    historyLinearlayout.addView(historyView)
                }
            }
        }).start()
    }
    
    fun closeHistoryButtonClicked(v : View){
        historyLayout.isVisible = false
    }

    fun historyClearButtonClicked(v : View) {
        historyLinearlayout.removeAllViews()
        Thread(Runnable {
            db.historyDao().deleteAll()
        })
    }
}

//확장함수 사용
fun String.isNumber() : Boolean {
    return try {
        this.toBigInteger()
        true
    } catch (e : NumberFormatException) {
        false
    }
}