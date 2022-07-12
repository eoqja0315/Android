package com.dbhong.cp03.kidsnotetask.repository

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.dbhong.cp03.kidsnotetask.AppDatabase
import com.dbhong.cp03.kidsnotetask.api.PicsumService
import com.dbhong.cp03.kidsnotetask.model.PicsumPicture
import com.dbhong.cp03.kidsnotetask.view.MainActivity
import com.google.gson.JsonArray
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

open class Repository(database : AppDatabase) {

    protected val pictureDao = database.pictureDao()
    val localAllPicsumPicture : LiveData<List<PicsumPicture>> = pictureDao.getAll()

    private lateinit var picsumService: PicsumService
    val allPicsumPicture : MutableLiveData<List<PicsumPicture>> = MutableLiveData()

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

    suspend fun insertPicture(picsumPicture: PicsumPicture) {
        Log.i(TAG, "++ [I] : insertPicture ++")
        pictureDao.insertPicture(picsumPicture)
    }

    suspend fun getPicsumPicturesFromServer(){
        val picsumPictures = mutableListOf<PicsumPicture>()

        picsumService.getPictures()
            .enqueue(object : Callback<JsonArray> {
                override fun onResponse(
                    call: Call<JsonArray>,
                    response: Response<JsonArray>
                ) {
                    val result = response.body()

                    if (result != null) {
                        for (pictureResult in result) {
                            val picsumPicture = PicsumPicture(
                                    id = pictureResult.asJsonObject.get("id").asInt,
                                    author = pictureResult.asJsonObject.get("author").asString,
                                    width = pictureResult.asJsonObject.get("width").asInt,
                                    height = pictureResult.asJsonObject.get("height").asInt,
                                    url = pictureResult.asJsonObject.get("url").asString,
                                    downloadUrl = pictureResult.asJsonObject.get("download_url").asString,
                                    false
                                )

                            picsumPictures.add(picsumPicture)
                        }
                    }

                    allPicsumPicture.value = picsumPictures
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