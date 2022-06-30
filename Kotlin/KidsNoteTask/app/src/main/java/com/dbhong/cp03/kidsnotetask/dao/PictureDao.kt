package com.dbhong.cp03.kidsnotetask.dao

import androidx.room.*
import com.dbhong.cp03.kidsnotetask.model.Picture

@Dao
interface PictureDao {
    @Query("SELECT * FROM picture")
    fun getAllPictures() : List<Picture>

    @Query("SELECT * FROM picture WHERE id == :id")
    fun getOnePictureById(id : Int) : Picture

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun savePictureById(picture : Picture)
}