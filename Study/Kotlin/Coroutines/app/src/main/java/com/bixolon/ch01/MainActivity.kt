package com.bixolon.ch01

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import kotlinx.coroutines.*
import kotlinx.coroutines.channels.actor
import kotlinx.coroutines.sync.Mutex
import kotlinx.coroutines.sync.withLock
import java.util.concurrent.atomic.AtomicInteger
import kotlin.system.*
import kotlin.random.Random

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


//Dispatcher 의 종류
//import kotlinx.coroutines.*
//
//fun main() = runBlocking<Unit> {
//    launch { //runBlocking이 main에서 수행됐기 때문
//        println("부모의 콘텍스트 / ${Thread.currentThread().name}")
//    }
//
//    launch(Dispatchers.Default) { //Default 디스패처에서 수행
//        println("Default / ${Thread.currentThread().name}")
//    }
//
//    launch(Dispatchers.IO) { //Default 디스패처에서 수행
//        println("IO / ${Thread.currentThread().name}")
//    }
//
//    launch(Dispatchers.Unconfined) { //Main 스레드에서 수행
//        println("Unconfined / ${Thread.currentThread().name}")
//    }
//
//    launch(newSingleThreadContext("Fast Campus")) { //항상 새로운 스레드를 만들어 수행 파라미터로 스레드 이름을 지정
//        println("newSingleThreadContext / ${Thread.currentThread().name}")
//    }
//}

//각 디스패처의 종류마다 스레드 관련 정책이 다르다.

//Default는 코어 수에 비례하는 스레드 풀에서 수행합니다.
//IO는 코어 수 보다 훨씬 많은 스레드를 가지는 스레드 풀입니다. IO 작업은 CPU를 덜 소모하기 때문입니다.

//네트워크 사용 등.
//Unconfined는 어디에도 속하지 않습니다. 지금 시점에는 부모의 스레드에서 수행될 것입니다.
//앞으로 어떤 종류의 디스패처에서 수행될 지 알 수 없다.
//일반적으로 많이 사용하지 않는다.

//newSingleThreadContext는 항상 새로운 스레드를 만듭니다.

//fun main() = runBlocking<Unit> {
//    async {
//        println("부모의 콘텍스트 / ${Thread.currentThread().name}")
//    }
//
//    async(Dispatchers.Default) {
//        println("Default / ${Thread.currentThread().name}")
//    }
//
//    async(Dispatchers.IO) {
//        println("IO / ${Thread.currentThread().name}")
//    }
//
//    //하나 이상의 suspension point 가 있는 경우 잠에서 깨어난 후 어디서 수행될지 모른다
//    //가능하면 명시된 디스패처를 사용하는 것이 좋다.
//    async(Dispatchers.Unconfined) {
//        println("Unconfined / ${Thread.currentThread().name}")
//        delay(500L)
//        println("Unconfined / ${Thread.currentThread().name}")
//        delay(500L)
//        println("Unconfined / ${Thread.currentThread().name}")
//        println("")
//    }
//
//    async(newSingleThreadContext("Fast Campus")) {
//        println("newSingleThreadContext / ${Thread.currentThread().name}")
//    }
//}

//fun main() = runBlocking<Unit> { //증부모
//    val job = launch { //부모
//        launch(Job()) { //자식 이지만, launch 실행 시, job을 새로 만든다면 구조적인 부모 자식 관계가 성립되지 않는다. (가족관계증명이 끊어짐 ㅋㅋㅋ)
//            println(coroutineContext[Job])
//            println("launch1: ${Thread.currentThread().name}")
//            delay(1000L)
//            println("3!")
//        }
//
//        launch { //자식
//            println(coroutineContext[Job])
//            println("launch2: ${Thread.currentThread().name}")
//            delay(1000L)
//            println("1!")
//        }
//    }
//
//    delay(500L)
//    job.cancelAndJoin()
//    delay(1000L)
//    //500ms 쉬고 job을 취소하면 1000 ms delay가 있는 각 launch는 수행결과에서 차이를 보인다.
//    //첫 번째는 Job을 새로 생성했으므로 1초 뒤 3이 출력이 되고 두 번째의 경우 1을 출력하지 않는다. (부모가 취소됐으므로)
//    //마지막 줄 delay(1000L)을 삭제하면 첫 번째 3도 출력이 되지 않는다. 왜냐하면 해당 코루틴은 더이상 자식이 아니므로 부모가 기다려주지 않아 프로그램이 종료된다.
//}
//
//fun main() = runBlocking<Unit> {
//    val elapsed = measureTimeMillis {
//        val job = launch { // 부모
//            launch() { // 자식 1
//                println("launch1: ${Thread.currentThread().name}")
//                delay(5000L)
//            }
//
//            launch { // 자식 2
//                println("launch2: ${Thread.currentThread().name}")
//                delay(10L)
//            }
//        }
//        job.join()
//    }
//    println(elapsed)
//}

