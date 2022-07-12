package com.dbhong.cp03.kidsnotetask.view

import android.content.Intent
import android.net.Network
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import androidx.activity.viewModels
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.Observer
import androidx.recyclerview.widget.LinearLayoutManager
import com.dbhong.cp03.kidsnotetask.R
import com.dbhong.cp03.kidsnotetask.`interface`.NetworkResult
import com.dbhong.cp03.kidsnotetask.adapter.PictureAdapter
import com.dbhong.cp03.kidsnotetask.databinding.ActivityMainBinding
import com.dbhong.cp03.kidsnotetask.viewmodel.MainActivityViewModel
import kotlinx.coroutines.*
import kotlinx.coroutines.flow.collect

class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding
    private lateinit var pictureAdapter: PictureAdapter
    private val viewModel: MainActivityViewModel by viewModels()

    @OptIn(ExperimentalCoroutinesApi::class)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        initPictureRecyclerView()
        MainScope().launch {
            viewModel.getResultOfPicsumPicturesFromServer().collect {
                if(it == "SUCCESS"){
                    //pictureAdapter.setPictures(viewModel.allPicture.value ?: emptyList())
                }
            }
        }

//        viewModel.localAllPicture.observe(this, Observer {
//            it?.let {
//                pictureAdapter.setPictures(viewModel.updateLike(it))
//            }
//        })

        viewModel.allPicture.observe(this, Observer {
            it?.let {
                pictureAdapter.setPictures(it)
            }
        })
    }

    private fun initPictureRecyclerView() {
        pictureAdapter = PictureAdapter(
            itemClickListener = {
                val intent = Intent(this, DetailActivity::class.java)

                intent.putExtra(PICTURE_ID, it.value?.id)
                intent.putExtra(PICTURE_AUTHOR, it.value?.author)
                intent.putExtra(PICTURE_WIDTH, it.value?.width)
                intent.putExtra(PICTURE_HEIGHT, it.value?.height)
                intent.putExtra(PICTURE_URL, it.value?.url)
                intent.putExtra(PICTURE_DOWNLOAD_URL, it.value?.downloadUrl)
                intent.putExtra(PICTURE_LIKE, it.value?.like)

                startActivity(intent)
            }, likeImageButtonClickListener = {
                viewModel.insertPicsumPicture(picsumPicture = it)
            })

        binding.pictureRecyclerView.layoutManager = LinearLayoutManager(this)
        binding.pictureRecyclerView.adapter = pictureAdapter
    }

    companion object {
        const val TAG = "MainActivity"
        const val GET_PICTURE_URL = "https://picsum.photos"
        const val PICTURE_MODEL_INTENT_NAME = "pictureModel"

        const val PICTURE_ID = "id"
        const val PICTURE_AUTHOR = "author"
        const val PICTURE_WIDTH = "width"
        const val PICTURE_HEIGHT = "height"
        const val PICTURE_URL = "url"
        const val PICTURE_DOWNLOAD_URL = "downloadUrl"
        const val PICTURE_LIKE = "like"
    }
}