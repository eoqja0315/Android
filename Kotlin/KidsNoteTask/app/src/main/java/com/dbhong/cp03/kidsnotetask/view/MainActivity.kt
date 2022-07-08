package com.dbhong.cp03.kidsnotetask.view

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.activity.viewModels
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.Observer
import androidx.recyclerview.widget.LinearLayoutManager
import com.dbhong.cp03.kidsnotetask.R
import com.dbhong.cp03.kidsnotetask.adapter.PictureAdapter
import com.dbhong.cp03.kidsnotetask.databinding.ActivityMainBinding
import com.dbhong.cp03.kidsnotetask.viewmodel.MainActivityViewModel

class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding
    private lateinit var pictureAdapter: PictureAdapter
    private val viewModel : MainActivityViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        val dataBinding = DataBindingUtil.setContentView<ActivityMainBinding>(this, R.layout.activity_main)

        viewModel.allPicture.observe(this, Observer {
            it?.let{
                pictureAdapter.setPictures(it)
            }
        })

//        viewModel.serverPicture.observe(this, Observer {
//            it?.let{
//                pictureAdapter.setPictures(it)
//            }
//        })

        setContentView(binding.root)

        viewModel.getPicutresFromServer()
        initPictureRecyclerView()
    }

    override fun onResume() {
        super.onResume()
    }

    private fun initPictureRecyclerView() {
        pictureAdapter = PictureAdapter (itemClickListener = {
            val intent = Intent(this, DetailActivity::class.java)
            intent.putExtra(PICTURE_MODEL_INTENT_NAME, it)
            startActivity(intent)
        })

        binding.pictureRecyclerView.layoutManager = LinearLayoutManager(this)
        binding.pictureRecyclerView.adapter = pictureAdapter
    }

    companion object {
        const val TAG = "MainActivity"
        const val GET_PICTURE_URL = "https://picsum.photos"
        const val PICTURE_MODEL_INTENT_NAME = "pictureModel"
    }
}