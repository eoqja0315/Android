
package com.dbhong.cp03.ch06

import androidx.fragment.app.Fragment
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.dbhong.cp03.ch06.chatlist.ChatListFragment
import com.dbhong.cp03.ch06.home.HomeFragment
import com.dbhong.cp03.ch06.mypage.MyPageFragment
import com.google.android.material.bottomnavigation.BottomNavigationView

class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        val bottomNavigationView = findViewById<BottomNavigationView>(R.id.bottomNavigationView)

        val homeFragment = HomeFragment()
        val chatListFragment = ChatListFragment()
        val myPageFragment = MyPageFragment()

        bottomNavigationView.setOnItemSelectedListener {
            when(it.itemId) {
                R.id.home -> {

                }
                R.id.chatList -> {

                }
                R.id.myPage -> {

                }
            }
            true
        }
    }

    private fun replaceFragment(fragment : Fragment) {
        supportFragmentManager.beginTransaction().apply {
            replace(R.id.fragmentContainer, fragment)
            commit()
        }
    }
}