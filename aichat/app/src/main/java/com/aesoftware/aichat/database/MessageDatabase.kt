package com.aesoftware.aichat.database

import androidx.room.Database
import androidx.room.RoomDatabase
import com.aesoftware.aichat.data.local.MessageDao

@Database(entities = [RoomEntity::class], version = 1, exportSchema = false)
abstract class MessageDatabase: RoomDatabase() {

    abstract fun messageDao(): MessageDao
}