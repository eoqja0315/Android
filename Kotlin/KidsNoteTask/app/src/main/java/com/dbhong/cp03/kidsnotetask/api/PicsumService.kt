package com.dbhong.cp03.kidsnotetask.api

import retrofit2.Call
import com.google.gson.JsonArray
import retrofit2.http.GET
import retrofit2.http.Query

interface PicsumService {

    @GET("/v2/list")
    fun getPictures(
    ) : Call<JsonArray>

    @GET("/v2/list")
    fun getPictures(
        @Query("page") pageNum : String,
        @Query("limit")  limit : String
    ) : Call<JsonArray>
}