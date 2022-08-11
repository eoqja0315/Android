package com.dbhong.cp03.kidsnotetask.model.dto

import com.dbhong.cp03.kidsnotetask.model.PicsumPicture
import com.google.gson.annotations.SerializedName

data class SearchPicturesDto(
    @SerializedName("pictures") val picsumPictures: List<PicsumPicture>
)

