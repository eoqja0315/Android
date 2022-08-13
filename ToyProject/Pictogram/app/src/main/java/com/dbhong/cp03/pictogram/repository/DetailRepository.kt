package com.dbhong.cp03.pictogram.repository

import androidx.lifecycle.LiveData
import com.dbhong.cp03.pictogram.AppDatabase
import com.dbhong.cp03.pictogram.model.PicsumPicture

class DetailRepository(database : AppDatabase, private val id : Int) : Repository(database) {

    val detailPicsumPicture : LiveData<PicsumPicture> = pictureDao.getOnePictureById(id)

}