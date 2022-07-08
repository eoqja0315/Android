package com.dbhong.cp03.kidsnotetask.repository

import android.util.Log
import android.widget.Toast
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.dbhong.cp03.kidsnotetask.AppDatabase
import com.dbhong.cp03.kidsnotetask.api.PicsumService
import com.dbhong.cp03.kidsnotetask.model.Picture
import com.dbhong.cp03.kidsnotetask.view.MainActivity
import com.google.gson.JsonArray
import kotlinx.coroutines.processNextEventInCurrentThread
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

class Repository(database : AppDatabase) {

    private val pictureDao = database.pictureDao()
    val localAllPicture : LiveData<List<Picture>> = pictureDao.getAll()

    private lateinit var picsumService: PicsumService
    val allPicture : MutableLiveData<List<Picture>> = MutableLiveData()

    init {
        initPicsumService()
    }

    private fun initPicsumService() {
        val retrofit = Retrofit.Builder()
            .baseUrl(MainActivity.GET_PICTURE_URL)
            .addConverterFactory(GsonConverterFactory.create())
            .build()

        picsumService = retrofit.create(PicsumService::class.java)
    }

    suspend fun getAll() : LiveData<List<Picture>>{
        return pictureDao.getAll()
    }

    suspend fun insertPicture(picture: Picture) {
        pictureDao.insertPicture(picture)
    }

    suspend fun getPicturesFromServer(){
        val pictures = mutableListOf<Picture>()

        picsumService.getPictures()
            .enqueue(object : Callback<JsonArray> {
                override fun onResponse(
                    call: Call<JsonArray>,
                    response: Response<JsonArray>
                ) {
                    val result = response.body()

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

                    allPicture.value = pictures
                }

                override fun onFailure(call: Call<JsonArray>, t: Throwable) {
                    t.printStackTrace()
                    Log.e(TAG, "Failed code : ${t.message}")
                }
            })
    }

    companion object {
        const val TAG = "Repository"
        private var sInstance : Repository? = null
        fun getInstance(database: AppDatabase) : Repository {
            return sInstance ?: synchronized(this) {
                val instance = Repository(database)
                sInstance = instance
                instance
            }
        }
    }
}