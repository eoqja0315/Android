package com.bixolon.ch03

import kotlinx.coroutines.*
import kotlinx.coroutines.channels.*
import kotlinx.coroutines.selects.select

//***3-1채널 기초***//
//채널은 일종의 파이프입니다. 송신측에서 채널에 send로 데이터를 전달하고 수신 측에서 채널을 통해 receive 받습니다.
//(trySend와 tryReceive도 있습니다. 특별한 경우 사용
//과거에는 null을 반환하는 offer와 poll가 있었습니다. 지금은 trySend tryReceive로 대체됨)
//fun main() = runBlocking<Unit> {
//    val channel = Channel<Int>()
//    launch {
//        for (x in 1..10) {
//            channel.send(x) // suspension point 송신할 때도 받는 사람이 없는 경우, 받을 때까지 기다렸다가 다음 데이터를 송신
//        }
//    }
//
//    repeat(10) {
//        println(channel.receive()) //suspension point 수신할 때도 데이터가 없는 경우 잠이들었다가 데이터가 생기면 수신
//    }
//    println("완료")
//}

//fun main() = runBlocking<Unit> {
//    val channel = Channel<Int>()
//
//    launch {
//        for (x in 1.. 10) {
//            channel.send(x) //데이터를 send 한 후, launch 블록 자체가 잠에 듦
//        }
//
////        repeat(10) {
////            println(channel.receive()) //데이터가 send 된 뒤, launch 블록이 잠에 들어 데이터를 수신할 수 없음.
////        }
//
////        println("Complete")
//    }
//
//    val job = launch {
//        repeat(10) { // 수신 블록을 별도로 처리
//            println(channel.receive())
//        }
//        println("완료")
//    }
//    job.join()
//}

//fun main() = runBlocking<Unit> {
//    val channel = Channel<Int>()
//
//    launch {
//        for (x in 1.. 10) {
//            channel.send(x)
//        }
//        channel.close() //만약 close를 호출하지 않는다면 아래 for문은 끝이 어딘지 알 수 없어 무한루프를 돌게 됨
//    }
//
//    for (x in channel) { //receive를 수행하지 않아도 for문으로 해결 가능.
//        println(x)
//    }
//}

//생산자(producer)와 소비자(consumer)는 굉장히 일반적인 패턴입니다. 채널을 이용해서 한 쪽에서 데이터를 만들고 다른 쪽에서 받는 것을 도와주는 확장 함수들이 있습니다.
//1. produce 코루틴을 만들고 채널을 제공합니다.
//2. consumeEach 채널에서 반복해서 데이터를 제공합니다.

//ProducerScope는 CoroutineScope 인터페이스와 SendChannel 인터페이스를 함께 상속받습니다. 그래서 코루틴 컨텍스트와 몇가지 채널 인터페이스를 같이 사용할 수 있는 특이한 스코프입니다.
//produce를 사용하면 ProducerScope를 상속받은 ProducerCoroutine 코루틴을 얻게 됩니다.

//참고:
//우리가 흔히 쓰는 runBlocking은 BlockingCoroutine을 쓰는데 이는 AbstractCoroutine를 상속받고 있어요.
//결국 코루틴 빌더는 코루틴을 만드는데 이들이 코루틴 스코프이기도 한거죠.
//AbstractCoroutine은 JobSupport, Job(인터페이스), Continuation(인터페이스), CoroutineScope(인터페이스)을 상속받고 있고요.
//Continuation은 다음에 무엇을 할지, Job은 제어를 위한 정보와 제어, CoroutineScope는 컨텍스트 제공의 역할을 합니다. JobSupport는 잡의 실무(?)를 한다고 봐야죠.

//Dispatcher, 코루틴 네임, ch 또한 코루틴 컨텍스트의 엘리먼트이다.

//fun main() = runBlocking<Unit> {
//
////    val oneToTen = produce { //ProducerScope = CoroutineScope + SendChannel
////        //this.send this.coroutineContext 가 가능
////        for (x in 1.. 10) {
////            channel.send(x)
////        }
////    }
////
////    oneToTen.consumeEach {
////        println(it)
////    }
//
////    val channel = Channel<Int>()
////
////    launch {
////        for(i in 1.. 10) {
////            channel.send(i)
////        }
////    }
////
////    launch {
////        println(channel.receive())
////    }
//
//    println("Complete")
//}

