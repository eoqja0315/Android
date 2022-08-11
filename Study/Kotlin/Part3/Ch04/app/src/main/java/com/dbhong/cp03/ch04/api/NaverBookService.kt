package com.dbhong.cp03.ch04.api

import com.dbhong.cp03.ch04.model.dto.SearchBooksDto
import retrofit2.Call
import retrofit2.http.GET
import retrofit2.http.Header
import retrofit2.http.Query

interface NaverBookService {

    @GET("/v1/search/book.json")
    fun getBooksByName(
        @Header("X-Naver-Client-Id") clientId : String,
        @Header("X-Naver-Client-Secret") secretKey: String,
        @Query("query") keyword : String
    ) : Call<SearchBooksDto>
}