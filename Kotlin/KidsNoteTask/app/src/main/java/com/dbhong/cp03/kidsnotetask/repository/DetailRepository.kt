package com.dbhong.cp03.kidsnotetask.repository

import androidx.lifecycle.LiveData
import com.dbhong.cp03.kidsnotetask.AppDatabase
import com.dbhong.cp03.kidsnotetask.model.Picture

class DetailRepository(database : AppDatabase, private val id : Int) : Repository(database) {

    val detailPicture : LiveData<Picture> = pictureDao.getOnePictureById(id)

}