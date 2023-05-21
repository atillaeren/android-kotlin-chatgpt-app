package com.aesoftware.aichat.repository

import android.util.Log
import androidx.annotation.WorkerThread
import com.aesoftware.aichat.data.api.ChatApiService
import com.aesoftware.aichat.model.RequestModel
import com.aesoftware.aichat.data.local.MessageDao
import com.aesoftware.aichat.database.RoomEntity
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.flowOn
import javax.inject.Inject


class ChatRepository @Inject constructor(private val apiService: ChatApiService, private val imageDAO: MessageDao) {

    val allMessages: Flow<List<RoomEntity>> = imageDAO.getAllMessages()

    @WorkerThread
    suspend fun insertMessage(roomEntity: RoomEntity) {
        imageDAO.insertMessage(roomEntity)
    }

    @WorkerThread
    suspend fun deleteMessages() {
        imageDAO.deleteTypingMessages()
    }

    //silinecek-
    @WorkerThread
    suspend fun deleteAllMessages() {
        imageDAO.deleteAllMessages()
    }

    suspend fun getResponse(query: String): Flow<RoomEntity> = flow {
        val response = apiService.getResponse(
            RequestModel(
                model = "gpt-3.5-turbo",
                messages = listOf(
                    RequestModel.Message(
                        role = "user",
                        content = query
                    )
                ),
                temperature = 0.1,
                max_tokens = 4000
            )
        )
        val responseMsg = response.choices[0].message.content
        Log.d("choices","${response.choices[0]}")
        val adjusted: String = responseMsg.replace("\n", "")
        emit(RoomEntity(0, adjusted, "bot"))
    }.catch { e ->
        // Handle any exceptions here
        Log.e("ChatRepository", "getResponse error: $e")

        //sor
        emit(RoomEntity(0, "Sorry, something went wrong...", "bot"))
    }.flowOn(Dispatchers.IO)
}