//@OptIn(ExperimentalStdlibApi::class)
//fun main() = runBlocking<Unit> {
//    launch { // A (부모 컨텍스트)
//        launch(Dispatchers.IO + CoroutineName("launch1")) { //A + Dispatchers.IO(B) + CoroutineName("launch1")(C) A + B + C 로 코루틴 엘리먼트 결합이 됨
//            println("launch1: ${Thread.currentThread().name}")
//            println(coroutineContext[CoroutineDispatcher])
//            println(coroutineContext[CoroutineName])
//            delay(5000L)
//        }
//
//        launch(Dispatchers.Default + CoroutineName("launch1")) {
//            println("launch2: ${Thread.currentThread().name}")
//            println(coroutineContext[CoroutineDispatcher])
//            println(coroutineContext[CoroutineName])
//            delay(10L)
//        }
//    }
//}

//
//suspend fun printRandom() {
//    delay(500L)
//    println(Random.nextInt(0, 500))
//}
//
//fun main() {
//    val job = GlobalScope.launch(Dispatchers.IO) {
//        launch { printRandom() }
//    }
//    Thread.sleep(1000L)
//}

//
//Thread.sleep(1000L)를 쓴 까닭은 main이 runBlocking이 아니기 때문입니다. delay 메서드를 수행할 수 없습니다.
//GlobalScope는 어떤 계층에도 속하지 않고 영원히 동작하게 된다는 문제점이 있습니다. 프로그래밍에서 전역 객체를 잘 사용하지 않는 것 처럼 GlobalScope도 잘 사용하지 않습니다.

//suspend fun printRandom() {
//    delay(500L)
//    println(Random.nextInt(0, 500))
//}
//
//fun main() {
//    val scope = CoroutineScope(Dispatchers.Default + CoroutineName("MainScope"))
//    val job = scope.launch(Dispatchers.IO) {
//        println(coroutineContext[CoroutineName])
//        launch { printRandom() }
//    }
//    Thread.sleep(1000L)
//}

//
//하나의 코루틴 엘리먼트, 디스패처 Dispatchers.Default만 넣어도 코루틴 컨텍스트가 만들어지기 때문에 이렇게 사용할 수 있습니다.
//이제부터 scope 계층적으로 형성된 코루틴을 관리할 수 있습니다. 우리의 필요에 따라 코루틴 스코프를 관리할 수 있습니다.

//suspend fun printRandom1() {
//    delay(1000L)
//    println(Random.nextInt(0, 500))
//}
//
//suspend fun printRandom2() {
//    delay(500L)
//    throw ArithmeticException()
//}
//
//val ceh = CoroutineExceptionHandler { coroutineContext, exception ->
//    println("Something happend: $exception")
//}
//
//fun main() = runBlocking<Unit> {
//    val scope = CoroutineScope(Dispatchers.IO + CoroutineName("FastCampus"))
//    val job = scope.launch (ceh) {
//        launch { printRandom1() }
//        launch { printRandom2() }
//    }
//    job.join()
//}

//CoroutineExceptionHandler에 등록하는 람다에서 첫 인자는 CoroutineContext 두 번째 인자는 Exception입니다. 대부분의 경우에는 Exception만 사용하고 나머지는 _로 남겨둡니다.

//suspend fun getRandom1(): Int {
//    delay(1000L)
//    return Random.nextInt(0, 500)
//}
//
//suspend fun getRandom2(): Int {
//    delay(500L)
//    throw ArithmeticException()
//}
//
//val ceh = CoroutineExceptionHandler { _, exception ->
//    println("Something happend: $exception")
//}
//
//fun main() = runBlocking<Unit> {
//    val job = launch (ceh) {
//        val a = async { getRandom1() }
//        val b = async { getRandom2() }
//        println(a.await())
//        println(b.await())
//    }
//    job.join()
//}
//수행 결과, Something happend 문구가 출력되지 않는 것을 알 수 있음.
//Exception handling이 안 됨



