package com.dbhong.cp03.ch04.model

import android.os.Parcelable
import com.google.gson.annotations.SerializedName
import kotlinx.parcelize.Parcelize

@Parcelize
data class Book(
    @SerializedName("isbn") val isbn : String,
    @SerializedName("title") val title : String,
    @SerializedName("description") val description: String,
    @SerializedName("price") val priceSales: String,
    @SerializedName("image") val coverSmallUrl: String,
    @SerializedName("link") val mobileLink: String,
    @SerializedName("author") val author: String
) : Parcelable