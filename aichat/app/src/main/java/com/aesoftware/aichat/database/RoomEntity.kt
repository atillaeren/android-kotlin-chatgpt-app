package com.aesoftware.aichat.database

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey


@Entity(tableName = "messages")
data class RoomEntity (

    @PrimaryKey(autoGenerate = true)
    var id: Int = 0,

    @ColumnInfo(name = "message")
    var message : String,

    @ColumnInfo(name = "sender")
    var sender : String
)