//suspend fun printRandom1() {
//    delay(1000L)
//    println(Random.nextInt(0, 500))
//}
//
//suspend fun printRandom2() {
//    delay(500L)
//    throw ArithmeticException()
//}
//
//val ceh = CoroutineExceptionHandler { _, exception ->
//    println("Something happend: $exception")
//}
//
//fun main() = runBlocking<Unit> {
//    val scope = CoroutineScope(Dispatchers.IO + SupervisorJob() + ceh)
//    val job1 = scope.launch { printRandom1() }
//    val job2 = scope.launch { printRandom2() }
//    joinAll(job1, job2)
//}
//일반적으로 코루틴은 문제가 발생해 취소가 되면, 부모와 자식 모두 취소된다.
//하지만 위의 SupervisorJob은 아래(자식)쪽으로만 취소된다.
//printRandom2가 실패했지만 printRandom1은 제대로 수행된다.
//joinAll은 북수개의 Job에 대해 join를 수행하여 완전히 종료될 때까지 기다린다.

//suspend fun printRandom1() {
//    delay(1000L)
//    println(Random.nextInt(0, 500))
//}
//
//suspend fun printRandom2() {
//    delay(500L)
//    throw ArithmeticException()
//}
//
//suspend fun supervisoredFunc() = supervisorScope {
//    launch { printRandom1() }
//    //문제가 발생한 곳에 무조건 핸들러가 있어야 한다.
//    //핸들러가 없는 경우, 일반적인 스코프와 같이 자식과 부모에게 까지 전파된다.
//    launch(ceh) { printRandom2() }
//}
//
//val ceh = CoroutineExceptionHandler { _, exception ->
//    println("Something happend: $exception")
//}
//
//fun main() = runBlocking<Unit> {
//    val scope = CoroutineScope(Dispatchers.IO)
//    val job = scope.launch {
//        supervisoredFunc()
//    }
//    job.join()
//}
//슈퍼바이저 스코프를 사용할 때 주의점은 무조건 자식 수준에서 예외를 핸들링 해야한다는 것입니다. 자식의 실패가 부모에게 전달되지 않기 때문에 자식 수준에서 예외를 처리해야합니다.

//suspend ramda를 받음
//suspend fun massiveRun(action: suspend () -> Unit) {
//    val n = 100 // 시작할 코루틴의 갯수
//    val k = 1000 // 코루틴 내에서 반복할 횟수
//    val elapsed = measureTimeMillis {
//        coroutineScope { // scope for coroutines
//            repeat(n) {
//                launch {
//                    //여러 스레드에서 돌아가면서 action이라는 동작을 수행할 것임.
//                    //람다 함수로 counter를 ++ 하기 때문에 동작 결과가 10만이 나와야할 것 같지만 그렇지 않을 수 있다.
//                    //여러 스레드가 서로 다른 정보를 가지고 있기 때문이다. (동시성 문제)
//                    repeat(k) { action() }
//                }
//            }
//        }
//    }
//    println("$elapsed ms동안 ${n * k}개의 액션을 수행했습니다.")
//}
//
//var counter = 0
//
//fun main() = runBlocking {
//    //withContext가 들어가면 runBlocking이 잠든다.
//    //runBlocking과 비슷해보이지만 runBlocking은 스레드를 잡고있고
//    //withContext는 부모 코루틴을 잠시 잠들게 할뿐이다.
//    withContext(Dispatchers.Default) {
//        massiveRun {
//            counter++
//        }
//    }
//    println("Counter = $counter")
//}

//withContext는 수행이 완료될 때 까지 기다리는 코루틴 빌더입니다. 뒤의 println("Counter = $counter") 부분은 잠이 들었다 withContext 블록의 코드가 모두 수행되면 깨어나 호출됩니다.
//위의 코드는 불행히도 항상 10000이 되는 것은 아닙니다. Dispatchers.Default에 의해 코루틴이 어떻게 할당되냐에 따라 값이 달라집니다.

