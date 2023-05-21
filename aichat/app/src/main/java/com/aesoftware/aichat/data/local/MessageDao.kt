package com.aesoftware.aichat.data.local

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.aesoftware.aichat.database.RoomEntity
import kotlinx.coroutines.flow.Flow

@Dao
interface MessageDao {
    @Query("SELECT * FROM messages")
    fun getAllMessages(): Flow<List<RoomEntity>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertMessage(messageEntity: RoomEntity)

    @Query("DELETE FROM messages WHERE sender = 'typing'")
    suspend fun deleteTypingMessages()

    @Query("DELETE FROM messages")
    suspend fun deleteAllMessages()
}