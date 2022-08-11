package com.dbhong.cp03.ch06.home

import android.os.Bundle
import android.view.View
import androidx.fragment.app.Fragment
import com.dbhong.cp03.ch06.R
import com.dbhong.cp03.ch06.databinding.FragmentHomeBinding

class HomeFragment : Fragment(R.layout.fragment_home){

    private var binding : FragmentHomeBinding? = null

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val fragmentHomeBinding = FragmentHomeBinding.bind(view)
        binding = fragmentHomeBinding
    }
}