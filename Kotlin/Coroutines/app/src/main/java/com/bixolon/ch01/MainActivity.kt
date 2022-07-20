package com.bixolon.ch01

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import kotlinx.coroutines.*

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

//fun main() = runBlocking {
//    launch {
//        println("launch : ${Thread.currentThread().name}")
//        println("World!")
//    }
//    println("runBlocking : ${Thread.currentThread().name}")
//    delay(500L)
//    println("Hello")
//}
//
//fun main() = runBlocking {
//    launch {
//        println("launch : ${Thread.currentThread().name}")
//        println("World!")
//    }
//
//    println("runBlocking : ${Thread.currentThread().name}")
//    Thread.sleep(500) //Thread 와 delay의 차이를 보여줌
//    println("Hello")
//}

//fun main() = runBlocking {
//    launch {
//        println("launch1 : ${Thread.currentThread().name}")
//        delay(1000L) //suspension point
//        println("3!")
//    }
//    launch {
//        println("launch2 : ${Thread.currentThread().name}")
//        println("1!")
//    }
//
//    println("runBlocking : ${Thread.currentThread().name}")
//    delay(500L)//suspension point
//    println("2!")
//}

//fun main() = runBlocking {
//    launch {
//        println("launch1 : ${Thread.currentThread().name}")
//        delay(200L) //suspension point
//        println("3!")
//    }
//    launch {
//        println("launch2 : ${Thread.currentThread().name}")
//        delay(1000L) //suspension point
//        println("1!")
//    }
//
//    println("runBlocking : ${Thread.currentThread().name}")
//    delay(500L)//suspension point
//    println("2!")
//}

//fun main() {
//    //계층적, 구조적
//    //부모 launch1 launch2 를 자식으로 가짐. runBlocking의 루틴이 모두 끝나더라도 자식 launch가 종료되지 않는 한, 종료되지 않음
//    //부모 cancle시 자식 launch들도 모두 cancle됨
//    runBlocking {
//        launch {
//            println("launch1 : ${Thread.currentThread().name}")
//            delay(1000L) //suspension point
//            println("3!")
//        }
//        launch {
//            println("launch2 : ${Thread.currentThread().name}")
//            println("1!")
//        }
//
//        println("runBlocking : ${Thread.currentThread().name}")
//        delay(500L)//suspension point
//        println("2!")
//    }
//    println("4!") //4가 delay를 얼마 주더라도 항상 늦게 출력되는 것을 알 수 있음.
//}

//suspend fun doThree() {
//    println("launch1 : ${Thread.currentThread().name}")
//    delay(1000L) //suspension point
//    println("3!")
//}
//
////delay가 없을 땐 suspend 키워드 없어도 됨
//fun doOne() {
//    println("launch2 : ${Thread.currentThread().name}")
//    println("1!")
//}
//
//suspend fun doTwo() {
//    println("runBlocking : ${Thread.currentThread().name}")
//    delay(500L)//suspension point
//    println("2!")
//
//}
//
//fun main() {
//
//    runBlocking<Unit> {
//        launch {
//            doThree()
//        }
//        launch {
//            doOne()
//        }
//
//        doTwo()
//    }
//}

//launch는 반드시 코루틴 안에서 호출되어야함
//suspend 키워드는 단순히 잠들 수 있다는 뜻이지 코루틴은 아님
//suspend fun doOneTwoThree() = coroutineScope{ //this : 코루틴
//
//    val job = launch { // corountineScope의 자식으로써 새로운 코루틴을 생성 this : 코루틴. Receiver. 수신객체
//        println("launch1 : ${Thread.currentThread().name}")
//        delay(1000L) //suspension point
//        println("3!")
//    }
//    job.join() // suspension point 첫 번째 launch 블록이 끝날 때까지 기다림. 수행 결과 launch1, 3이 붙어 출력되는 것을 볼 수 있음,
//    //그 다음 launch 블록들은 대기, coroutineScope코드가 먼저 실행 돔. 3 -> 4 -> 1 -> 2 출력 순.
//
//    launch { // this : 코루틴 Receiver. 수신객체
//        println("launch2 : ${Thread.currentThread().name}")
//        println("1!")
//    }
//
//    launch { // this : 코루틴 Receiver. 수신객체
//        println("launch3 : ${Thread.currentThread().name}")
//        delay(500L) //suspension point
//        println("2!")
//    }
//
//    println("4!")
//}
//
////runBlocking 내부에서 doOneTwoThree를 호출했음
////doOneTwoThree는 코루틴이아니므로 launch를 수행하지 못한다는 에러 발생
//fun main() = runBlocking { // this : 코루틴 Receiver. 수신객체
//    doOneTwoThree() //suspension point because, doOneTwoThree is suspend function
//    println("runBlocking : ${Thread.currentThread().name}")
//    println("5!")
//}