//suspend fun massiveRun(action: suspend () -> Unit) {
//    val n = 100 // 시작할 코루틴의 갯수
//    val k = 1000 // 코루틴 내에서 반복할 횟수
//    val elapsed = measureTimeMillis {
//        coroutineScope { // scope for coroutines
//            repeat(n) {
//                launch {
//                    repeat(k) { action() }
//                }
//            }
//        }
//    }
//    println("$elapsed ms동안 ${n * k}개의 액션을 수행했습니다.")
//}
//
////어떤 스레드에서 변경하더라도 다른 스레드에게 영향이 간다.
////하지만 수행 결과 10만이 나오지 않는 것을 알 수 있다.
//@Volatile // 코틀린에서 어노테이션입니다.
//var counter = 0
//
//fun main() = runBlocking {
//    //Default 디스패처 >> 여러 개의 스레드에서 호출될 수 있음.
//    withContext(Dispatchers.Default) {
//        massiveRun {
//            counter++
//        }
//    }
//    println("Counter = $counter")
//}
////volatile은 가시성 문제만을 해결할 뿐 여러 스레드가 동시에 읽고 수정해서 생기는 문제를 해결하지 못합니다.
//가시성 문제라는 것은 한 쪽에서 수정했을 때 그 값을 정확하게 볼 수만 있을 뿐이기 때문이다.
//현재 값을 정확하게 보더라도 다른 스레드에서 올리는 와중에 다른 스레드에서 그 값을 보고 증가시켰다면, 2가 증가하는게 아니라 1이 증가됨




//suspend fun massiveRun(action: suspend () -> Unit) {
//    val n = 100 // 시작할 코루틴의 갯수
//    val k = 1000 // 코루틴 내에서 반복할 횟수
//    val elapsed = measureTimeMillis {
//        coroutineScope { // scope for coroutines
//            repeat(n) {
//                launch {
//                    repeat(k) { action() }
//                }
//            }
//        }
//    }
//    println("$elapsed ms동안 ${n * k}개의 액션을 수행했습니다.")
//}
//
////값을 증가시키는 도중에 다른 스레드에서 접근하지 못하도록 함.
//val counter = AtomicInteger()
//
//fun main() = runBlocking {
//    withContext(Dispatchers.Default) {
//        massiveRun {
//            counter.incrementAndGet() //(++)임
//        }
//    }
//    println("Counter = $counter")
//}
////AtomicInteger가 이 문제에는 적합한데 모든 상황에서 정답은 아닙니다.




//
//suspend fun massiveRun(action: suspend () -> Unit) {
//    val n = 100 // 시작할 코루틴의 갯수
//    val k = 1000 // 코루틴 내에서 반복할 횟수
//    val elapsed = measureTimeMillis {
//        coroutineScope { // scope for coroutines
//            repeat(n) {
//                launch {
//                    repeat(k) { action() }
//                }
//            }
//        }
//    }
//    println("$elapsed ms동안 ${n * k}개의 액션을 수행했습니다.")
//}
//
//var counter = 0
//val counterContext = newSingleThreadContext("CounterContext") //특정 스레드를 만들어 해당 스레드만 사용하도록 함 >> 항상 같은 스레드에서만 수행되는 것이 보장됨.
//
//fun main() = runBlocking {
//    //withContext의 위치를 변경하여 어떤 코드 부분을 하나의 스레드에서만 동작하도록 변경할 수 있다. (스레드 한정의 범위를 변경할 수 있음)
////    withContext(Dispatchers.Default) {
////        massiveRun {
////            withContext(counterContext) {
////                counter++
////            }
////        }
////    }
//    withContext(counterContext) {
//        massiveRun {
//            counter++
//        }
//    }
//    println("Counter = $counter")
//}



//suspend fun massiveRun(action: suspend () -> Unit) {
//    val n = 100 // 시작할 코루틴의 갯수
//    val k = 1000 // 코루틴 내에서 반복할 횟수
//    val elapsed = measureTimeMillis {
//        coroutineScope { // scope for coroutines
//            repeat(n) {
//                launch {
//                    repeat(k) { action() }
//                }
//            }
//        }
//    }
//    println("$elapsed ms동안 ${n * k}개의 액션을 수행했습니다.")
//}
//
//val mutex = Mutex()
//var counter = 0
//
//fun main() = runBlocking {
//    withContext(Dispatchers.Default) {
//        massiveRun {
//            mutex.withLock {
//                counter++ //여러 스레드 중 하나의 스레드만 진입이 가능하다. 동시 접근 안됨
//            }
//        }
//    }
//    println("Counter = $counter")
//}





