package com.bixolon.ch02

import kotlin.random.Random
import kotlinx.coroutines.*
import kotlinx.coroutines.flow.*
import java.lang.IllegalStateException
import java.lang.RuntimeException
import kotlin.system.measureTimeMillis

fun log(msg : String) = println("[${Thread.currentThread().name}] $msg")

//***2-1*** 플로우 기초
//fun flowSomething(): Flow<Int> = flow {
//    repeat(10) {
//        emit(Random.nextInt(0, 500))
//        delay(10L)
//    }
//}
//
//fun main() = runBlocking {
//    //Collect 호출 동작 시작
//    flowSomething().collect { value ->
//        println(value)
//    }
//}

//flow 플로우 빌더 함수를 이용해 코드블록을 구성하고 emit을 호출해서 스트림에 데이터를 흘려 보낸다.
//플로우는 콜드 스트림이기 때문에 collect를 호출해야 값이 발생하기 시작.
// 콜드 스트림 - 요청이 있는 경우에 보통 1:1로 값을 전달하기 시작.
// 핫 스트림 - 0개 이상의 상대를 향해 지속적으로 값을 전달

//fun flowSomething(): Flow<Int> = flow {
//    repeat(10) {
//        emit(Random.nextInt(0, 500))
//        delay(100L)
//    }
//}
//
//fun main() = runBlocking<Unit> {
//    val result = withTimeoutOrNull(500L) {
//        flowSomething().collect { value ->
//            println(value)
//        }
//        true
//    } ?: false
//    if (!result) {
//        println("취소되었습니다.")
//    }

//    val result2 = withTimeout(500L) {
//        flowSomething().collect{ value ->
//            println(value)
//        }
//    }
    //TimeoutCancellationExeption 발생
//}



//fun main() = runBlocking<Unit> {
//    //flow 다섯 개 생성
//    //아래 코드와 동일
//    flowOf(1, 2, 3, 4, 5).collect { value ->
//        println(value)
//    }
//
////
////    flow {
////        emit(1)
////        emit(2)
////        emit(3)
////        emit(4)
////        emit(5)
////    }.collect { value ->
////        println(value)
////    }
//}



//
//fun main() = runBlocking<Unit> {
//    listOf(1, 2, 3, 4, 5).asFlow().collect { value ->
//        println(value)
//    }
//    (6..10).asFlow().collect {
//        println(it)
//    }
//
//    //flowOf(1,2,3,4,5,6, 7, 8, 9, 10).collect { println(it) }
////    flow {
////        emit(1)
////        emit(2)
////        emit(3)
////        emit(4)
////        emit(5)
////        emit(6)
////        emit(7)
////        emit(8)
////        emit(9)
////        emit(10)
////    }.collect { println(it) }
//}



//***2-2*** 플로우 연산자
//fun flowSomething() : Flow<Int> = flow {
//    repeat(10) {
//        emit(Random.nextInt(0, 500))
//        delay(10L)
//    }
//}
//
//fun main() = runBlocking{
//    flowSomething().map {
//        "$it $it"
//    }.collect { println(it) }
//}


//fun main() = runBlocking {
//    (1..20).asFlow().filter {
//        (it % 2) == 0 //필터 조건 >> 술어 또는 predicate라고도 부름
//    }.collect {
//        println(it)
//    }
//}
//



//fun main()  = runBlocking {
//    (1..20).asFlow().filterNot {
//        (it % 2) == 0
//    }.collect {
//        println(it)
//    }
//
//    (1..20).asFlow().filterNot {
//        (it % 2) == 0
//    }.map {
//        it * 3
//    }.collect {
//        println(it)
//    }
//}

//
//suspend fun someCalc(i : Int) : Int {
//    delay(100L)
//    return i * 2
//}
//
//fun main() = runBlocking {
//    (1..20).asFlow().transform {
//        emit(it) //1, 방출
//        emit(someCalc(it)) // 2
//    }.collect { //1 2 2 4 3 6 4 8 5 10
//        println (it)
//    }
//}


//suspend fun someCalc(i : Int) : Int {
//    delay(10L)
//    return i * 2
//}
//
//fun main() = runBlocking {
//
////    (1..20).asFlow().transform {
////        emit(it)
////        emit(someCalc(it))
////    }.take(5) // 값을 5개만 가져 옴
////        .collect {
////            println(it)
////        }
//
//
//    (1..20).asFlow().transform {
//        emit(it)
//        emit(someCalc(it))
//    }.takeWhile { emitValue ->
//        emitValue >= 15 //첫 번째 값이 15미만 이기 때문에 아무것도 출력하지 않고 종료됨.
//    }.collect { collectValue ->
//        println(collectValue)
//    }
//
//}

