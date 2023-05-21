package com.aesoftware.aichat.data.api

import com.aesoftware.aichat.utils.Constants
import com.aesoftware.aichat.model.RequestModel
import com.aesoftware.aichat.model.ResponseModel
import retrofit2.http.Body
import retrofit2.http.Headers
import retrofit2.http.POST

interface ChatApiService {

    //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
    @Headers(
        "Content-Type: application/json",
        "Authorization: Bearer ${Constants.CHAT_API_KEY}"
    )
    @POST("v1/chat/completions")
    suspend fun getResponse(@Body request: RequestModel): ResponseModel
}