// 코루틴의 특성 상, 10만개도 큰 부담은 아님 리소스를 많이 차지하지 않음
//suspend fun doOneTwoThree() = coroutineScope{ //코루틴 생성
//
//    val job = launch { // 코루틴 생성
//        println("launch1 : ${Thread.currentThread().name}")
//        delay(1000L)
//        println("3!")
//    }
//    job.join()
//
//    launch {
//        println("launch2 : ${Thread.currentThread().name}")
//        println("1!")
//    }
//
//    repeat(10000) { //코루틴 1만개 생성.
//        launch {
//            println("launch3 : ${Thread.currentThread().name}")
//            delay(500L)
//            println("2!")
//        }
//    }
//
//    println("4!")
//}
//
//
//fun main() = runBlocking {
//    doOneTwoThree()
//    println("runBlocking : ${Thread.currentThread().name}")
//    println("5!")
//}

//3만 출력되지 않음.
//
//suspend fun doOneTwoThree() = coroutineScope{ //코루틴 생성
//
//    val job1 = launch { // 코루틴 생성
//        println("launch1 : ${Thread.currentThread().name}")
//        delay(1000L)
//        println("3!")
//    }
//
//
//    val job2 = launch {
//        println("launch2 : ${Thread.currentThread().name}")
//        println("1!")
//    }
//
//
//    val job3 = launch {
//        println("launch3 : ${Thread.currentThread().name}")
//        delay(500L)
//        println("2!")
//    }
//
//    delay(800L)
//    job1.cancel()
//    job2.cancel()
//    job3.cancel()
//    println("4!")
//}
//
//fun main() = runBlocking {
//    doOneTwoThree()
//    println("runBlocking : ${Thread.currentThread().name}")
//    println("5!")
//}

// 1,2,3 은 출력안되고 4,5 만 되는 걸 알 수 있음.
//suspend fun doOneTwoThree() = coroutineScope{
//
//    val job1 = launch {
//        try {
//            println("launch1 : ${Thread.currentThread().name}")
//            delay(1000L)
//            println("3!")
//        } finally {
//            println("job1 is finishing!")
//            //release resource
//        }
//    }
//
//    val job2 = launch {
//        try {
//            println("launch2 : ${Thread.currentThread().name}")
//            delay(1000L)
//            println("1!")
//        } finally {
//            println("job2 is finishing!")
//            //release resource
//        }
//    }
//
//    val job3 = launch {
//        try {
//            println("launch3 : ${Thread.currentThread().name}")
//            delay(1000L)
//            println("2!")
//        } finally {
//            println("job3 is finishing!")
//            //release resource
//        }
//    }
//
//    delay(800L)
//    job1.cancel()
//    job2.cancel()
//    job3.cancel()
//
//    println("4!")
//}
//
//fun main() = runBlocking {
//    doOneTwoThree()
//    println("5!")
//}

//WithContext
//suspend fun doOneTwoThree() = coroutineScope{
//
//    val job1 = launch {
//        withContext(NonCancellable) {
//            println("launch1 : ${Thread.currentThread().name}")
//            delay(1000L)
//            println("3!")
//        }
//        delay(1000L)
//        print("job1: end")
//    }
//
//    val job2 = launch {
//        withContext(NonCancellable) {
//            println("launch2 : ${Thread.currentThread().name}")
//            delay(1000L)
//            println("1!")
//        }
//        delay(1000L)
//        print("job2: end")
//    }
//
//    //withContext >> cancle 되지 않음
//    //finally block에도 사용가능. > resource 해제 중 취소될 수 있으므로 withContext를 활용하여 취소하지 못하도록 작성
//    val job3 = launch {
//        withContext(NonCancellable) {
//            println("launch3 : ${Thread.currentThread().name}")
//            delay(1000L)
//            println("2!")
//        }
//        delay(1000L)
//        print("job3: end")
//    }
//
//    delay(800L)
//    job1.cancel()
//    job2.cancel()
//    job3.cancel()
//
//    println("4!")
//}
//
//fun main() = runBlocking {
//    doOneTwoThree()
//    println("5!")
//}

