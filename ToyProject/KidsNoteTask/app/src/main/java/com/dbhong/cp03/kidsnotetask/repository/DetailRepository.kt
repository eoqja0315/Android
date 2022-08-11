package com.dbhong.cp03.kidsnotetask.repository

import androidx.lifecycle.LiveData
import com.dbhong.cp03.kidsnotetask.AppDatabase
import com.dbhong.cp03.kidsnotetask.model.PicsumPicture

class DetailRepository(database : AppDatabase, private val id : Int) : Repository(database) {

    val detailPicsumPicture : LiveData<PicsumPicture> = pictureDao.getOnePictureById(id)

}