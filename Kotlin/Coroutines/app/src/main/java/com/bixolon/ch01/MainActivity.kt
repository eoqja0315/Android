package com.bixolon.ch01

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import kotlinx.coroutines.runBlocking

//class MainActivity : AppCompatActivity() {
//
//    override fun onCreate(savedInstanceState: Bundle?) {
//        super.onCreate(savedInstanceState)
//        setContentView(R.layout.activity_main)
//    }
//}

//runBlocking은 실행되는 동안 다른 블록이 수행되지 못하도록 막음
//fun main() = runBlocking {
//    println(coroutineContext)
//    println(this)
//    println(Thread.currentThread().name) //어떤 스레드에서 수행하고 있는지 알려줌. 디버깅할 때 사용
//    println("Hello")
//}

//하나의 스레드로 수행 >> runBlocking 다음에 launch 수행
//runBlocking이 먼저 호출되었고 runBlocking은 메인스레드를 수행하므로 launch는 잠시 대기 후 실행됨
//fun main() = runBlocking {
//    launch {
//        println("launch : ${Thread.currentThread().name}")
//        println("World!")
//    }
//    println("runBlocking : ${Thread.currentThread().name}")
//    println("Hello")
//}

fun main() = runBlocking {
    launch {
        println("launch : ${Thread.currentThread().name}")
        println("World!")
    }
    println("runBlocking : ${Thread.currentThread().name}")
    delay(500L)
    println("Hello")
}