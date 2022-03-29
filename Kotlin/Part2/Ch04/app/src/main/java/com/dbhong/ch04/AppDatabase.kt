package com.dbhong.ch04

import androidx.room.Database
import androidx.room.RoomDatabase
import com.dbhong.ch04.dao.HistoryDao
import com.dbhong.ch04.model.History

@Database(entities = [History::class], version = 1)
abstract class AppDatabase : RoomDatabase() {
    abstract fun historyDao() : HistoryDao
}