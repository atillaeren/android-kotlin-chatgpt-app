package com.aesoftware.aichat.adapter


import com.google.gson.annotations.SerializedName

data class Tune(
    @SerializedName("tune")
    val tune: Tune
) {
    data class Tune(
        @SerializedName("branch")
        val branch: String,
        @SerializedName("image_urls")
        val imageUrls: List<String>,
        @SerializedName("name")
        val name: String,
        @SerializedName("prompt_attributes")
        val promptAttributes: List<PromptAttribute>,
        @SerializedName("title")
        val title: String
    ) {
        data class PromptAttribute(
            @SerializedName("cfg_scale")
            val cfgScale: Double,
            @SerializedName("num_images")
            val numImages: Int,
            @SerializedName("seed")
            val seed: Int,
            @SerializedName("text")
            val text: String
        )
    }
}