//***3-2 채널 파이프 라인***
//fun CoroutineScope.produceNumbers() = produce<Int> { //produce는 receiveChannel을 반환 , send 채널 + CoroutineScope를 가짐
//    var x = 1
//    while (true) {
//        send(x++) //produceStringNumbers 에 파라미터로 넘어가 반복 수 만큼 send
//    }
//}
//
//fun CoroutineScope.produceStringNumbers(numbers : ReceiveChannel<Int>) : ReceiveChannel<String> = produce {
//    for (i in numbers) {
//        println("++produceStringNumbers send $i")
//        send("${i}!") //produceNumbers 에서 send한 데이터를 numbers channel에서 받아 문자열로 변환 후, Channel에 전송
//    }
//}
//
//fun CoroutineScope.filterOdd(numbers : ReceiveChannel<Int>) : ReceiveChannel<String> = produce{
//    for (i in numbers) {
//        if(i % 2 == 1) {
//            println("++filterOdd send $i")
//            send("${i}!")
//        }
//    }
//}
//
//fun CoroutineScope.filter(numbers : ReceiveChannel<Int>, prime : Int) : ReceiveChannel<Int> = produce {
//    for (i in numbers) {
//        if (i % prime != 0) {
//            send(i)
//        }
//    }
//}
//
//fun CoroutineScope.numbersFrom(start : Int) = produce<Int> {
//    var x = start
//    while (true) {
//        send(x++)
//    }
//}

//fun main() = runBlocking<Unit> {
//      val numbers = produceNumbers() //number : 리시브 채널, receive 메서드 사용 가능. send는 X.
//    val stringNumbers = produceStringNumbers(numbers)

//    repeat(5) {  //for문은 사용 불가능. 이유는 produceNumbers 와 produceStringNumbers 모두 close가 없기 때문.
//        println(stringNumbers.receive())
//    }

//    val stringOdds = filterOdd(numbers)
//
//    repeat(10) { //receive가 10번 수행
//        println(stringOdds.receive())
//    }


    //누가 이렇게 할까 싶은 예제입니다. 원한다면 디스패처를 이용해 CPU 자원을 효율적으로 이용하는 것이 가능합니다.
    //Receive, 3부터 데이터를 가지고 있음, 루프를 돌 때마다 채널이 대체됨
//    var numbers = numbersFrom(2)
//
//    repeat(100) {
//        val prime = numbers.receive() // 2를 처음에 Receive
//        println(prime)
//        numbers = filter(numbers, prime) //numbers : 3,4,5 prime 2
//        //2의 배수를 필터하는 채널 생성
//        //3의 배수을 필터하는 채널 생성
//        //5의 배수를 필터하는 채널
//        //... 무수히 많은 채널을 생성
//    }

//    println("Complete")
//    coroutineContext.cancelChildren()
//}

//***3-3 채널 팬아웃, 팬인

//fun CoroutineScope.produceNumbers() = produce<Int> { //생산자 1개
//    var x = 1
//    while (true) {
//        send(x++)
//        delay(100L)
//    }
//}
//
//fun CoroutineScope.processNumber(id: Int, channel: ReceiveChannel<Int>) = launch {
//    channel.consumeEach { //채널 구독
//        println("${id}가 ${it}을 받았습니다.")
//    }
//}
//
//
//fun main() = runBlocking<Unit> {
//    val producer = produceNumbers()
//    repeat (5) { //5개의 코루틴 생성 >> 소비자 5개
//        processNumber(it, producer)
//    }
//    delay(1000L)
//    producer.cancel()
//}



//coroutineContext의 자식이 아닌 본인을 취소하면 어떻게 될까요?
//processNumber를 suspend 함수의 형태로 변형하면 어떻게 될까요?
//다른 방법으로 취소할 수 있을까요?
//suspend fun produceNumbers(channel: SendChannel<Int>, from: Int, interval: Long) {
//    var x = from
//    while (true) {
//        channel.send(x)
//        x += 2
//        delay(interval)
//    }
//}
//
//fun CoroutineScope.processNumber(channel: ReceiveChannel<Int>) = launch {
//    channel.consumeEach {
//        println("${it}을 받았습니다.")
//    }
//}
//
//fun main() = runBlocking<Unit> {
//    val channel = Channel<Int>() // channel = Receive + Send Channel
//    launch {
//        produceNumbers(channel, 1, 100L) // 1, 3, 5, 7, 9 1번 보낼때마다 100ms 같은 채널 사용
//    }
//    launch {
//        produceNumbers(channel, 2, 150L) // 2, 4, 6, 8 ,10 1번 보낼때마다 150ms 같은 채널 사용
//    } //생산자 2개
//    processNumber(channel) //소비자 1개
//    delay(1000L)
//    coroutineContext.cancelChildren()
//}



