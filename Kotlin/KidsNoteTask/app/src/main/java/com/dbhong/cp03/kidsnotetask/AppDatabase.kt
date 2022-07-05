package com.dbhong.cp03.kidsnotetask

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import com.dbhong.cp03.kidsnotetask.dao.PictureDao
import com.dbhong.cp03.kidsnotetask.model.Picture

@Database(entities = [Picture::class], version = 1)
abstract class AppDatabase : RoomDatabase() {
    abstract fun pictureDao() : PictureDao
}

fun getAppDatabase(context : Context) : AppDatabase {

    return Room.databaseBuilder(
        context,
        AppDatabase::class.java,
        "KidsNoteTaskDB"
    )
        .build()
}