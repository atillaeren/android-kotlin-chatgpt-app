package com.aesoftware.aichat.model

data class RequestModel(
    val model: String,
    val messages: List<Message>,
    val temperature: Double,
    val max_tokens: Int
) {
    data class Message(
        val role: String,
        val content: String
    )
}