//suspend fun someone(channel: Channel<String>, name: String) {
//    for (comment in channel) {
//        println("${name}: ${comment}") // 민준 : 패스트 캠퍼스 // 서연 : 패스트 캠퍼스
//        channel.send(comment.drop(1) + comment.first()) // 스트 캠퍼스패 전달 >> 이 메시지를 받은 someone은 someone : "스트 캠퍼스패"로 출력 후, "트 캠퍼스패스"를 전달할 것이다.
//        delay(100L)
//    }
//}
//
//fun main() = runBlocking<Unit> {
//    val channel = Channel<String>()
//    launch {
//        someone(channel, "민준") // 민준 channel에 민준 send
//    }
//    launch {
//        someone(channel, "서연") // 서연
//    }
//    channel.send("패스트 캠퍼스") // 패스트 캠퍼스를 send
//    delay(1000L)
//    coroutineContext.cancelChildren()
//}
//채널이 민준과 서연이 번갈아가며 수행되는 것을 볼 수 있다.



//리턴 값 리시브 채널
//fun CoroutineScope.sayFast() = produce<String> {
//    // 코루틴 스코프 + 샌드채널
//    while (true) {
//        delay(100L)
//        send("패스트")
//    }
//}
//
//fun CoroutineScope.sayCampus() = produce<String> {
//    while (true) {
//        delay(150L)
//        send("캠퍼스")
//    }
//}
//
//fun main() = runBlocking<Unit> {
//    val fasts = sayFast()
//    val campuses = sayCampus()
//    repeat (5) {
//        //먼저 끝내는 애만 select 해서 듣겠다.
//        select<Unit> {
//            fasts.onReceive {
//                println("fast: $it")
//            }
//            campuses.onReceive {
//                println("campus: $it")
//            }
//        }
//    }
//    coroutineContext.cancelChildren()
//}

//채널에 대해 onReceive를 사용하는 것 이외에도 아래의 상황에서 사용할 수 있습니다.
//Job - onJoin
//Deferred - onAwait
//SendChannel - onSend
//ReceiveChannel - onReceive, onReceiveCatching
//delay - onTimeout


//***3-4 채널 버퍼링***

//fun main() = runBlocking<Unit> {
//    val channel = Channel<Int>(10) //buffer의 개수 10
//
//    launch {
//        for (x in 1..20) {
//            println("${x} sending")
//            channel.send(x) //받든 안 받든 10개까지는 채널에 계속 send
//        }
//        channel.close()
//    }
//
//    for( x in channel) {
//        println("${x} 수신")
//        delay(100L)
//    }
//
//    println("Complete")
//}

//fun main() = runBlocking<Unit> {
//    val channel = Channel<Int>(Channel.RENDEZVOUS) //사실 상 Buffer size가 0 위의 채널들은 예제 91을 제외하고는 전부 랑데뷰 채널
//
//    launch {
//        for (x in 1..20) {
//            println("${x} 전송중")
//            channel.send(x)
//        }
//        channel.close()
//    }
//
//    for (x in channel) {
//        println("${x} 수신")
//        delay(100L)
//    }
//    println("완료")
//}
//UNLIMITED - 무제한으로 설정 LinkedList 형태로 값을 뒤로 계속 이어 붙임.
//CONFLATED - 오래된 값이 지워짐. 처리하지 못한 값은 버려버리고 다음 값으로 대체 됨.
//BUFFERED - 64개의 버퍼. 오버플로우엔 suspend

//fun main() = runBlocking<Unit> {
//    val channel = Channel<Int>(2, BufferOverflow.DROP_OLDEST)
//    launch {
//        for (x in 1..50) {
//            channel.send(x)
//        }
//        channel.close()
//    }
//
//    delay(500L)
//
//    for (x in channel) {
//        println("${x} 수신")
//        delay(100L)
//    }
//    println("완료")
//}

//SUSPEND - 잠이 들었다 깨어납니다.
//DROP_OLDEST - 예전 데이터를 지웁니다. 위 CONFLATED와 비슷한 방법.
//DROP_LATEST - 새 데이터를 지웁니다. 처리하지 못한 새 데이터를 지우고 오래된 데이터를 유지함