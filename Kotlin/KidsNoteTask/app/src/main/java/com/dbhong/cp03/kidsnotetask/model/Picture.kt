package com.dbhong.cp03.kidsnotetask.model

import android.os.Parcelable
import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import com.google.gson.annotations.SerializedName
import kotlinx.parcelize.Parcelize

@Parcelize
@Entity
data class Picture (
    @SerializedName("id") @PrimaryKey val id : Int,
    @SerializedName("author") val author : String,
    @SerializedName("width") val width : Int,
    @SerializedName("height") val height : Int,
    @SerializedName("url") val url : String,
    @SerializedName("download_url") val downloadUrl : String,
    @SerializedName("like") @ColumnInfo(name ="like") var like : Boolean,
) : Parcelable