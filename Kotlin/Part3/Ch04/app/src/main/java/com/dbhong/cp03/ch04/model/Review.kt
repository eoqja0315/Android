package com.dbhong.cp03.ch04.model

import androidx.annotation.NonNull
import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity
data class Review(
    @PrimaryKey val isbn : String,
    @ColumnInfo(name ="review") val review : String?
)