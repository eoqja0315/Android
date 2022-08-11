package com.dbhong.cp03.kidsnotetask

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import com.dbhong.cp03.kidsnotetask.dao.PicsumPictureDao
import com.dbhong.cp03.kidsnotetask.model.PicsumPicture

@Database(entities = [PicsumPicture::class], version = 1)
abstract class AppDatabase : RoomDatabase() {
    abstract fun pictureDao() : PicsumPictureDao
}

fun getAppDatabase(context : Context) : AppDatabase {

    return Room.databaseBuilder(
        context,
        AppDatabase::class.java,
        "KidsNoteTaskDB"
    )
        .build()
}