package com.example.sonatale.data.translate

import com.google.gson.annotations.SerializedName

data class TranslateResponse (
    @SerializedName("translated_text")val translatedText: String?
)