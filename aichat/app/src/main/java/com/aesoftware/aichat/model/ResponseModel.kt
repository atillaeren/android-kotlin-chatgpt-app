package com.aesoftware.aichat.model

data class ResponseModel(
    val choices: List<Choice>
){
    data class Choice(
        val message: Message
    )

    data class Message(
        val role: String,
        val content: String
    )
}