//suspend fun someCalc(i : Int) : Int {
//    delay(10L)
//    return i * 2
//}
//
//fun main() = runBlocking {
//
////    (1..20).asFlow().transform {
////        emit(it)
////        emit(someCalc(it))
////    }.drop(5) //처음 다섯 개를 버림
////        .collect {
////            println(it)
////        }
//
//
////    (1..20).asFlow().transform {
////        emit(it)
////        emit(someCalc(it))
////    }.dropWhile {
////        it < 15 //9부터는 출력됨. 조건에 맞는 부분까지 버리다가 조건에 안 맞는 부분을 만나면 dropWhile이 종료되는 듯(takeWhile처럼)
////    }
////        .collect {
////            println(it)
////        }
//
//}



//reduce는 흔히 map과 reduce로 함께 소개되는 함수형 언어의 오래된 메커니즘입니다. 첫번째 값을 결과에 넣은 후 각 값을 가져와 누진적으로 계산합니다.
//suspend fun someCalc(i : Int) : Int {
//    delay(10L)
//    return i * 2
//}
//
//fun main() = runBlocking {
//    val value = (1..10)
//        .asFlow() //1 ~ 10까지 스트림에 플려보냄
//        .reduce {a, b -> //첫 번째로 reduce에 a = 1 b = 2가 들어감
//            a + b
//        }
//    //첫 번째로 reduce에 a = 1 b = 2가 들어감
//    //결과적으로 3이 reduce에 결과값이 됨.
//
//    //두 번째로 a = 3이되고 그 다음 flow 값인 b = 3 이되고 계산 결과가 6이되겠죠?
//    //3. a = 6, b = 4
//    //4. a = 10 b = 5
//    //5. a = 15 b = 6
//    //...
//    //9. a = 45 b = 10
//    println(value) //55 출력
//
//    val value2 = (1..10)
//        .asFlow()
//        .fold(10) {a, b -> //초기값이 존재하는 reduce임. 위 reduce a = 1 b = 2에서 시작했다면 fold에서는 a = 10 b = 1 부터 시작. 결과적으로 55 + 10이 출력됨
//            a + b
//        }
//
//    println(value2)
//
//    //counter
//    val counter = (1..10)
//        .asFlow()
//        .count { //종단 연산자, terminal operator. 종단 연산자를 만나자마자 특정 값, 컬렉션 등의 결과를 return 함. collect가 없어도 값을 return
//            (it % 2) == 0
//        } //조건에 맞는 값의 개수를 셈
//
//    println(counter)
//
//    //fliter인 경우는, 값 자체를 남기면서 collect를 호출하여 값을 emit 해야함
//    //fileter 같은 경우를 중간 연산자라고 한다. 중간 연산자의 경우 다른 중간 연산자들과 함께 사용된 후, 마지막에 collect를 호출하여 값을 확인해야 한다.
//    //Collect 또한 종단 연산자라고 볼 수 있다.
//}



//***2-3*** 플로우 컨텍스트
//fun simple() : Flow<Int> = flow {
//    log("flow 시작")
//    for (i in 1..10) {
//        emit(i)
//    }
//}
//

//fun simple2() : Flow<Int> = flow {
//    withContext(Dispatchers.Default) {
//        for(i in 1..10) {
//            delay(100L)
//            emit(i)
//        }
//    }
//}

//fun simple3() : Flow<Int> = flow<Int> {
//    for(i in 1..10) {
//        delay(100L)
//        log("값 ${i}를 emit합니다.")
//        emit(i)
//    }
//    //flowOn 위 쪽은 업스트림으로 부름
//}.flowOn(Dispatchers.Default) //위치 >> 업스트림만 Dispatchers.Default에서 호출됨
//    .map { // 다운 스트림
//        it * 2 //IO 디스패처
//    }
//    .flowOn(Dispatchers.IO) //map을 IO 디스패처에서 실행
//
////**실수로 flowOn이 연달아 나오는 경우 가장 위의 것만 인정됨
//
//fun main() = runBlocking<Unit> {
//    //모든 코루틴이 디스패처 io에 영향 받음.
////    launch(Dispatchers.IO) {
////        simple()
////            .collect { log("${it}를 받음.") }
////    }
//
////    launch(Dispatchers.IO) {
////        //에러 발생 >> 플로우 내에서는 컨텍스트를 바꿀 수 없다. simple2를 호출한 메인 함수 launch에서 Dispatchers.IO context에서 호출하고 있기 때문에 Default로 바꿀 수 없어 에러남
////        simple2()
////            .collect { log("${it}를 받음.") }
////    }
//
//    simple3().collect { value ->
//        log("${value}를 받음.")
//    }
//}