//withTimeOut 과 isActive
//
//suspend fun doCount() = coroutineScope{
//
//    val job1 = launch(Dispatchers.Default) {
//        var i = 1
//        var nextTime = System.currentTimeMillis() + 100L
//
//        //job cancle 시 isActive가 false가 됨. 이를 통해 cancle가능한 코드를 작성할 수 있음.
//        while ( i <= 10 && isActive) {
//            val currentTime = System.currentTimeMillis()
//            if (currentTime >= nextTime) {
//                println(i)
//                nextTime = currentTime + 100L
//                i++
//            }
//        }
//    }
//}
//
//fun main() = runBlocking {
//    //응답을 무한정 기다릴 수 없으므로 withTimeout 메소드로 어느 정도 수행하다가 취소할지 결정할 수 있음
//    //추가적으로 withTimeout은 TimeoutCancellationException을 발생시키는데, 매 withTimeout마다 try catch 문을 작성하는 것은 번거로운 일일 수 있음
//    //따라서 withTimeoutOrNull을 사용하여 실패할 경우 Null을 반환하도록 할 수 있음
//    val result = withTimeoutOrNull(500L) {
//        doCount()
//        true
//    } ?: false
//    println(result)
//}

//async
//import kotlinx.coroutines.*
//import kotlin.random.Random
//import kotlin.system.*
//
//suspend fun getRandom1() : Int{
//
//    delay(1000L)
//    return Random.nextInt(0, 500)
//
//}
//
//suspend fun getRandom2() : Int{
//
//    delay(1000L)
//    return Random.nextInt(0, 500)
//
//}
//
////코루틴 빌더 async 와 launch가 있음
////결과를 받아야한다면 async, 필요없으면 launch를 활용함 일반적으로.
//fun main() = runBlocking {
//    val elapsedTime = measureTimeMillis {
//        //순차적으로 수행 시(coroutine을 사용하지 않을 시), 2000ms 이상의 시간이 소요됨.
//        val value1 = async { //this : 코루틴
//
//            getRandom1()
//        }
//
//        val value2 = async { //this : 코루틴
//
//            getRandom2()
//        }
//
//        //await은 job.join() + 결과를 가져오는 효과
//        println("${value1.await()} + ${value2.await()} = ${value1.await() + value2.await()}")
//
//    }
//    println(elapsedTime)
//}

//async + lazy
//import kotlinx.coroutines.*
//import kotlin.random.Random
//import kotlin.system.*
//
//suspend fun getRandom1() : Int{
//    delay(1000L)
//    return Random.nextInt(0, 500)
//}
//
//suspend fun getRandom2() : Int{
//    delay(1000L)
//    return Random.nextInt(0, 500)
//}
//
//fun main() = runBlocking {
//    val elapsedTime = measureTimeMillis {
//
//        //start를 파라미터로 넣으면, 코루틴이 만들어지긴 하지만 수행이 되지는 않는다.
//        //.start로 호출해야만 큐에 수행 예약을 한다.
//        val value1 = async(start = CoroutineStart.LAZY) {
//            getRandom1()
//        }
//
//        val value2 = async(start = CoroutineStart.LAZY) {
//            getRandom2()
//        }
//
//        value1.start() //큐에 수행을 예약한다.
//        value2.start()
//
//        println("${value1.await()} + ${value2.await()} = ${value1.await() + value2.await()}")
//
//    }
//    println(elapsedTime)
//}

//coroutine 에서 부모와 자식간의 관계
//Exception 으로 예제
//작업1, 작업2, 작업3 를 동시에 수행한다고 할 때, 작업2가 취소됐을 경우 작업1과 3이 의미가 없는 경우 1,2,3을 하나로 묶어 활용할 수 있다.
//import kotlinx.coroutines.*
//import kotlin.random.Random
//import kotlin.system.*
//
//suspend fun getRandom1() : Int{
//    try {
//        delay(1000L)
//        return Random.nextInt(0, 500)
//    } finally {
//        println("getRandom1 is cancelled")
//    }
//}
//
//suspend fun getRandom2() : Int{
//    delay(500L)
//    throw IllegalStateException()
//}
//
////자식2에서 문제가 발생하더라도 exception은 형제와 부모에게까지 모두 퍼진다.
////그러므로 자식2에서 문제 발생 -> 자식1도 취소, 부모도 취소
//suspend fun doSomething() = coroutineScope { //부모 코루틴
//    val value1 = async { //자식1 코루틴
//        getRandom1()
//    }
//
//    val value2 = async {  //자식2 코루틴
//        getRandom2()
//    }
//
//    try {
//        println("${value1.await()} + ${value2.await()} = ${value1.await() + value2.await()}")
//    } finally {
//        println("doSomething is cancelled")
//    }
//}
//
//fun main() = runBlocking {
//    try {
//        doSomething()
//    } catch (e: IllegalStateException) { //Exception 내용은 부모에게까지 올라오므로 위에서도 처리할 수 있다.
//        e.printStackTrace()
//    }
//}

