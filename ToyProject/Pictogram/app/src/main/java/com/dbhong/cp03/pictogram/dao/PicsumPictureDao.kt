package com.dbhong.cp03.pictogram.dao

import androidx.lifecycle.LiveData
import androidx.room.*
import com.dbhong.cp03.pictogram.model.PicsumPicture

@Dao
interface PicsumPictureDao {
    @Query("SELECT * FROM picsumpicture")
    fun getAll() : LiveData<List<PicsumPicture>>

    @Query("SELECT * FROM picsumpicture WHERE id == :id")
    fun getOnePictureById(id : Int) : LiveData<PicsumPicture>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insertPicture(picsumPicture : PicsumPicture)
}