//***2-4*** 플로우 버퍼링
//fun simple(): Flow<Int> = flow {
//    for (i in 1..3) {
//        delay(100) //데이터 생성에 100ms 지연
//        emit(i)
//    }
//}
//
//fun main() = runBlocking<Unit> {
//    val time = measureTimeMillis {
//        simple().collect { value ->
//            delay(300) //데이터 소비에 300ms 지연, 총 한 번 데이터의 소비에 400ms 가 소비되는 것을 알 수 있음.
//            println(value)
//        }
//    }
//    println("Collected in $time ms")
//}


//fun simple(): Flow<Int> = flow {
//    for (i in 1..3) {
//        delay(100)
//        log("${i}를 방출합니다.")
//        emit(i)
//    }
//}
//
//fun main() = runBlocking<Unit> {
//    val time = measureTimeMillis {
//        //.buffer()를 붙힘으로써 collector의 준비와 상관없이(delay 300을 하지 않고) 보내는 쪽이 받는 쪽을 고려하지 않고 데이터를 바로바로 방출하도록 함.
//        //simple 함수 내에서는 100ms 씩 기다리면서 1 2 3 을 차례로 방출하여 buffer에 담아두고
//        //받는 쪽은 300ms씩 기다리면서 buffer에 있는 데이터를 하나씩 가져옴
//        simple().buffer()
//            .collect { value ->
//                delay(300)
//                log("${value}를 받았습니다.")
//                println(value)
//            }
//    }
//    println("Collected in $time ms")
//}

//fun simple() : Flow<Int> = flow {
//    for (i in 1 .. 10) {
//        delay(100)
//        log("${i}를 방출합니다.")
//        emit(i)
//    }
//}
//
//fun main() = runBlocking {
//    val time = measureTimeMillis {
//        simple().conflate() //받는 쪽에서 받지 못하는 중간 데이터 값을 삭제시킴 1 ,2, 3 이면 2가 사라짐
//            .collect { value ->
//                delay(500)
//                log("${value}를 받았습니다.")
//                println(value)
//            }
//    }
//
//    //위와 같은 경우에는 보내는 쪽 1방출 후 받는 쪽에서 1을 받고 500ms 기다림(실제적으로는 받은 데이터 1을 처리하는 동안) 2,3,4,5를 마저 방출한다.
//    //받는 쪽은 처리한 1을 출력 후, 5을 받아들고 500ms 다시 데이터 처리(500ms 잠듬). 중간 값들을 없애므로 2,3,4 는 삭제된다.
//
//    //다시 받는 쪽이 500ms 데이터 처리하는 동안 보내는 쪽은 6,7,8,9,10을 방출한다.
//    //받는 쪽은 받았던 5를 출력하고 10을 500ms동안 데이터 처리 후 10을 출력한다.
//    println("Collected in $time ms")
//}


//fun simple(): Flow<Int> = flow {
//    for (i in 1..3) {
//        delay(100)
//        emit(i)
//    }
//}
//
//fun main() = runBlocking<Unit> {
//    val time = measureTimeMillis {
//        //첫 번째 값을 받고 처리하는 동안 두 번째 값이 들어옴. >>> 리셋
//        //두 번째 값을 받고 처리하는 동안 세 번째 값이 들어옴. >>> 리셋
//        //3만 출력됨
//        simple().collectLatest { value ->
//            println("값 ${value}를 처리하기 시작합니다.")
//            delay(300)
//            println(value)
//            println("처리를 완료하였습니다.")
//        }
//    }
//    println("Collected in $time ms")
//}

//***2-5*** 플로우 결합하기

//fun main() = runBlocking<Unit> {
//    val nums = (1..3).asFlow()
//    val strs = flowOf("일", "이", "삼")
//
//    //zip을 통해 플로우를 묶음
//    //num에 strs를 zip했으므로 a = 1 b = "일" 이 됨. 순서대로 가져옴
//    nums.zip(strs) { a, b -> "${a}은(는) $b" }
//        .collect { println(it) }
//}

//zip의 경우 두 데이터가 모두 준비가 된 후에 동작한다.