//액터는 1973년에 칼 휴이트가 만든 개념으로 액터가 독점적으로 자료를 가지며 그 자료를 다른 코루틴과 공유하지 않고 액터를 통해서만 접근하게 만듭니다.
//먼저 실드 클래스를 만들어서 시작합시다.

//sealed class CounterMsg
//object IncCounter : CounterMsg() //값 증가
//class GetCounter(val response: CompletableDeferred<Int>) : CounterMsg() //현재 값 가져오기

//CounerMsg()와 GetCounter를 오버라이딩하는 것 외에 확장할 수 없음.
// 1. 실드(sealed) 클래스는 외부에서 확장이 불가능한 클래스이다. CounterMsg는 IncCounter와 GetCounter 두 종류로 한정됩니다.
// 2. IncCounter는 싱글톤으로 인스턴스를 만들 수 없습니다. 액터에게 값을 증가시키기 위한 신호로 쓰입니다.
// 3. GetCounter는 값을 가져올 때 쓰며 CompletableDeferred<Int>를 이용해 값을 받아옵니다.

//fun CoroutineScope.counterActor() = actor<CounterMsg> {
//    var counter = 0 // 액터 안에 상태를 캡슐화해두고 다른 코루틴이 접근하지 못하게 합니다. counter의 수정은 오로지 자료형을 가지고 있는 액터만 가능
//
//    for (msg in channel) { // 외부에서 보내는 것은 채널을 통해서만 받을 수 있습니다.(일종의 파이프)(receive)
//        when (msg) {
//            is IncCounter -> counter++ // 증가시키는 신호. 받은 신호가 IncCounter인 경우
//            is GetCounter -> msg.response.complete(counter) // 현재 상태를 반환합니다. 현재 Counter의 값. 다른 곳에서 해당 액터의 GetCounter를 호출할 때 response를 paramter로 넣음. 해당 response에 complete 함수안에 값을 넣어 반환해줌.
//        }
//    }
//}

//채널은 송신 측에서 값을 send할 수 있고 수신 측에서 receive를 할 수 있는 도구입니다. 3부와 4부에서 채널에 대해 상세히 다루겠습니다.
//전체 코드는 다음과 같은 형태가 됩니다.

//suspend fun massiveRun(action: suspend () -> Unit) {
//    val n = 100
//    val k = 1000
//    val elapsed = measureTimeMillis {
//        coroutineScope {
//            repeat(n) {
//                launch {
//                    repeat(k) { action() }
//                }
//            }
//        }
//    }
//    println("$elapsed ms동안 ${n * k}개의 액션을 수행했습니다.")
//}
//
//sealed class CounterMsg
//object IncCounter : CounterMsg()
//class GetCounter(val response: CompletableDeferred<Int>) : CounterMsg()
//
//fun CoroutineScope.counterActor() = actor<CounterMsg> {
//    var counter = 0 //해당 actor외 아무도 수정할 수 없음. channel 을 통해 signal을 보내는 방법밖에 없음.
//    for (msg in channel) { //suspension point
//        when (msg) {
//            is IncCounter -> counter++
//            is GetCounter -> msg.response.complete(counter)
//        }
//    }
//}
//
//fun main() = runBlocking<Unit> {
//    val counter = counterActor()
//    withContext(Dispatchers.Default) {
//        massiveRun {
//            counter.send(IncCounter) //suspension point
//        }
//    }
//
//    val response = CompletableDeferred<Int>()
//    counter.send(GetCounter(response)) //suspension point
//    println("Counter = ${response.await()}") //suspension point IncCounter 또는 GetCounter 호출 후, 결과가 올 때까지 잠이 들었다가 결과가 오면 깨어나기때문에 Suspension Point 임.
//    counter.close()
//}
//자료를 관리하는 Actor라는 자료구조를 만들어 자료를 관리하게 만들고, 해당 Actor에게 신호를 보내어 동시성 문제를 해결하는 방법.