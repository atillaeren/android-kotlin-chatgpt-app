package com.aesoftware.aichat.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.aesoftware.aichat.database.RoomEntity
import com.aesoftware.aichat.repository.ChatRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class ChatViewModel @Inject constructor(private val repository: ChatRepository) : ViewModel() {
    private val _messageList = MutableStateFlow<List<RoomEntity>>(emptyList())

    val allMessageList: Flow<List<RoomEntity>> = repository.allMessages

    suspend fun getResponse(query: String) {
        addMessage(RoomEntity(0,query, "user"))
        emitTyping()

        viewModelScope.launch {
            val chatModel = repository.getResponse(query)
            chatModel.collectLatest {
                repository.deleteMessages()
                addMessage(it)
            }
        }
    }

    suspend fun deleteAll() {
        repository.deleteAllMessages()
    }

    private suspend fun emitTyping() {
        viewModelScope.launch {
            delay(100)
            addMessage(RoomEntity(0, "", "typing"))
        }
    }

    suspend fun botWelcomeMessage() {
        viewModelScope.launch {
            delay(100)
            addMessage(RoomEntity(0, "Hello! How can i help you?", "bot"))
        }
    }

    private suspend fun addMessage(roomEntity: RoomEntity) {
        val messages = _messageList.value.toMutableList()
        messages.add(roomEntity)
        repository.insertMessage(roomEntity)
        _messageList.value = messages
    }
}