//combine은 한 쪽만 준비되면 동작을 시작한다.
//fun main() = runBlocking<Unit> {
//    val nums = (1..3).asFlow().onEach { delay(100L) }
//    val strs = flowOf("일", "이", "삼").onEach { delay(200L) }
//
//    //항상 최신의 데이터를 가지고 있는다.
//    //1, 일
//    //2, 일
//    //3, 일
//    //3, 이
//    //3, 삼
//    nums.combine(strs) { a, b -> "${a}은(는) $b" }
//        .collect { println(it) }
//}

//항상 짝을 이루어야하는 경우 >> zip을 사용
//최신의 데이터가 중요한 경우, 하나의 UI 안에 여러 데이터를 가지고 있을 텐데, 하나의 데이터가 업데이트 될 때 다른 최신의 여러 데이터를 전체적으로 업데이트해야 하는 경우는 combine이 맞을 수 있음.


//***2-6*** 플로우 플래트닝하기

//fun requestFlow(i : Int) : Flow<String> = flow {
//    //값을 받아 두 개의 값을 방출
//    emit("$i : First") // 1: First, 2: First
//    delay(500) // 500ms 뒤에 , 500ms 뒤에
//    emit("$i : Second") // 1: Second, 2: Second
//}
//
//fun main() = runBlocking {
//    val startTime = System.currentTimeMillis()
//
////    (1..3).asFlow().onEach { delay(100) } //a number everay 100ms delay //1,2,3
////        .flatMapConcat { //requestFlow(1) .. requestFlow(2) .. requestFlow(3) 를 순서대로 이어 붙이겠다.
////            requestFlow(it)
////        }
////        .collect { //collect and print
////            println("$it at ${System.currentTimeMillis() - startTime} ms from Start")
////        }
////
////    (1..3).asFlow().onEach { delay(100) }
////    .flatMapMerge {
////        //flatMapConcat의 경우 requestFlow(1) 이 종료될 때까지 기다렸는데 flatMapMerge는 기다리지 않는다.
////        //출력 시, First 들이 먼저 출력되는 것을 알 수 있다.
////        requestFlow(it)
////    }
////    .collect { //collect and print
////        println("$it at ${System.currentTimeMillis() - startTime} ms from Start")
////    }
//
////    (1..3).asFlow().onEach { delay(100) }
////        .flatMapLatest {
////            //flatMapLatest의 경우도 마찬가지로 requestFlow(1) 이 종료될 때까지 가다리지 않는다.
////            //다만 flatMapMerge와의 차이 점은. requestFlow(2)가 호출 될 때 requestFlow(1)을 캔슬시켜버린다.
////            // 100ms -> requestFlow(1) -> 100ms -> requestFlow(1) cancel requestFlow(2) call -> 100ms -> requestFlow(2) cancel requestFlow(3) call
////            requestFlow(it)
////        }
////        .collect { //collect and print
////            println("$it at ${System.currentTimeMillis() - startTime} ms from Start")
////        }
//}

//***2-7 플로우 예외처리하기***

//fun simple(): Flow<Int> = flow {
//    for (i in 1..3) {
//        println("Emitting $i")
//        emit(i) // emit next value
//    }
//}
//
//fun main() = runBlocking<Unit> {
//    try {
//        simple().collect { value ->
//            println(value)
//            check(value <= 1) { "Collected $value" } //check(2)에서 예외 발생가 발생하면서 Collected 2 가 출력됨
//        }
//    } catch (e: Throwable) {
//        println("Caught $e")
//    }
//}

//fun simple(): Flow<String> = flow {
//        for (i in 1..3) {
//            println("Emitting $i")
//            emit(i) // emit next value
//        }
//    }
//        .map { value ->
//            check(value <= 1) { "Crashed on $value" }
//            "string $value"
//        }
//
//fun main() = runBlocking<Unit> {
//    try {
//        simple().collect { value -> println(value) }
//    } catch (e: Throwable) {
//        println("Caught $e")
//    }
//}

//빌더 코드 블록 내에서 예외를 처리하는 것은 예외 투명성을 어기는 것입니다. 플로우 밖에서는 알 수 없기 때문에, 플로우에서는 catch 연산자를 이용하는 것을 권합니다.
//catch 블록에서 예외를 새로운 데이터로 만들어 emit을 하거나, 다시 예외를 던지거나, 로그를 남길 수 있습니다.

//fun simple(): Flow<String> =
//    flow {
//        for (i in 1..3) {
//            println("Emitting $i")
//            emit(i) // emit next value
//        }
//    }
//        .map {
//            check(it <= 1) { "Crashed on $it" }
//            "string $it"
//        }
//
//fun main() = runBlocking<Unit> {
//    simple()
//        .catch { e -> emit("Caught $e") } // emit on exception e는 에러 내용 이겠죠? 위 함수에서 발생한 check 블록 내 메시지
//        .collect { println(it) }
//}

