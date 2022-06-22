package com.dbhong.cp03.ch04

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import androidx.room.migration.Migration
import androidx.sqlite.db.SupportSQLiteDatabase
import com.dbhong.cp03.ch04.dao.HistoryDao
import com.dbhong.cp03.ch04.dao.ReviewDao
import com.dbhong.cp03.ch04.model.History
import com.dbhong.cp03.ch04.model.Review

@Database(entities = [History::class, Review::class], version = 1)
abstract class AppDatabase : RoomDatabase() {
    abstract fun historyDao() : HistoryDao
    abstract fun reviewDao() : ReviewDao
}

fun getAppDatabase(context : Context) : AppDatabase {

    val migration_1_2 = object : Migration(1, 2) {
        override fun migrate(database: SupportSQLiteDatabase) {
            database.execSQL("CREATE TABLE `REVIEW` (`isbn` TEXT, `review` TEXT," + "PRIMARY KEY(`isbn`)) ")
        }
    }

    return Room.databaseBuilder(
        context,
        AppDatabase::class.java,
        "BookSearchDB"
    )
        .addMigrations(migration_1_2)
        .build()
}