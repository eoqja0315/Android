package com.dbhong.myapplication

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.TextView
import java.util.*

class MainActivity : AppCompatActivity() {

    val textView: TextView = findViewById(R.id.textView)

    val intArr: IntArray = intArrayOf(1, 2, 3)
    val textString = "";
    val textString2: String = "textString2";
    val stringArray: Array<String> = arrayOf("123", "1234")

    val immutableList: List<String> = listOf("Lorem", "David")
    val mutableList: MutableList<String> = arrayListOf("David", "Lorem")

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        val a = 100;
        val b: Int? = 100
        val c = 100

        //b?.sum() //null 일경우에는 실행하지 않음
        //c.sum() // 애초에 null safe 함

        //apply함수
        val person = Person().apply {
            firstName = "Fast"
            lastName = "Campus0"
        } //초기화 + 계산

        //Also 함수 객체가 파라미터를 통해서 전달됨 람다의 입력값으로 넣어옴 그 입력값을 it 키워드로 접근

//        Random.nextInt(100).also {
//            print("getRandomInt() generated value $it")
//        }
//
//        Random.nextInt(100).also { value -> print("getRadomInt() generated value $value")}

        //let 함수
        val number: Int? = 10

        val sumNumberStr = number?.let { //also, apply >> 객체 자신이 반환. //let code블럭의 수행 결과가 반환됨
            "${sum(10, it)}" //number?.let 안으로 들어오는 변수는 널이 아닐 때만 들어오므로 null safe
        }

        val num: Int?
        num = 0
        val sumNum = num?.let {
            "${sum(10, it)}"
        }.orEmpty()

        //sum 10과 num 의 결과 값을 sumNum에 문자열로 넣음 num이 널이라 let 함수가 실행되지 않을경우 빈 값이 넣어짐

        //with 함수
        val person2 = Person()
        with(person2) {
            //person 안의 변수와 함수를 this를 생략하고 호출 가능
            work()
            sleep()
            println(age)
            //person  객체안의 work,  sleep, 함수를 호출하고  age를 출력하겠다
        }

        //Run 함수

//        val result = service.run {
//            port = 8080
//            query()
//        }
//      반환 값이 람다의 결과값 with 과 다르게 확장 함수로 사용이 가능. >> service.run << with은 person.with 으로 사용 XXX

        //Java 라면
        //service.port = 8080
        //Result result = service.query()

        var nullableNumber: Int? = null
        
        val lazyNumber : Int by lazy {
            100
        }
        
        // 사용하기 전까지는 lazyNumber 라는 변수에 100이 할당되지 않음.
        // lazyNumber.add() 실행을해야 100이할당됨
        // View 나중에 할당하는 경우가 있음. 뷰에 속성들을 lazyinit 으로 넣어놓고 변수를 사용하지 않을 경우 값이 할당되지 않아 뷰가 제대로 그려지지 않는 특징이 있다.

    data class JavaObject(val s: String) //데이터를 저장하는 목적 copy, toString, hasoCode  생성자 등이 자동으로 만들어짐
    //Kotlin은 getter setter 보다는 직접 접근하는 방식을 주로 사용. 다만 Java 클래스에서 Kotlin 객체를 쓸 수 있으므로 Getter Setter는 자동으로 생성해줌 
    //val 변수가 아닌 var 타입으로 생성해야 Getter Setter가 자동으로 생성됨

    //람다식은 함수에 함수를 전달하고 전달받은 함수를 전달한 함수에서 실행할 수 있도록 함

//    button.setOnClickListener(new View.OnClickListener()
//    {
//        @Override
//        public void onClick(View view)
//        {
//            ...
//        }
//    })

//    button.setOnClickListener { v -> } 인터페이스에 메서드가 하나만 있을 경우 사용가능 
    // 람다에서는 파라미터가 하나인 경우 생략이 가능하다 >> It으로도 접근 가능

   class Person() {
        lateinit var firstName : String //초기화 해주지 않으면 실행 과정에서 에러발생함 전역변수인 경우 사용 가능하다. 전역변수는 초기화 바로 못하는 경우가 있으니까
        var lastName : String = ""
        var age : Int = 10
        
        fun work() : Unit {

        }

        fun sleep() : Unit {

        }

    }

    fun sum(a : Int, b : Int) = a + b
}