//flow 밖에서 Exception을 받아 새로운 데이터를 다시 만들어 Exception을 처리하는 경우이다.
//throw e 하여 Excpetion을 던져도 되고 로그를 남겨도 된다.

//fun simple(): Flow<Int> = flow {
//    for (i in 1..3) {
//        println("Emitting $i")
//        emit(i)
//    }
//}
//
//fun main() = runBlocking<Unit> {
//    simple()
//        .catch { e -> println("Caught $e") } // does not catch downstream exceptions
//        .collect { value ->
//            check(value <= 1) { "Collected $value" }
//            println(value) //collectd 부분은 catch를 기준으로 다운스트림이므로, collect 내에서 발생한 Exception은 처리하지 못한다.
//        }
//}

//많은 flow은 업스트림에만 영향을 미치고 다운스트림에는 영향을 미치지 않는다. 이를 catch 투명성이라고 한다.

//***2-8 플로우 완료처리하기

//fun simple() : Flow<Int> = flow {
//    for(i in 1.. 3) {
//        emit(i)
//    }
//}
//
//fun simple2() : Flow<Int> = flow {
//    emit(1)
//    throw RuntimeException()
//}
//
//fun main() = runBlocking {
//
////    try {
////        simple().collect { println(it) }
////    } finally {
////        println("Done") //예외가 발생하든 하지 않든 Done이 무조건 호출
////    }
//
//    //다음과 같이 finally를 연산자 호출로 처리할 수 있다.
////    simple()
////        .onCompletion { println("Done") }
////        .collect { println(it) }
//
////    simple() //1, 2, 3
////        .map {
////            if (it > 2) {
////                throw IllegalStateException()
////            }
////            it + 1
////        } //1, 2 방출 후 3에서 예외
////        .catch { e-> emit(-99)}
////        .onCompletion { println("Done") } //catch를 하지 않으면 Done이 출력되지 않을 수 있음. onCompletion이 수행되지 않을 수 있음.
////        .collect { println(it) }
//
//    //onCompletion 은 finally와 다르게 cause를 통해서 예외가 발생되었는지 알 수 있다.
//    simple2()
//        .onCompletion { cause -> //Exception 원인
//            if (cause != null) {
//                println("Flow completed exceptionally")
//            } else {
//                println("Flow completed.")
//            }
//        }
//        .catch { cause -> println("Caught exception") }
//        .collect { value -> println(value) }
//}

//***2-9 플로우 런칭***

//addEventListener 대신 플로우의 onEach를 사용할 수 있습니다. 이벤트마다 onEach가 대응하는 것입니다.

//ForExample touch, keyboard, mouse event 등 기존의 Callback 을 통해서 처리하고 있음.
//addEventListener { event ->
//    handle(event)
//}

//event가 Flow라면 onEach 문에서 handle 할 수 있다.
//events().onEach {
//    touch, keyboard 등등
//}
//하지만 이 문제는 collect()가 호출되지 않는 한 실행되지 않는 문제점이 있다.

fun events() : Flow<Int> = (1..3).asFlow().onEach { delay(100) }
fun logFun(msg : String) = println("${Thread.currentThread().name} : $msg")

fun main() = runBlocking { // this : 코루틴 스코프, 코루틴.

//    events()
//        .onEach { event -> println("Event : $event") }
//        .collect() //스트림이 끝날 때까지 기다린다. 실제로는 이벤트는 화면이 종료될 때까지 계속해서 발생할 것이다. 끝날 때까지 다음 순번에 기회를 주지 않는다.
//    println("Done")

    // 이벤트를 처리하는 동안 다음 동작들을 수행할 수 없게 된다.
    // 일반적으로 App의 이벤트는 화면이 유지되는 동안 계속해서 발생한다고 볼 수 없으므로 아래 동작들을 수행할 수 없게 된다.
    // UI 작업
    // 네트워크 호출


    // 위에 대한 해결책이 아래 코드이다.
    //launchIn을 이용하면 별도의 코루틴에서 플로우를 런칭할 수 있습니다.
    events()
        .onEach { event -> logFun("Event: $event") } //각기 다른 코루틴에서 호출됨을 알 수 있다.
        .launchIn(this) //첫 번째 파라미터로 코루틴 스코프를 전달한다. 새로운 코루틴을 생성하여 onEach를 수행.
    println("Done")

    //UI 작업
    //네트워크 호출 등을 수행하더라도 각 코루틴에서 수행되므로 이벤트 처리를 플로우로 할 수 있다.
}

