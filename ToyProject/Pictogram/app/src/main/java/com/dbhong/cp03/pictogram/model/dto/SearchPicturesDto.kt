package com.dbhong.cp03.pictogram.model.dto

import com.dbhong.cp03.pictogram.model.PicsumPicture
import com.google.gson.annotations.SerializedName

data class SearchPicturesDto(
    @SerializedName("pictures") val picsumPictures: List<PicsumPicture>
)

