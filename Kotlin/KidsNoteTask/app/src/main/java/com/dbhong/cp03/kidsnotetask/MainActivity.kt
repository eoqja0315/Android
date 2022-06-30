package com.dbhong.cp03.kidsnotetask

import android.animation.ValueAnimator
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.ArrayAdapter
import android.widget.Toast
import androidx.recyclerview.widget.LinearLayoutManager
import com.dbhong.cp03.kidsnotetask.adapter.PictureAdapter
import com.dbhong.cp03.kidsnotetask.api.PicsumService
import com.dbhong.cp03.kidsnotetask.databinding.ActivityMainBinding
import com.dbhong.cp03.kidsnotetask.model.Picture
import com.google.gson.JsonArray
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding
    private lateinit var pictureAdapter: PictureAdapter
    private lateinit var picsumService: PicsumService

    private lateinit var db : AppDatabase

    private lateinit var pictures : MutableList<Picture>

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        db = getAppDatabase(this)

        initPicsumService()
        getPicturesFromApi()
        initPictureRecyclerView()
    }

    private fun initPicsumService() {
        val retrofit = Retrofit.Builder()
            .baseUrl("https://picsum.photos")
            .addConverterFactory(GsonConverterFactory.create())
            .build()

        picsumService = retrofit.create(PicsumService::class.java)
    }

    private fun initPictureRecyclerView() {
        pictureAdapter = PictureAdapter (itemClickListener = {
            val intent = Intent(this, DetailActivity::class.java)
            intent.putExtra("pictureModel", it)
            startActivity(intent)
        })

        binding.pictureRecyclerView.layoutManager = LinearLayoutManager(this)
        binding.pictureRecyclerView.adapter = pictureAdapter
    }

    private fun getPicturesFromApi() {
        picsumService.getPictures()
            .enqueue(object : Callback<JsonArray> {
                override fun onResponse(
                    call: Call<JsonArray>,
                    response: Response<JsonArray>
                ) {
                    val result = response.body()
                    pictures = mutableListOf()

                    if (result != null) {
                        for (pictureResult in result) {

                            val picture = Picture(
                                id = pictureResult.asJsonObject.get("id").asInt,
                                author = pictureResult.asJsonObject.get("author").asString,
                                width = pictureResult.asJsonObject.get("width").asInt,
                                height = pictureResult.asJsonObject.get("height").asInt,
                                url = pictureResult.asJsonObject.get("url").asString,
                                downloadUrl = pictureResult.asJsonObject.get("download_url").asString,
                                false
                            )

                            pictures.add(picture)
                        }
                    }

                    pictureAdapter.submitList(pictures)

//                    getLikeDataFromDB() {
//                        runOnUiThread {
//                            pictureAdapter.submitList(pictures)
//                        }
//                    }
                }

                override fun onFailure(call: Call<JsonArray>, t: Throwable) {
                    t.printStackTrace()
                    Toast.makeText(applicationContext, "Failed code : ${t.message}", Toast.LENGTH_SHORT).show()
                }
            })
    }

//    private fun getLikeDataFromDB(updateListener: (MutableList<Picture>) -> (Unit)) {
//        Thread {
//            for(i in pictures.indices) {
//                pictures[i].like = db.pictureDao().getOnePictureById(pictures[i].id).like
//            }
//            updateListener(pictures)
//        }.start()
//    }

    companion object {
        const val TAG = "MainActivity"
    }
}