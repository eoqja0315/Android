package com.dbhong.cp03.kidsnotetask.dao

import androidx.lifecycle.LiveData
import androidx.room.*
import com.dbhong.cp03.kidsnotetask.model.Picture

@Dao
interface PictureDao {
    @Query("SELECT * FROM picture")
    fun getAll() : LiveData<List<Picture>>

    @Query("SELECT * FROM picture WHERE id == :id")
    fun getOnePictureById(id : Int) : Picture?

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insertPicture(picture : Picture)
}