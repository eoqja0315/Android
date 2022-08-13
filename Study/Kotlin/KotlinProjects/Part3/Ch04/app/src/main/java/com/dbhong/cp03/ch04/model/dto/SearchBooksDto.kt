package com.dbhong.cp03.ch04.model.dto

import com.dbhong.cp03.ch04.model.Book
import com.google.gson.annotations.SerializedName

data class SearchBooksDto(
    @SerializedName("items") val books : List<Book>
)